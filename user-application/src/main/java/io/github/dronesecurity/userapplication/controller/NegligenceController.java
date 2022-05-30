/*
 * Copyright (c) 2021-2022, Mirko Felice & Maxim Derevyanchenko. All rights reserved.
 * Licensed under the MIT license. See LICENSE file in the project root for details.
 */

package io.github.dronesecurity.userapplication.controller;

import io.github.dronesecurity.userapplication.utilities.AlertUtils;
import io.github.dronesecurity.userapplication.auth.entities.Maintainer;
import io.github.dronesecurity.userapplication.events.NewNegligence;
import io.github.dronesecurity.userapplication.negligence.entities.NegligenceActionFormImpl;
import io.github.dronesecurity.userapplication.negligence.entities.OpenNegligenceReport;
import io.github.dronesecurity.userapplication.negligence.services.MaintainerNegligenceReportService;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
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
        this.negligenceReportService.subscribeToNegligenceReports(this::onNewNegligence);
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
                        Notifications.create()
                                .position(Pos.CENTER)
                                .owner(this.pane.getScene().getWindow())
                                .title("INFO")
                                .text("You have taken action against Courier with username: " + report.getNegligent())
                                .showInformation();
                        this.solution.clear();
                    }));
        }
    }
}
