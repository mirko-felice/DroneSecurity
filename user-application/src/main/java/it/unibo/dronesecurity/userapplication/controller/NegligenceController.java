package it.unibo.dronesecurity.userapplication.controller;

import it.unibo.dronesecurity.lib.AlertUtils;
import it.unibo.dronesecurity.userapplication.auth.entities.Maintainer;
import it.unibo.dronesecurity.userapplication.events.DomainEvents;
import it.unibo.dronesecurity.userapplication.events.NewNegligence;
import it.unibo.dronesecurity.userapplication.negligence.entities.NegligenceActionFormImpl;
import it.unibo.dronesecurity.userapplication.negligence.entities.OpenNegligenceReport;
import it.unibo.dronesecurity.userapplication.negligence.services.MaintainerNegligenceReportService;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.layout.GridPane;
import org.controlsfx.control.Notifications;
import org.jetbrains.annotations.NotNull;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * Controller dedicated to control main window of a {@link Maintainer}.
 */
public class NegligenceController implements Initializable {

    private static final String NEGLIGENCE_DATA_FILENAME = "negligenceData.fxml";
    private static final DomainEvents<NewNegligence> NEGLIGENCE_DOMAIN_EVENTS = new DomainEvents<>();
    private final MaintainerNegligenceReportService negligenceReportService;
    @FXML private GridPane pane;
    @FXML private TextArea solution;
    @FXML private Button takeActionButton;
    private NegligenceDataController dataController;

    /**
     * Build the controller.
     */
    public NegligenceController() {
        this.negligenceReportService = MaintainerNegligenceReportService.getInstance();
        NEGLIGENCE_DOMAIN_EVENTS.register(this::onNewNegligence);
        this.negligenceReportService.subscribeToNegligenceReports(NEGLIGENCE_DOMAIN_EVENTS);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void initialize(final URL location, final ResourceBundle resources) {
        try {
            final URL fileUrl = NegligenceController.class.getResource(NEGLIGENCE_DATA_FILENAME);
            final FXMLLoader fxmlLoader = new FXMLLoader(fileUrl);
            this.pane.add(fxmlLoader.load(), 0, 0, 2, 1);
            this.dataController = fxmlLoader.getController();
            this.dataController.setOnClosedTabSelectionChanged(isSelected -> {
                this.takeActionButton.setDisable(isSelected);
                this.solution.setDisable(isSelected);
            });
        } catch (IOException e) {
            LoggerFactory.getLogger(this.getClass()).warn("Can NOT load reports window.", e);
        }
    }

    private void onNewNegligence(final @NotNull NewNegligence newNegligence) {
        LoggerFactory.getLogger(this.getClass()).debug("{}", newNegligence);
    }

    @FXML
    private void takeAction() {
        final OpenNegligenceReport report = this.dataController.getSelectedOpenReport();
        if (report == null)
            AlertUtils.showErrorAlert("You have to select a report to take action on.");
        else {
            this.dataController.emptyDetails();
            this.negligenceReportService.takeAction(new NegligenceActionFormImpl(report, this.solution.getText()))
                    .onSuccess(unused -> Platform.runLater(() -> {
                        this.dataController.updateReports();
                        Notifications.create().title(report.assignedTo()).showInformation();
                        this.solution.clear();
                    }));
        }
    }
}
