/*
 * Copyright (c) 2021-2022, Mirko Felice & Maxim Derevyanchenko. All rights reserved.
 * Licensed under the MIT license. See LICENSE file in the project root for details.
 */

package io.github.dronesecurity.userapplication.presentation.reporting.issue;

import io.github.dronesecurity.lib.utilities.*;
import io.github.dronesecurity.userapplication.application.reporting.issue.*;
import io.github.dronesecurity.userapplication.application.user.ohs.pl.*;
import io.github.dronesecurity.userapplication.domain.reporting.issue.activeissue.entities.AbstractActiveIssue;
import io.github.dronesecurity.userapplication.domain.reporting.issue.closedissue.entities.ClosedIssue;
import io.github.dronesecurity.userapplication.domain.reporting.issue.createdissue.entities.AbstractCreatedIssue;
import io.github.dronesecurity.userapplication.domain.reporting.issue.openissue.entities.OpenIssue;
import io.github.dronesecurity.userapplication.domain.reporting.issue.visionedissue.entities.VisionedIssue;
import io.github.dronesecurity.userapplication.infrastructure.reporting.issue.serializers.IssueStringHelper;
import io.github.dronesecurity.userapplication.utilities.*;
import io.github.dronesecurity.userapplication.utilities.user.UserAPIHelper;
import io.vertx.core.Future;
import io.vertx.ext.web.client.HttpResponse;
import io.vertx.ext.web.codec.BodyCodec;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import org.apache.commons.text.CaseUtils;
import org.jetbrains.annotations.NotNull;

import java.net.URL;
import java.util.Comparator;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

/**
 * The controller of the issue creation service.
 */
public final class IssuesUIController implements Initializable {

    private static final String ERROR_MESSAGE = "Error connecting to issue information. Please retry.";
    private static final double MIN_WIDTH = 450;
    private static final double MIN_HEIGHT = 300;
    private static final String NEW_ISSUE_FXML = "newIssue.fxml";
    @FXML private TabPane issuesPane;

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
    @FXML private TableView<AbstractActiveIssue> activeIssuesTable;
    @FXML private TableColumn<AbstractActiveIssue, String> activeIssuesId;
    @FXML private TableColumn<AbstractActiveIssue, String> activeIssuesSubject;
    @FXML private TableColumn<AbstractActiveIssue, String> activeIssuesCourier;
    @FXML private TableColumn<AbstractActiveIssue, String> activeIssuesDroneId;
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

    private IssueReportService issueReportService;

    private AbstractCreatedIssue currentlySelectedIssue;
    private GenericUser loggedGenericUser;

    /**
     * {@inheritDoc}
     */
    @Override
    public void initialize(final URL location, final ResourceBundle resources) {
        UserAPIHelper.get(UserAPIHelper.Operation.CHECK_LOGGED_USER_ROLE, BodyCodec.json(UserRole.class))
                .onSuccess(res -> this.initLoggedUser(res.body()));

        IssueHelper.initTable(this.activeIssuesTable, this.activeIssuesId, this.activeIssuesSubject,
                this.activeIssuesCourier, this.activeIssuesDroneId, issue -> {
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

        IssueHelper.initTable(this.closedIssuesTable, this.closedIssuesId, this.closedIssuesSubject,
                this.closedIssuesCourier, this.closedIssuesDroneId, issue -> {
            this.currentlySelectedIssue = issue;
            this.solutionLabel.setVisible(true);
            this.closedIssueSolution.setVisible(true);
            this.closedIssueSolution.setText(issue.getIssueSolution());
            this.visionIssueButton.setVisible(false);
            this.goToClosingPageButton.setVisible(false);
            this.newIssueButton.setVisible(false);
            this.fillIssueFields();
        });
    }

    @FXML
    private void newIssue() {
        final FXMLLoader loader = new FXMLLoader(IssuesUIController.class.getResource(NEW_ISSUE_FXML));
        FXHelper.initializeWindow(Modality.WINDOW_MODAL, "Create new Issue", loader, MIN_WIDTH, MIN_HEIGHT)
                .ifPresent(stage -> {
                    stage.initOwner(this.issuesPane.getScene().getWindow());
                    stage.setOnHidden(ignored -> this.refreshOpenIssues());
                    stage.showAndWait();
                });
    }

    @FXML
    private void visionIssue() {
        CastHelper.safeCast(this.currentlySelectedIssue, OpenIssue.class).ifPresent(openIssue -> {
            if (this.issueReportService.visionIssue(openIssue.visionIssue())) {
                this.refreshOpenIssues();
                Platform.runLater(() -> {
                    final TableView.TableViewSelectionModel<AbstractActiveIssue> selectionModel =
                            this.activeIssuesTable.getSelectionModel();

                    selectionModel.clearSelection();
                    this.activeIssuesTable.getItems()
                            .filtered(c -> c.getId() == this.currentlySelectedIssue.getId())
                            .stream()
                            .findFirst()
                            .ifPresent(selectionModel::select);
                });
            } else
                DialogUtils.showErrorDialog(ERROR_MESSAGE);
        });
    }

    @FXML
    private void closeIssue() {
        CastHelper.safeCast(this.currentlySelectedIssue, VisionedIssue.class).ifPresent(visionedIssue -> {
            if (this.issueReportService.closeIssue(visionedIssue.closeIssue(this.solutionTextArea.getText()))) {
                Platform.runLater(() -> {
                    this.refreshOpenIssues();
                    this.refreshClosedIssues();
                    this.closedIssuesTable.getItems()
                            .filtered(c -> c.getId().isSameValueAs(this.currentlySelectedIssue.getId()))
                            .stream().findFirst()
                            .ifPresent(issue -> this.closedIssuesTable.getSelectionModel().select(issue));
                });

                this.activeIssuesTable.getSelectionModel().clearSelection();
                this.cancelClosing();
            } else
                DialogUtils.showErrorDialog(ERROR_MESSAGE);
        });
    }

    @FXML
    private void goBackFromOpenIssues() {
        this.selectedIssuePane.setVisible(false);
        this.activeIssuesTable.getSelectionModel().clearSelection();
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

    private void initLoggedUser(final @NotNull UserRole userRole) {
        final Future<GenericUser> loggedUser;
        switch (userRole) {
            case COURIER:
                loggedUser = UserAPIHelper.get(UserAPIHelper.Operation.RETRIEVE_LOGGED_COURIER_IF_PRESENT,
                        BodyCodec.json(GenericUser.class)).map(HttpResponse::body);
                break;
            case MAINTAINER:
                loggedUser = UserAPIHelper.get(
                        UserAPIHelper.Operation.RETRIEVE_LOGGED_MAINTAINER_IF_PRESENT,
                        BodyCodec.json(GenericUser.class)).map(HttpResponse::body);
                break;
            default:
                loggedUser = Future.failedFuture(
                        new IllegalStateException("Unexpected value: " + userRole));
                break;
        }
        loggedUser.onSuccess(res -> {
            this.loggedGenericUser = res;
            this.newIssueButton.setVisible(this.loggedGenericUser.getRole() == UserRole.COURIER);

            if (this.loggedGenericUser.getRole() == UserRole.COURIER) {
                this.issueReportService = new CourierIssueReportService();
                this.activeIssuesTable.getColumns().remove(this.activeIssuesCourier);
                this.closedIssuesTable.getColumns().remove(this.closedIssuesCourier);
            } else {
                this.issueReportService = new MaintainerIssueReportService();
                this.issueReportService.subscribeToNewIssue(this.loggedGenericUser.getUsername(), issue -> {
                        this.refreshOpenIssues();
                        Platform.runLater(() -> DialogUtils.showInfoNotification("You have received a new issue!",
                                this.issuesPane.getScene().getWindow()));
                });
            }
            this.refreshOpenIssues();
            this.refreshClosedIssues();
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
        IssueHelper.refreshOpenIssues(this.activeIssuesTable, this.loggedGenericUser.getRole() == UserRole.COURIER
                ? this.issueReportService.getActiveIssueReportsForCourier(this.loggedGenericUser.getUsername())
                : this.issueReportService.getActiveIssueReportsForAssignee(this.loggedGenericUser.getUsername()));
    }

    private void refreshClosedIssues() {
        this.closedIssuesTable.setItems(FXCollections.observableList(
                (this.loggedGenericUser.getRole() == UserRole.COURIER
                        ? this.issueReportService.getClosedIssueReportsForCourier(this.loggedGenericUser.getUsername())
                        : this.issueReportService.getClosedIssueReportsForAssignee(this.loggedGenericUser.getUsername())
                )
                .stream()
                .sorted(Comparator.comparingLong(issue -> issue.getId().getId()))
                .collect(Collectors.toList())));
    }
}
