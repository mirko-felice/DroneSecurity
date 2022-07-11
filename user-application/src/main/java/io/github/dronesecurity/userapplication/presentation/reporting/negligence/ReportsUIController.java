/*
 * Copyright (c) 2021-2022, Mirko Felice & Maxim Derevyanchenko. All rights reserved.
 * Licensed under the MIT license. See LICENSE file in the project root for details.
 */

package io.github.dronesecurity.userapplication.presentation.reporting.negligence;

import io.github.dronesecurity.userapplication.application.user.ohs.pl.GenericUser;
import io.github.dronesecurity.userapplication.domain.reporting.negligence.entities.contracts.ClosedNegligenceReport;
import io.github.dronesecurity.userapplication.domain.reporting.negligence.entities.contracts.OpenNegligenceReport;
import io.github.dronesecurity.userapplication.domain.reporting.negligence.objects.Assignee;
import io.github.dronesecurity.userapplication.domain.reporting.negligence.objects.Negligent;
import io.github.dronesecurity.userapplication.utilities.reporting.negligence.AssigneeAPIHelper;
import io.github.dronesecurity.userapplication.utilities.reporting.negligence.NegligentAPIHelper;
import io.github.dronesecurity.userapplication.utilities.user.UserAPIHelper;
import io.vertx.core.Future;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.json.Json;
import io.vertx.ext.web.client.HttpResponse;
import io.vertx.ext.web.codec.BodyCodec;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Tab;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * Controller dedicated to list and control
 * {@link io.github.dronesecurity.userapplication.domain.reporting.negligence.entities.contracts.NegligenceReport}.
 */
public final class ReportsUIController implements Initializable {

    private static final Function<HttpResponse<Buffer>, List<OpenNegligenceReport>> OPEN_MAPPER = r ->
            Arrays.asList(Json.decodeValue(r.bodyAsBuffer(), OpenNegligenceReport[].class));
    private static final Function<HttpResponse<Buffer>, List<ClosedNegligenceReport>> CLOSED_MAPPER = r ->
            Arrays.asList(Json.decodeValue(r.bodyAsBuffer(), ClosedNegligenceReport[].class));
    @FXML private Tab closedReportsTab;
    @FXML private ReportsTableUIController<OpenNegligenceReport> openController;
    @FXML private ReportsTableUIController<ClosedNegligenceReport> closedController;

    /**
     * {@inheritDoc}
     */
    @Override
    public void initialize(final URL location, final ResourceBundle resources) {
        UserAPIHelper.get(UserAPIHelper.Operation.CHECK_LOGGED_USER_ROLE, BodyCodec.string()).onSuccess(response -> {
            // TODO logic?
            if ("COURIER".equals(response.body()))
                UserAPIHelper.get(UserAPIHelper.Operation.RETRIEVE_LOGGED_COURIER_IF_PRESENT,
                                BodyCodec.json(GenericUser.class))
                        .onSuccess(user -> this.updateReports(Negligent.parse(user.body().getUsername())));
            else if ("MAINTAINER".equals(response.body()))
                UserAPIHelper.get(UserAPIHelper.Operation.RETRIEVE_LOGGED_MAINTAINER_IF_PRESENT,
                                BodyCodec.json(GenericUser.class))
                        .onSuccess(user -> this.updateReports(Assignee.parse(user.body().getUsername())));
        });
        this.openController.setReportType(false);
        this.closedController.setReportType(true);
    }

    /**
     * Sets a {@link javafx.event.EventHandler} on closed reports tab selection changed.
     * @param tabSelectedConsumer {@link Consumer} to use on tab selection
     */
    public void setOnClosedTabSelectionChanged(final Consumer<Boolean> tabSelectedConsumer) {
        this.closedReportsTab.setOnSelectionChanged(ignored ->
                tabSelectedConsumer.accept(this.closedReportsTab.isSelected()));
    }

    /**
     * Empty the detail nodes.
     */
    public void emptyDetails() {
        this.openController.emptyDetails();
        this.closedController.emptyDetails();
    }

    /**
     * Retrieve the selected {@link OpenNegligenceReport}.
     * @return a {@link OpenNegligenceReport}
     */
    public @Nullable OpenNegligenceReport getSelectedOpenReport() {
        return this.openController.getSelectedOpenReport();
    }

    /**
     * Update showed reports for the current {@link Assignee}.
     * @param assignee {@link Assignee} to retrieve reports from
     */
    public void updateReports(final @NotNull Assignee assignee) {
        this.setOpenReports(AssigneeAPIHelper.get(
                AssigneeAPIHelper.Operation.RETRIEVE_OPEN_REPORTS,
                AssigneeAPIHelper.ASSIGNEE_KEY,
                assignee.asString()));
        this.setClosedReports(AssigneeAPIHelper.get(
                AssigneeAPIHelper.Operation.RETRIEVE_CLOSED_REPORTS,
                AssigneeAPIHelper.ASSIGNEE_KEY,
                assignee.asString()));
    }

    private void updateReports(final @NotNull Negligent negligent) {
        this.setOpenReports(NegligentAPIHelper.get(
                NegligentAPIHelper.Operation.RETRIEVE_OPEN_REPORTS,
                NegligentAPIHelper.NEGLIGENT_KEY,
                negligent.asString()));
        this.setClosedReports(NegligentAPIHelper.get(
                NegligentAPIHelper.Operation.RETRIEVE_CLOSED_REPORTS,
                NegligentAPIHelper.NEGLIGENT_KEY,
                negligent.asString()));
    }

    private void setOpenReports(final @NotNull Future<HttpResponse<Buffer>> future) {
        future.map(OPEN_MAPPER).onSuccess(reports -> Platform.runLater(() ->
                this.openController.setItems(FXCollections.observableList(reports))));
    }

    private void setClosedReports(final @NotNull Future<HttpResponse<Buffer>> future) {
        future.map(CLOSED_MAPPER).onSuccess(reports -> Platform.runLater(() ->
                this.closedController.setItems(FXCollections.observableList(reports))));
    }

}
