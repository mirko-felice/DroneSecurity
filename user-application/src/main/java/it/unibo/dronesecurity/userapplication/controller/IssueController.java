package it.unibo.dronesecurity.userapplication.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import it.unibo.dronesecurity.lib.Connection;
import it.unibo.dronesecurity.lib.MqttMessageParameterConstants;
import it.unibo.dronesecurity.lib.MqttTopicConstants;
import it.unibo.dronesecurity.userapplication.issue.courier.IssueReportService;
import it.unibo.dronesecurity.userapplication.issue.courier.issues.NotCreatedIssue;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;
import org.slf4j.LoggerFactory;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

/**
 * The controller of the issue creation service.
 */
public final class IssueController implements Initializable {

    @FXML private TextArea infoTextArea;
    @FXML private ListView<String> openIssuesListView;
    private final IssueReportService issueReportService;

    /**
     * Instantiates the controller with its service.
     */
    public IssueController() {
        this.issueReportService = new IssueReportService();
    }

    @Override
    public void initialize(final URL location, final ResourceBundle resources) {
        this.issueReportService.getIssueReports().onComplete(res -> {
            if (res.succeeded()) {
                final List<String> ids = res.result().stream().map(issue -> "#" + issue.getId())
                        .collect(Collectors.toList());
                this.openIssuesListView.setItems(FXCollections.observableList(ids));
            }
        });
    }

    @FXML
    private void sendIssue() {
        final String issueInfo = this.infoTextArea.getText();
        final NotCreatedIssue issue = new NotCreatedIssue(issueInfo);
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
}
