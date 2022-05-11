package it.unibo.dronesecurity.userapplication.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import it.unibo.dronesecurity.lib.Connection;
import it.unibo.dronesecurity.lib.MqttMessageParameterConstants;
import it.unibo.dronesecurity.lib.MqttTopicConstants;
import it.unibo.dronesecurity.userapplication.issue.courier.IssueReportService;
import it.unibo.dronesecurity.userapplication.issue.courier.issues.ClosedIssue;
import it.unibo.dronesecurity.userapplication.issue.courier.issues.CreatedIssue;
import it.unibo.dronesecurity.userapplication.issue.courier.issues.Issue;
import it.unibo.dronesecurity.userapplication.issue.courier.issues.SendingIssue;
import it.unibo.dronesecurity.userapplication.utilities.UserHelper;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import org.slf4j.LoggerFactory;

import java.net.URL;
import java.time.Instant;
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

    // New Issue creation nodes
    @FXML private TextField issueSubject;
    @FXML private TextArea infoTextArea;

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
    private void sendIssue() {
        final String issueInfo = this.infoTextArea.getText();
        final String subjectText = this.issueSubject.getText();
        final SendingIssue issue =
                new SendingIssue(subjectText,
                        issueInfo,
                        UserHelper.get().getUsername(),
                        Instant.now());
        Platform.runLater(() -> this.issueReportService.addIssueReport(issue));
        final JsonNode json = new ObjectMapper().createObjectNode()
                        .put(MqttMessageParameterConstants.ISSUE_REPORT_INFO_PARAMETER, issueInfo);
        Connection.getInstance().publish(MqttTopicConstants.ISSUE_TOPIC, json);
        try {
            ((Stage) this.infoTextArea.getScene().getWindow()).close();
        } catch (ClassCastException e) {
            LoggerFactory.getLogger(getClass()).error("Error closing the new window:", e);
        }
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

                this.fillIssueFields(issue);
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
                final ClosedIssue issue = this.closedIssues.get(newValue.getId());

                this.fillIssueFields(issue);
            }
        });
    }

    private void fillIssueFields(final Issue issue) {
        this.selectedIssueSubject.setText(issue.getSubject());
        this.issueState.setText(issue.getState().substring(0, 1).toUpperCase(Locale.ITALY)
                + issue.getState().substring(1));
        final String creationInstant = issue.getReportingDate().toString();
        final String[] instantComponents = creationInstant.split("T");
        this.selectedIssueCreationDate.setText(instantComponents[0]);
        this.selectedIssueCreationTime.setText(instantComponents[1]
                .replace("Z", ""));
        this.selectedIssueCourier.setText(issue.getCourier());
        this.selectedIssueDetails.setText(issue.getDetails());

        this.issuesPane.setVisible(false);
        this.selectedIssuePane.setVisible(true);
    }
}
