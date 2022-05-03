package it.unibo.dronesecurity.userapplication.controller;

import it.unibo.dronesecurity.userapplication.auth.entities.Maintainer;
import it.unibo.dronesecurity.userapplication.events.DomainEvents;
import it.unibo.dronesecurity.userapplication.events.NewNegligence;
import it.unibo.dronesecurity.userapplication.negligence.entities.NegligenceActionFormImpl;
import it.unibo.dronesecurity.userapplication.negligence.entities.NegligenceReport;
import it.unibo.dronesecurity.userapplication.negligence.services.MaintainerNegligenceReportService;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import org.jetbrains.annotations.NotNull;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * Controller dedicated to control main window of a {@link Maintainer}.
 */
public class MaintainerController implements Initializable {

    private static final String NEGLIGENCE_REPORTS_FILENAME = "negligenceReports.fxml";
    private static final DomainEvents<NewNegligence> NEGLIGENCE_DOMAIN_EVENTS = new DomainEvents<>();
    private final MaintainerNegligenceReportService negligenceReportService;
    @FXML private GridPane pane;
    @FXML private TextArea solution;
    @FXML private Button takeActionButton;

    /**
     * Build the controller.
     */
    public MaintainerController() {
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
            final URL fileUrl = MaintainerController.class.getResource(NEGLIGENCE_REPORTS_FILENAME);
            final FXMLLoader fxmlLoader = new FXMLLoader(fileUrl);
            this.pane.add(fxmlLoader.load(),  0, 0, 2, 1);
            ((NegligenceReportsController) fxmlLoader.getController()).setOnClosedTabSelectionChanged(isSelected -> {
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
        final TableView<?> reportsTable = (TableView<?>) this.pane.lookup("#openReportsTable");
        final NegligenceReport report = (NegligenceReport) reportsTable.getSelectionModel().getSelectedItem();
        this.negligenceReportService.takeAction(new NegligenceActionFormImpl(report, this.solution.getText()));
    }
}
