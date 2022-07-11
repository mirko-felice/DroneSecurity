/*
 * Copyright (c) 2021-2022, Mirko Felice & Maxim Derevyanchenko. All rights reserved.
 * Licensed under the MIT license. See LICENSE file in the project root for details.
 */

package io.github.dronesecurity.userapplication.controller;

import io.github.dronesecurity.lib.DateHelper;
import io.github.dronesecurity.userapplication.application.user.ohs.pl.*;
import io.github.dronesecurity.userapplication.domain.reporting.issue.entities.*;
import io.github.dronesecurity.userapplication.domain.reporting.issue.serialization.IssueStringHelper;
import io.github.dronesecurity.userapplication.domain.reporting.issue.services.MaintainerIssueReportService;
import io.github.dronesecurity.userapplication.utilities.CastHelper;
import io.github.dronesecurity.userapplication.utilities.DialogUtils;
import io.github.dronesecurity.userapplication.utilities.FXHelper;
import io.github.dronesecurity.userapplication.utilities.user.UserAPIHelper;
import io.vertx.ext.web.codec.BodyCodec;
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
import org.apache.commons.text.CaseUtils;
import org.jetbrains.annotations.NotNull;

import java.net.URL;
import java.util.Comparator;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.function.Consumer;
import java.util.stream.Collectors;

/**
 * The controller of the issue creation service.
 */
public class IssuesController implements Initializable {

    private static final String ERROR_MESSAGE = "Error connecting to issue information. Please retry.";
    private static final double MIN_WIDTH = 450;
    private static final double MIN_HEIGHT = 300;
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
    @FXML private Label selectedIssueDroneId;
    @FXML private Button visionIssueButton;
    @FXML private Button goToClosingPageButton;

    // Open issues list visualization nodes
    @FXML private TableView<CreatedIssue> openIssuesTable;
    @FXML private TableColumn<CreatedIssue, String> openIssuesId;
    @FXML private TableColumn<CreatedIssue, String> openIssuesSubject;
    @FXML private TableColumn<CreatedIssue, String> openIssuesCourier;
    @FXML private TableColumn<CreatedIssue, String> openIssuesDroneId;
    @FXML private TableView<ClosedIssue> closedIssuesTable;
    @FXML private TableColumn<ClosedIssue, String> closedIssuesId;
    @FXML private TableColumn<ClosedIssue, String> closedIssuesSubject;
    @FXML private TableColumn<ClosedIssue, String> closedIssuesCourier;
    @FXML private TableColumn<ClosedIssue, String> closedIssuesDroneId;

    // Closing issue panel
    @FXML private AnchorPane closingIssuePane;
    @FXML private TextArea solutionTextArea;

    @FXML private Button newIssueButton;
    @FXML private Label solutionLabel;
    @FXML private Text closedIssueSolution;

    private final MaintainerIssueReportService issueReportService;

    private CreatedIssue currentlySelectedIssue;
    private GenericUser loggedGenericUser;

    /**
     * Instantiates the issue report controller with its service.
     */
    public IssuesController() {
        this.issueReportService = MaintainerIssueReportService.getInstance();
        UserAPIHelper.get(UserAPIHelper.Operation.CHECK_LOGGED_USER_ROLE, BodyCodec.string()).onSuccess(res -> {
            switch (UserRole.valueOf(res.body())) {
                case COURIER:
                    UserAPIHelper.get(UserAPIHelper.Operation.RETRIEVE_LOGGED_COURIER_IF_PRESENT,
                                    BodyCodec.json(GenericUser.class))
                            .onSuccess(response -> this.loggedGenericUser = response.body());
                    break;
                case MAINTAINER:
                    UserAPIHelper.get(UserAPIHelper.Operation.RETRIEVE_LOGGED_MAINTAINER_IF_PRESENT,
                                    BodyCodec.json(GenericUser.class))
                            .onSuccess(response -> this.loggedGenericUser = response.body());
                    break;
                case NOT_LOGGED:
                default:
                    // TODO
            }
            if (this.loggedGenericUser.getRole() == UserRole.MAINTAINER)
                this.issueReportService.subscribeToNewIssue(this.loggedGenericUser.getUsername(), issue ->
                        Platform.runLater(() -> {
                            this.refreshOpenIssues();
                            DialogUtils.showInfoNotification("You have received a new issue!",
                                    this.issuesPane.getScene().getWindow());
                        }));
        });
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void initialize(final URL location, final ResourceBundle resources) {
        this.newIssueButton.setVisible(this.loggedGenericUser.getRole() == UserRole.COURIER);

        this.initTable(this.openIssuesTable, this.openIssuesId, this.openIssuesSubject, this.openIssuesCourier,
                this.openIssuesDroneId, issue -> {
            this.currentlySelectedIssue = issue;
            this.solutionLabel.setVisible(false);
            this.closedIssueSolution.setVisible(false);
            this.closedIssueSolution.setText("");
            this.visionIssueButton.setVisible(this.loggedGenericUser.getRole() == UserRole.MAINTAINER
                    && IssueStringHelper.STATUS_OPEN.equals(issue.getState()));
            this.goToClosingPageButton.setVisible(this.loggedGenericUser.getRole() == UserRole.MAINTAINER
                    && IssueStringHelper.STATUS_VISIONED.equals(issue.getState()));
            this.newIssueButton.setVisible(false);

            this.fillIssueFields();
        });

        this.initTable(this.closedIssuesTable, this.closedIssuesId, this.closedIssuesSubject, this.closedIssuesCourier,
                this.closedIssuesDroneId, issue -> {
            this.currentlySelectedIssue = issue;
            this.solutionLabel.setVisible(true);
            this.closedIssueSolution.setVisible(true);
            this.closedIssueSolution.setText(issue.getIssueSolution());
            this.visionIssueButton.setVisible(false);
            this.goToClosingPageButton.setVisible(false);
            this.newIssueButton.setVisible(false);
            this.fillIssueFields();
        });

        if (this.loggedGenericUser.getRole() == UserRole.COURIER) {
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
        FXHelper.initializeWindow(Modality.WINDOW_MODAL, "Create new Issue", loader, MIN_WIDTH, MIN_HEIGHT)
                .ifPresent(stage -> {
                    stage.initOwner(this.issuesPane.getScene().getWindow());
                    stage.setOnHidden(ignored -> this.refreshOpenIssues());
                    stage.showAndWait();
                });
    }

    @FXML
    private void visionIssue() {
        CastHelper.safeCast(this.currentlySelectedIssue, OpenIssue.class).ifPresent(openIssue ->
                this.issueReportService.visionIssue(openIssue.visionIssue()).onComplete(res -> {
                    if (Boolean.TRUE.equals(res.result())) {
                        Platform.runLater(() -> {
                            this.refreshOpenIssues();
                            final TableView.TableViewSelectionModel<CreatedIssue> selectionModel =
                                    this.openIssuesTable.getSelectionModel();

                            selectionModel.clearSelection();
                            final Optional<CreatedIssue> a =
                                    this.openIssuesTable.getItems()
                                            .filtered(c -> c.getId() == this.currentlySelectedIssue.getId())
                                            .stream()
                                            .findFirst();
                            a.ifPresent(selectionModel::select);
                        });
                    } else
                        DialogUtils.showErrorDialog(ERROR_MESSAGE);
                }));
    }

    @FXML
    private void closeIssue() {
        CastHelper.safeCast(this.currentlySelectedIssue, VisionedIssue.class).ifPresent(visionedIssue ->
                this.issueReportService.closeIssue(visionedIssue.closeIssue(this.solutionTextArea.getText()))
                        .onSuccess(succeeded -> {
                            if (Boolean.TRUE.equals(succeeded)) {
                                Platform.runLater(() -> {
                                    this.refreshOpenIssues();
                                    this.issueReportService.getClosedIssueReports().onSuccess(issues ->
                                        Platform.runLater(() -> {
                                            this.closedIssuesTable.setItems(FXCollections.observableList(issues.stream()
                                                    .sorted(Comparator.comparingInt(CreatedIssue::getId))
                                                    .collect(Collectors.toList())));
                                            final Optional<ClosedIssue> selectedIssue = this.closedIssuesTable
                                                    .getItems()
                                                    .filtered(c -> c.getId() == this.currentlySelectedIssue.getId())
                                                    .stream().findFirst();
                                            selectedIssue.ifPresent(issue ->
                                                    this.closedIssuesTable.getSelectionModel().select(issue));
                                        }));
                                });

                                this.openIssuesTable.getSelectionModel().clearSelection();

                                this.cancelClosing();
                            } else
                                DialogUtils.showErrorDialog(ERROR_MESSAGE);
                        }));
    }

    @FXML
    private void goBackFromOpenIssues() {
        this.selectedIssuePane.setVisible(false);
        this.openIssuesTable.getSelectionModel().clearSelection();
        this.closedIssuesTable.getSelectionModel().clearSelection();
        this.issuesPane.setVisible(true);
        this.newIssueButton.setVisible(this.loggedGenericUser.getRole() == UserRole.COURIER);
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

    private <T extends CreatedIssue> void initTable(final @NotNull TableView<T> table,
                           final @NotNull TableColumn<T, String> idColumn,
                           final @NotNull TableColumn<T, String> subjectColumn,
                           final @NotNull TableColumn<T, String> courierColumn,
                           final @NotNull TableColumn<T, String> droneIdColumn,
                           final Consumer<T> consumer) {
        idColumn.setCellValueFactory(val -> new SimpleStringProperty("#" + val.getValue().getId()));
        subjectColumn.setCellValueFactory(val -> new SimpleStringProperty(val.getValue().getSubject()));
        courierColumn.setCellValueFactory(val -> new SimpleStringProperty(val.getValue().getCourier()));
        droneIdColumn.setCellValueFactory(val -> new SimpleStringProperty(val.getValue().getDroneId()));

        final TableView.TableViewSelectionModel<T> selectionModel = table.getSelectionModel();
        selectionModel.setSelectionMode(SelectionMode.SINGLE);
        selectionModel.selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null)
                consumer.accept(newValue);
        });
    }

    private void fillIssueFields() {
        this.selectedIssueSubject.setText(this.currentlySelectedIssue.getSubject());
        this.issueState.setText(CaseUtils.toCamelCase(this.currentlySelectedIssue.getState(), true));
        final String creationInstant = DateHelper.toString(this.currentlySelectedIssue.getReportingDate());
        final int splitIndex = creationInstant.indexOf(':') - 2;
        this.selectedIssueCreationDate.setText(creationInstant.substring(0, splitIndex));
        this.selectedIssueCreationTime.setText(creationInstant.substring(splitIndex));
        this.selectedIssueCourier.setText(this.currentlySelectedIssue.getCourier());
        this.selectedIssueDetails.setText(this.currentlySelectedIssue.getDetails());
        this.selectedIssueDroneId.setText(this.currentlySelectedIssue.getDroneId());

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
