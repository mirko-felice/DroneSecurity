/*
 * Copyright (c) 2021-2022, Mirko Felice & Maxim Derevyanchenko. All rights reserved.
 * Licensed under the MIT license. See LICENSE file in the project root for details.
 */

package io.github.dronesecurity.userapplication.controller;

import io.github.dronesecurity.userapplication.auth.entities.Maintainer;
import io.github.dronesecurity.userapplication.events.DomainEvents;
import io.github.dronesecurity.userapplication.events.NewNegligence;
import io.github.dronesecurity.userapplication.reporting.negligence.entities.OpenNegligenceReport;
import io.github.dronesecurity.userapplication.reporting.negligence.services.MaintainerNegligenceReportService;
import io.github.dronesecurity.userapplication.reporting.negligence.services.NegligenceReportService;
import io.github.dronesecurity.userapplication.utilities.CastHelper;
import io.github.dronesecurity.userapplication.utilities.DialogUtils;
import io.github.dronesecurity.userapplication.utilities.UserHelper;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.layout.GridPane;
import org.jetbrains.annotations.NotNull;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.function.Consumer;

/**
 * Controller dedicated to control main window of a {@link Maintainer}.
 */
public class NegligenceController implements Initializable {

    private static final String NEGLIGENCE_DATA_FXML = "negligenceData.fxml";
    private final MaintainerNegligenceReportService negligenceReportService;
    private final Consumer<NewNegligence> newNegligenceHandler;
    @FXML private GridPane pane;
    @FXML private TextArea solution;
    @FXML private Button takeActionButton;
    private NegligenceDataController dataController;

    /**
     * Build the controller.
     */
    public NegligenceController() {
        this.negligenceReportService = new NegligenceReportService();
        this.newNegligenceHandler = this::onNewNegligence;
        DomainEvents.register(NewNegligence.class, this.newNegligenceHandler);
        CastHelper.safeCast(UserHelper.logged(), Maintainer.class).ifPresent(maintainer -> maintainer.getCouriers()
                .forEach(this.negligenceReportService::subscribeToCourierNegligence));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void initialize(final URL location, final ResourceBundle resources) {
        try {
            final URL fileUrl = NegligenceController.class.getResource(NEGLIGENCE_DATA_FXML);
            final FXMLLoader fxmlLoader = new FXMLLoader(fileUrl);
            this.pane.add(fxmlLoader.load(), 0, 0, 2, 1);
            this.dataController = fxmlLoader.getController();
            this.dataController.setOnClosedTabSelectionChanged(isSelected -> {
                this.pane.getScene().getWindow().setOnHidden(ignored ->
                        DomainEvents.unregister(NewNegligence.class, this.newNegligenceHandler));
                this.takeActionButton.setDisable(isSelected);
                this.solution.setDisable(isSelected);
            });
        } catch (IOException e) {
            LoggerFactory.getLogger(this.getClass()).warn("Can NOT load reports window.", e);
        }
    }

    private void onNewNegligence(final @NotNull NewNegligence newNegligence) {
        Platform.runLater(() -> DialogUtils.showInfoNotification("INFO",
                "New negligence committed by " + newNegligence.getReport().getNegligent()
                        + ". Please take care of this. Go to the 'reports' window to show more information about it.",
                this.pane.getScene().getWindow()));
    }

    @FXML
    private void takeAction() {
        final OpenNegligenceReport report = this.dataController.getSelectedOpenReport();
        if (report == null)
            DialogUtils.showErrorDialog("You have to select a report to take action on.");
        else {
            this.dataController.emptyDetails();
            this.negligenceReportService.takeAction(report, this.solution.getText())
                    .onSuccess(unused -> Platform.runLater(() -> {
                        this.dataController.updateReports();
                        DialogUtils.showInfoNotification("INFO",
                                "You have taken action against Courier with username: " + report.getNegligent(),
                                this.pane.getScene().getWindow());
                        this.solution.clear();
                    }));
        }
    }
}
