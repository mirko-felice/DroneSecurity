package it.unibo.dronesecurity.userapplication.controller;

import it.unibo.dronesecurity.userapplication.auth.entities.Maintainer;
import it.unibo.dronesecurity.userapplication.events.DomainEvents;
import it.unibo.dronesecurity.userapplication.events.NewNegligence;
import it.unibo.dronesecurity.userapplication.negligence.services.MaintainerNegligenceReportService;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import org.jetbrains.annotations.NotNull;

/**
 * Controller dedicated to control main window of a {@link Maintainer}.
 */
public class MaintainerController {

    private static final DomainEvents<NewNegligence> NEGLIGENCE_DOMAIN_EVENTS = new DomainEvents<>();
    private final MaintainerNegligenceReportService negligenceReportService;
    @FXML private TextField negligent;

    /**
     * Build the controller.
     */
    public MaintainerController() {
        this.negligenceReportService = MaintainerNegligenceReportService.getInstance();
        NEGLIGENCE_DOMAIN_EVENTS.register(this::onNewNegligence);
        this.negligenceReportService.subscribeToNegligenceReports(NEGLIGENCE_DOMAIN_EVENTS);
    }

    private void onNewNegligence(final @NotNull NewNegligence newNegligence) {
        this.negligent.setText(newNegligence.getReport().getNegligent().getUsername());
    }

    @FXML
    private void takeAction() {
        this.negligenceReportService.takeAction(null);
    }
}
