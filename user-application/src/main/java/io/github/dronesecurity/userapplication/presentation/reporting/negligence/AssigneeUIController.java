/*
 * Copyright (c) 2021-2022, Mirko Felice & Maxim Derevyanchenko. All rights reserved.
 * Licensed under the MIT license. See LICENSE file in the project root for details.
 */

package io.github.dronesecurity.userapplication.presentation.reporting.negligence;

import io.github.dronesecurity.userapplication.domain.reporting.negligence.entities.contracts.OpenNegligenceReport;
import io.github.dronesecurity.userapplication.domain.reporting.negligence.events.NewNegligence;
import io.github.dronesecurity.userapplication.domain.reporting.negligence.objects.Assignee;
import io.github.dronesecurity.userapplication.domain.reporting.negligence.objects.NegligenceActionForm;
import io.github.dronesecurity.userapplication.events.DomainEvents;
import io.github.dronesecurity.userapplication.utilities.DialogUtils;
import io.github.dronesecurity.userapplication.utilities.reporting.negligence.AssigneeAPIHelper;
import io.vertx.core.json.JsonObject;
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
 * Controller dedicated to control main window of an {@link Assignee}.
 */
public final class AssigneeUIController implements Initializable {

    private static final String NEGLIGENCE_DATA_FXML = "negligenceData.fxml";
    private final Consumer<NewNegligence> newNegligenceHandler;
    @FXML private GridPane pane;
    @FXML private TextArea solution;
    @FXML private Button takeActionButton;
    private ReportsUIController dataController;

    /**
     * Build the controller.
     */
    public AssigneeUIController() {
        this.newNegligenceHandler = this::onNewNegligence;
        DomainEvents.register(NewNegligence.class, this.newNegligenceHandler);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void initialize(final URL location, final ResourceBundle resources) {
        try {
            final URL fileUrl = AssigneeUIController.class.getResource(NEGLIGENCE_DATA_FXML);
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
        Platform.runLater(() -> {
            this.dataController.updateReports(newNegligence.getReport().assignedTo());
            DialogUtils.showInfoNotification(
                    "New negligence committed by " + newNegligence.getReport().getNegligent()
                            + ". Please take care of this. Go to the 'reports' window to show more information"
                            + " about it.",
                    this.pane.getScene().getWindow());
        });
    }

    @FXML
    private void takeAction() {
        final OpenNegligenceReport report = this.dataController.getSelectedOpenReport();
        if (report == null)
            DialogUtils.showErrorDialog("You have to select a report to take action on.");
        else {
            this.dataController.emptyDetails();
            final JsonObject body = new JsonObject();
            body.put(AssigneeAPIHelper.REPORT_KEY, report);
            body.put(AssigneeAPIHelper.ACTION_FORM_KEY, NegligenceActionForm.create(this.solution.getText()));
            AssigneeAPIHelper.postJson(AssigneeAPIHelper.Operation.TAKE_ACTION, body).onSuccess(unused ->
                    Platform.runLater(() -> {
                        this.dataController.updateReports(report.assignedTo());
                        DialogUtils.showInfoNotification(
                                "You have taken action against Courier with username: " + report.getNegligent(),
                                this.pane.getScene().getWindow());
                        this.solution.clear();
                    }));
        }
    }

}
