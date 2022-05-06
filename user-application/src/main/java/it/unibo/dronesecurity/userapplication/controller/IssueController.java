package it.unibo.dronesecurity.userapplication.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import it.unibo.dronesecurity.lib.Connection;
import it.unibo.dronesecurity.lib.MqttMessageParameterConstants;
import it.unibo.dronesecurity.lib.MqttTopicConstants;
import it.unibo.dronesecurity.userapplication.issue.courier.IssueReportService;
import it.unibo.dronesecurity.userapplication.issue.courier.issues.BaseIssue;
import it.unibo.dronesecurity.userapplication.issue.courier.issues.OpenIssue;
import it.unibo.dronesecurity.userapplication.utilities.UserHelper;
import javafx.application.Platform;
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

/**
 * The controller of the issue creation service.
 */
public final class IssueController implements Initializable {

    @FXML private TabPane issuesPane;

    // Issue Details visualization nodes
    @FXML private GridPane selectedIssuePane;
    @FXML private Label selectedIssueCreationDate;
    @FXML private Label selectedIssueCreationTime;
    @FXML private Label selectedIssueSubject;
    @FXML private Text selectedIssueDetails;

    // New Issue creation nodes
    @FXML private TextField issueSubject;
    @FXML private TextArea infoTextArea;

    // Open issues list visualization nodes
    @FXML private ListView<String> openIssuesListView;

    private final IssueReportService issueReportService;
    private final Map<String, OpenIssue> issues;

    /**
     * Instantiates the issue report controller with its service.
     */
    public IssueController() {
        this.issueReportService = new IssueReportService();

        this.issues = new HashMap<>();
    }

    @Override
    public void initialize(final URL location, final ResourceBundle resources) {
        final MultipleSelectionModel<String> openIssuesSelectionModel = this.openIssuesListView.getSelectionModel();
        openIssuesSelectionModel.selectedItemProperty()
                .addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                final OpenIssue issue = this.issues.get(newValue);

                final String creationInstant = issue.getReportingDate().toString();
                final String[] instantComponents = creationInstant.split("T");
                this.selectedIssueCreationDate.setText(instantComponents[0]);
                this.selectedIssueCreationTime.setText(instantComponents[1]
                        .replace("Z", ""));
                this.selectedIssueSubject.setText(issue.getSubject());
                this.selectedIssueDetails.setText(issue.getDetails());

                this.issuesPane.setVisible(false);
                this.selectedIssuePane.setVisible(true);
            }
        });

        this.issueReportService.getIssueReports().onComplete(res -> {
            if (res.succeeded()) {
                res.result().forEach(el -> this.issues.put("#" + el.getId(), el));
                final List<String> ids = new ArrayList<>(List.copyOf(this.issues.keySet()));
                ids.sort(String::compareTo);
                this.openIssuesListView.setItems(FXCollections.observableList(ids));
            }
        });
        openIssuesSelectionModel.setSelectionMode(SelectionMode.SINGLE);
    }

    @FXML
    private void sendIssue() {
        final String issueInfo = this.infoTextArea.getText();
        final String subjectText = this.issueSubject.getText();
        final BaseIssue issue = new BaseIssue(subjectText, issueInfo, UserHelper.get().getUsername(), Instant.now());
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
        this.openIssuesListView.getSelectionModel().clearSelection();
        this.issuesPane.setVisible(true);
    }
}
