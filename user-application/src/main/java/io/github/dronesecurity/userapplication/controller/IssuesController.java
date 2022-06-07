/*
 * Copyright (c) 2021-2022, Mirko Felice & Maxim Derevyanchenko. All rights reserved.
 * Licensed under the MIT license. See LICENSE file in the project root for details.
 */

package io.github.dronesecurity.userapplication.controller;

import io.github.dronesecurity.lib.DateHelper;
import io.github.dronesecurity.userapplication.auth.entities.LoggedUser;
import io.github.dronesecurity.userapplication.auth.entities.Role;
import io.github.dronesecurity.userapplication.reporting.issue.entities.*;
import io.github.dronesecurity.userapplication.reporting.issue.serialization.IssueStringHelper;
import io.github.dronesecurity.userapplication.reporting.issue.services.MaintainerIssueReportService;
import io.github.dronesecurity.userapplication.utilities.*;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import org.jetbrains.annotations.NotNull;

import java.net.URL;
import java.util.Comparator;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.function.Consumer;
import java.util.stream.Collectors;

/**
 * The controller of the issue creation service.
 */
public class IssuesController implements Initializable {

    private static final String NEW_ISSUE_FXML = "newIssue.fxml";
    @FXML
    private TabPane issuesPane;

    // Issue Details visualization nodes
    @FXML private GridPane selectedIssuePane;
    @FXML private Label selectedIssueSubject;
    @FXML private Label issueState;
    @FXML private Label selectedIssueCreationDate;
    @FXML private Label selectedIssueCreationTime;
    @FXML private Label selectedIssueCourier;
    @FXML private Text selectedIssueDetails;
    @FXML private Button visionIssueButton;
    @FXML private Button goToClosingPageButton;

    // Open issues list visualization nodes
    @FXML private TableView<CreatedIssue> openIssuesTable;
    @FXML private TableColumn<CreatedIssue, String> openIssuesId;
    @FXML private TableColumn<CreatedIssue, String> openIssuesSubject;
    @FXML private TableColumn<CreatedIssue, String> openIssuesCourier;
    @FXML private TableView<CreatedIssue> closedIssuesTable;
    @FXML private TableColumn<CreatedIssue, String> closedIssuesId;
    @FXML private TableColumn<CreatedIssue, String> closedIssuesSubject;
    @FXML private TableColumn<CreatedIssue, String> closedIssuesCourier;

    // Closing issue panel
    @FXML private AnchorPane closingIssuePane;
    @FXML private TextArea solutionTextArea;

    @FXML private Button newIssueButton;
    @FXML private Label solutionLabel;
    @FXML private Text closedIssueSolution;

    private final MaintainerIssueReportService issueReportService;

    private CreatedIssue currentlySelectedIssue;
    private final Role role;

    /**
     * Instantiates the issue report controller with its service.
     */
    public IssuesController() {
        final LoggedUser loggedUser = UserHelper.logged();
        this.role = loggedUser.getRole();
        this.issueReportService = MaintainerIssueReportService.getInstance();

        if (this.role == Role.MAINTAINER)
            this.issueReportService.subscribeToNewIssue(loggedUser.getUsername(), issue ->
                Platform.runLater(() -> {
                    this.refreshOpenIssues();
                    DialogUtils.showInfoNotification("INFO", "You have received a new issue!",
                            this.issuesPane.getScene().getWindow());
                }));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void initialize(final URL location, final ResourceBundle resources) {
        this.newIssueButton.setVisible(this.role == Role.COURIER);

        this.initTable(this.openIssuesTable, this.openIssuesId, this.openIssuesSubject, this.openIssuesCourier,
                issue -> {
            this.currentlySelectedIssue = issue;
            this.solutionLabel.setVisible(false);
            this.closedIssueSolution.setVisible(false);
            this.visionIssueButton.setVisible(
                    this.role == Role.MAINTAINER && IssueStringHelper.STATUS_OPEN.equals(issue.getState()));
            this.goToClosingPageButton.setVisible(
                    this.role == Role.MAINTAINER && IssueStringHelper.STATUS_VISIONED.equals(issue.getState()));
            this.newIssueButton.setVisible(false);

            this.fillIssueFields();
        });

        this.initTable(this.closedIssuesTable, this.closedIssuesId, this.closedIssuesSubject, this.closedIssuesCourier,
                issue -> {
                    this.currentlySelectedIssue = issue;
                    this.solutionLabel.setVisible(true);
                    this.closedIssueSolution.setVisible(true);
                    this.closedIssueSolution.setText(((ClosedIssue) issue).getIssueSolution());
                    this.visionIssueButton.setVisible(false);
                    this.goToClosingPageButton.setVisible(false);
                    this.newIssueButton.setVisible(false);
                    this.fillIssueFields();
        });

        if (this.role == Role.COURIER) {
            this.openIssuesTable.getColumns().remove(this.openIssuesCourier);
            this.closedIssuesTable.getColumns().remove(this.closedIssuesCourier);
        }

        this.refreshOpenIssues();
        this.issueReportService.getClosedIssueReports().onSuccess(issues ->
                this.closedIssuesTable.setItems(FXCollections.observableList(issues.stream()
                        .sorted(Comparator.comparingInt(CreatedIssue::getId))
                        .collect(Collectors.toList()))));
    }

    @FXML
    private void newIssue() {
        final FXMLLoader loader = new FXMLLoader(IssuesController.class.getResource(NEW_ISSUE_FXML));
        FXHelper.initializeWindow(Modality.WINDOW_MODAL, "Create new Issue", loader).ifPresent(stage -> {
            stage.initOwner(this.issuesPane.getScene().getWindow());
            stage.setOnHidden(ignored -> this.refreshOpenIssues());
            stage.showAndWait();
        });
    }

    @FXML
    private void visionIssue() {
        CastHelper.safeCast(this.currentlySelectedIssue, OpenIssue.class).ifPresent(openIssue ->
                this.issueReportService.visionIssue(openIssue).onComplete(res -> {
                    if (Boolean.TRUE.equals(res.result())) {
                        Platform.runLater(() -> {
                            this.refreshOpenIssues();
                            final TableView.TableViewSelectionModel<CreatedIssue> selectionModel =
                                    this.openIssuesTable.getSelectionModel();

                            selectionModel.clearSelection();
                            selectionModel.select(this.currentlySelectedIssue.getId() - 1);
                        });
                    } else
                        DialogUtils.showErrorDialog("Error connecting to issue information. Please retry.");
                }));
    }

    @FXML
    private void closeIssue() {
        CastHelper.safeCast(this.currentlySelectedIssue, VisionedIssue.class).ifPresent(visionedIssue ->
                this.issueReportService.closeIssue(visionedIssue, this.solutionTextArea.getText())
                        .onSuccess(succeeded -> {
                            if (Boolean.TRUE.equals(succeeded)) {
                                Platform.runLater(() -> {
                                    final ClosedIssue closedIssue =
                                            visionedIssue.closeIssue(this.solutionTextArea.getText());
                                    this.refreshOpenIssues();
                                    this.issueReportService.getClosedIssueReports().onSuccess(issues ->
                                            this.closedIssuesTable.setItems(FXCollections.observableList(issues.stream()
                                                    .sorted(Comparator.comparingInt(CreatedIssue::getId))
                                                    .collect(Collectors.toList()))));

                                    this.currentlySelectedIssue = closedIssue;

                                    this.openIssuesTable.getSelectionModel().clearSelection();
                                    this.closedIssuesTable.getSelectionModel().select(this.currentlySelectedIssue);
                                    this.cancelClosing();
                                });
                            }
                        }));
    }

    @FXML
    private void goBackFromOpenIssues() {
        this.selectedIssuePane.setVisible(false);
        this.openIssuesTable.getSelectionModel().clearSelection();
        this.closedIssuesTable.getSelectionModel().clearSelection();
        this.issuesPane.setVisible(true);
        this.newIssueButton.setVisible(this.role == Role.COURIER);
    }

    @FXML
    private void goToClosingPage() {
        this.selectedIssuePane.setVisible(false);
        this.closingIssuePane.setVisible(true);
    }

    @FXML
    private void cancelClosing() {
        this.solutionTextArea.clear();
        this.closingIssuePane.setVisible(false);
        this.selectedIssuePane.setVisible(true);
    }

    private void initTable(final @NotNull TableView<CreatedIssue> table,
                           final @NotNull TableColumn<CreatedIssue, String> idColumn,
                           final @NotNull TableColumn<CreatedIssue, String> subjectColumn,
                           final @NotNull TableColumn<CreatedIssue, String> courierColumn,
                           final Consumer<CreatedIssue> consumer) {
        idColumn.setCellValueFactory(val -> new SimpleStringProperty("#" + val.getValue().getId()));
        subjectColumn.setCellValueFactory(val -> new SimpleStringProperty(val.getValue().getSubject()));
        courierColumn.setCellValueFactory(val -> new SimpleStringProperty(val.getValue().getCourier()));

        final TableView.TableViewSelectionModel<CreatedIssue> selectionModel = table.getSelectionModel();
        selectionModel.setSelectionMode(SelectionMode.SINGLE);
        selectionModel.selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null)
                consumer.accept(newValue);
        });
    }

    private void fillIssueFields() {
        this.selectedIssueSubject.setText(this.currentlySelectedIssue.getSubject());
        this.issueState.setText(this.currentlySelectedIssue.getState().substring(0, 1).toUpperCase(Locale.ITALY)
                + this.currentlySelectedIssue.getState().substring(1));
        final String creationInstant = DateHelper.toString(this.currentlySelectedIssue.getReportingDate());
        final String[] instantComponents = creationInstant.split(" ");
        this.selectedIssueCreationDate.setText(instantComponents[0]);
        this.selectedIssueCreationTime.setText(instantComponents[1]);
        this.selectedIssueCourier.setText(this.currentlySelectedIssue.getCourier());
        this.selectedIssueDetails.setText(this.currentlySelectedIssue.getDetails());

        this.issuesPane.setVisible(false);
        this.selectedIssuePane.setVisible(true);
    }

    private void refreshOpenIssues() {
        this.issueReportService.getOpenIssueReports().onSuccess(issues -> Platform.runLater(() ->
                this.openIssuesTable.setItems(FXCollections.observableList(issues.stream()
                        .sorted(Comparator.comparingInt(CreatedIssue::getId))
                        .collect(Collectors.toList())))));
    }

}
