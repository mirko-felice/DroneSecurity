/*
 * Copyright (c) 2021-2022, Mirko Felice & Maxim Derevyanchenko. All rights reserved.
 * Licensed under the MIT license. See LICENSE file in the project root for details.
 */

package io.github.dronesecurity.userapplication.controller;

import io.github.dronesecurity.userapplication.utilities.AlertUtils;
import io.github.dronesecurity.userapplication.issue.courier.IssueReportService;
import io.github.dronesecurity.userapplication.issue.courier.issues.ClosedIssue;
import io.github.dronesecurity.userapplication.issue.courier.issues.CreatedIssue;
import io.github.dronesecurity.userapplication.issue.courier.issues.OpenIssue;
import io.github.dronesecurity.userapplication.issue.courier.issues.VisionedIssue;
import io.github.dronesecurity.userapplication.issue.courier.serialization.IssueStringHelper;
import io.github.dronesecurity.userapplication.utilities.CastHelper;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;

import java.net.URL;
import java.util.*;
import java.util.stream.Collectors;

//TODO add issue after creating a new one

/**
 * The controller of the issue creation service.
 */
public final class MaintainerIssueController implements Initializable {

    @FXML private TabPane issuesPane;

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

    private final IssueReportService issueReportService;
    private final Map<Integer, CreatedIssue> openIssues;
    private final Map<Integer, ClosedIssue> closedIssues;

    private CreatedIssue currentlySelectedIssue;

    /**
     * Instantiates the issue report controller with its service.
     */
    public MaintainerIssueController() {
        this.issueReportService = new IssueReportService();

        this.openIssues = new HashMap<>();
        this.closedIssues = new HashMap<>();
    }

    @Override
    public void initialize(final URL location, final ResourceBundle resources) {

        //Initialize open issues table
        final TableView.TableViewSelectionModel<CreatedIssue> openIssuesSelectionModel =
                this.openIssuesTable.getSelectionModel();
        openIssuesSelectionModel.setSelectionMode(SelectionMode.SINGLE);
        this.openIssuesId.setCellValueFactory(val -> new SimpleStringProperty("#" + val.getValue().getId()));
        this.openIssuesSubject.setCellValueFactory(val -> new SimpleStringProperty(val.getValue().getSubject()));
        this.openIssuesCourier.setCellValueFactory(val -> new SimpleStringProperty(val.getValue().getCourier()));

        openIssuesSelectionModel.selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                final CreatedIssue issue = this.openIssues.get(newValue.getId());
                this.currentlySelectedIssue = issue;
                this.visionIssueButton.setVisible(IssueStringHelper.STATUS_OPEN.equals(issue.getState()));
                this.goToClosingPageButton.setVisible(IssueStringHelper.STATUS_VISIONED.equals(issue.getState()));

                this.fillIssueFields();
            }
        });

        // Initialize closed issues table
        final MultipleSelectionModel<CreatedIssue> closedIssuesSelectionModel =
                this.closedIssuesTable.getSelectionModel();
        closedIssuesSelectionModel.setSelectionMode(SelectionMode.SINGLE);
        this.closedIssuesId.setCellValueFactory(val -> new SimpleStringProperty("#" + val.getValue().getId()));
        this.closedIssuesSubject.setCellValueFactory(val -> new SimpleStringProperty(val.getValue().getSubject()));
        this.closedIssuesCourier.setCellValueFactory(val -> new SimpleStringProperty(val.getValue().getCourier()));

        closedIssuesSelectionModel.selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                this.currentlySelectedIssue = this.closedIssues.get(newValue.getId());
//                final ClosedIssue issue = this.closedIssues.get(newValue.getId());
//                this.currentlySelectedIssue = issue;
                this.visionIssueButton.setVisible(false);
                this.goToClosingPageButton.setVisible(false);

                this.fillIssueFields();
            }
        });

        this.issueReportService.getOpenIssueReports().onComplete(res -> {
            if (res.succeeded()) {
                res.result().forEach(el -> this.openIssues.put(el.getId(), el));
                this.populateOpenIssuesTable();
            }
        });

        this.issueReportService.getClosedIssueReports().onComplete(res -> {
            if (res.succeeded()) {
                res.result().forEach(el -> this.closedIssues.put(el.getId(), el));
                this.populateCloseIssuesTable();
            }
        });
    }

    @FXML
    private void visionIssue() {
        final Optional<OpenIssue> currentOpenIssue = CastHelper.safeCast(this.currentlySelectedIssue, OpenIssue.class);
        currentOpenIssue.ifPresent(openIssue ->
                this.issueReportService.visionIssue(openIssue).onComplete(res -> {
                    if (res.result()) {
                        this.currentlySelectedIssue = openIssue.visionIssue();
                        this.openIssues.replace(this.currentlySelectedIssue.getId(), this.currentlySelectedIssue);

                        Platform.runLater(() -> {
                            final TableView.TableViewSelectionModel<CreatedIssue> selectionModel =
                                    this.openIssuesTable.getSelectionModel();

                            selectionModel.clearSelection();
                            selectionModel.select(this.currentlySelectedIssue);
                        });
                    } else
                        AlertUtils.showErrorAlert("Error connecting to issue information. Please retry.");
                }));
    }

    @FXML
    private void goBackFromOpenIssues() {
        this.selectedIssuePane.setVisible(false);
        this.openIssuesTable.getSelectionModel().clearSelection();
        this.closedIssuesTable.getSelectionModel().clearSelection();
        this.issuesPane.setVisible(true);
    }

    @FXML
    private void goToClosingPage() {
        this.selectedIssuePane.setVisible(false);
        this.closingIssuePane.setVisible(true);
    }

    @FXML
    private void closeIssue() {
        final Optional<VisionedIssue> currentVisionedIssue =
                CastHelper.safeCast(this.currentlySelectedIssue, VisionedIssue.class);
        currentVisionedIssue.ifPresent(visionedIssue ->
                this.issueReportService.closeIssue(visionedIssue, this.solutionTextArea.getText()).onComplete(res -> {
                    if (res.result()) {
                        final ClosedIssue closedIssue = visionedIssue.closeIssue(this.solutionTextArea.getText());
                        this.openIssues.remove(closedIssue.getId());
                        this.closedIssues.put(closedIssue.getId(), closedIssue);
                        this.populateOpenIssuesTable();
                        this.populateCloseIssuesTable();

                        this.currentlySelectedIssue = closedIssue;

                        Platform.runLater(() -> {
                            this.openIssuesTable.getSelectionModel().clearSelection();
                            this.closedIssuesTable.getSelectionModel().select(this.currentlySelectedIssue);
                            this.cancelClosing();
                        });
                    }
                }));
    }

    @FXML
    private void cancelClosing() {
        this.solutionTextArea.clear();
        this.closingIssuePane.setVisible(false);
        this.selectedIssuePane.setVisible(true);
    }

    private void fillIssueFields() {
        this.selectedIssueSubject.setText(this.currentlySelectedIssue.getSubject());
        this.issueState.setText(this.currentlySelectedIssue.getState().substring(0, 1).toUpperCase(Locale.ITALY)
                + this.currentlySelectedIssue.getState().substring(1));
        final String creationInstant = this.currentlySelectedIssue.getReportingDate().toString();
        final String[] instantComponents = creationInstant.split("T");
        this.selectedIssueCreationDate.setText(instantComponents[0]);
        this.selectedIssueCreationTime.setText(instantComponents[1]
                .replace("Z", ""));
        this.selectedIssueCourier.setText(this.currentlySelectedIssue.getCourier());
        this.selectedIssueDetails.setText(this.currentlySelectedIssue.getDetails());

        this.issuesPane.setVisible(false);
        this.selectedIssuePane.setVisible(true);
    }

    private void populateOpenIssuesTable() {
        final List<Integer> ids = new ArrayList<>(List.copyOf(this.openIssues.keySet()));
        ids.sort(Integer::compareTo);
        final List<CreatedIssue> testIds = ids.stream().map(this.openIssues::get).collect(Collectors.toList());
        this.openIssuesTable.setItems(FXCollections.observableList(testIds));
    }

    private void populateCloseIssuesTable() {
        final List<Integer> ids = new ArrayList<>(List.copyOf(this.closedIssues.keySet()));
        ids.sort(Integer::compareTo);
        final List<CreatedIssue> testIds = ids.stream().map(this.closedIssues::get)
                .collect(Collectors.toList());
        this.closedIssuesTable.setItems(FXCollections.observableList(testIds));
    }
}
