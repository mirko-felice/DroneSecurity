package it.unibo.dronesecurity.userapplication.controller;

import it.unibo.dronesecurity.lib.AlertUtils;
import it.unibo.dronesecurity.userapplication.issue.courier.IssueReportService;
import it.unibo.dronesecurity.userapplication.issue.courier.issues.ClosedIssue;
import it.unibo.dronesecurity.userapplication.issue.courier.issues.CreatedIssue;
import it.unibo.dronesecurity.userapplication.issue.courier.issues.OpenIssue;
import it.unibo.dronesecurity.userapplication.issue.courier.serialization.IssueStringHelper;
import it.unibo.dronesecurity.userapplication.utilities.CastHelper;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
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
    @FXML private Button closeIssueButton;

    // Open issues list visualization nodes
    @FXML private TableView<CreatedIssue> openIssuesTable;
    @FXML private TableColumn<CreatedIssue, String> openIssuesId;
    @FXML private TableColumn<CreatedIssue, String> openIssuesSubject;
    @FXML private TableColumn<CreatedIssue, String> openIssuesCourier;
    @FXML private TableView<CreatedIssue> closedIssuesTable;
    @FXML private TableColumn<CreatedIssue, String> closedIssuesId;
    @FXML private TableColumn<CreatedIssue, String> closedIssuesSubject;
    @FXML private TableColumn<CreatedIssue, String> closedIssuesCourier;

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

        this.initOpenIssuesTable();

        this.initClosedIssuesTable();

        this.issueReportService.getOpenIssueReports().onComplete(res -> {
            if (res.succeeded()) {
                res.result().forEach(el -> this.openIssues.put(el.getId(), el));
                final List<Integer> ids = new ArrayList<>(List.copyOf(this.openIssues.keySet()));
                ids.sort(Integer::compareTo);
                final List<CreatedIssue> testIds = ids.stream().map(this.openIssues::get).collect(Collectors.toList());
                this.openIssuesTable.setItems(FXCollections.observableList(testIds));
            }
        });

        this.issueReportService.getClosedIssueReports().onComplete(res -> {
            if (res.succeeded()) {
                res.result().forEach(el -> this.closedIssues.put(el.getId(), el));
                final List<Integer> ids = new ArrayList<>(List.copyOf(this.closedIssues.keySet()));
                ids.sort(Integer::compareTo);
                final List<CreatedIssue> testIds = ids.stream().map(this.closedIssues::get)
                        .collect(Collectors.toList());
                this.closedIssuesTable.setItems(FXCollections.observableList(testIds));
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
                        this.openIssues.put(openIssue.getId(), this.currentlySelectedIssue);

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
        this.clearSelection();
        this.issuesPane.setVisible(true);
    }

    private void clearSelection() {
        this.openIssuesTable.getSelectionModel().clearSelection();
        this.closedIssuesTable.getSelectionModel().clearSelection();
    }

    private void initOpenIssuesTable() {
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
                this.closeIssueButton.setVisible(IssueStringHelper.STATUS_VISIONED.equals(issue.getState()));

                this.fillIssueFields();
            }
        });
    }

    private void initClosedIssuesTable() {
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
                this.closeIssueButton.setVisible(false);

                this.fillIssueFields();
            }
        });
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
}
