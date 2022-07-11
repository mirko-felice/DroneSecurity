/*
 * Copyright (c) 2021-2022, Mirko Felice & Maxim Derevyanchenko. All rights reserved.
 * Licensed under the MIT license. See LICENSE file in the project root for details.
 */

package io.github.dronesecurity.userapplication.presentation.reporting.negligence;

import io.github.dronesecurity.userapplication.application.user.ohs.pl.GenericUser;
import io.github.dronesecurity.userapplication.domain.reporting.negligence.entities.contracts.ClosedNegligenceReport;
import io.github.dronesecurity.userapplication.domain.reporting.negligence.entities.contracts.NegligenceReport;
import io.github.dronesecurity.userapplication.domain.reporting.negligence.objects.Assignee;
import io.github.dronesecurity.userapplication.domain.reporting.negligence.objects.DroneData;
import io.github.dronesecurity.userapplication.domain.reporting.negligence.objects.NegligenceActionForm;
import io.github.dronesecurity.userapplication.domain.reporting.negligence.objects.Negligent;
import io.github.dronesecurity.userapplication.utilities.user.UserAPIHelper;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.codec.BodyCodec;
import javafx.application.Platform;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.TextFieldTableCell;
import org.jetbrains.annotations.NotNull;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.function.Consumer;

/**
 * Controller dedicated to control the reports list.
 * @param <T> type parameter extending {@link NegligenceReport}
 */
public final class ReportsTableUIController<T extends NegligenceReport> implements Initializable {

    @FXML private TableView<T> table;
    @FXML private TableColumn<T, Negligent> negligentColumn;
    @FXML private TableColumn<T, Assignee> assigneeColumn;
    @FXML private TableColumn<T, DroneData> dataColumn;
    @FXML private TableColumn<T, NegligenceActionForm> actionFormColumn;
    @FXML private ReportDetailsUIController detailsController;

    /**
     * {@inheritDoc}
     */
    @Override
    public void initialize(final URL location, final ResourceBundle resources) {
        this.table.setPlaceholder(new Label("No reports found."));
        this.table.getSelectionModel().setCellSelectionEnabled(true);

        final Consumer<GenericUser> userConsumer = user ->
                Platform.runLater(() -> this.detailsController.updateDetails(user));

        final Consumer<Assignee> assigneeConsumer = assignee -> {
            final JsonObject body = new JsonObject();
            body.put(UserAPIHelper.USERNAME_KEY, assignee.asString());
            UserAPIHelper.get(
                    UserAPIHelper.Operation.RETRIEVE_MAINTAINER_BY_USERNAME, body, BodyCodec.json(GenericUser.class))
                    .onSuccess(res -> userConsumer.accept(res.body()));
        };

        this.assigneeColumn.setCellFactory(ignored -> new NegligenceReportCell<>(assigneeConsumer));
        this.assigneeColumn.setCellValueFactory(value -> new SimpleObjectProperty<>(value.getValue().assignedTo()));
        this.assigneeColumn.setReorderable(false);

        final Consumer<Negligent> negligentConsumer = negligent -> {
            final JsonObject body = new JsonObject();
            body.put(UserAPIHelper.USERNAME_KEY, negligent.asString());
            UserAPIHelper.get(
                    UserAPIHelper.Operation.RETRIEVE_COURIER_BY_USERNAME, body, BodyCodec.json(GenericUser.class))
                    .onSuccess(res -> userConsumer.accept(res.body()));
        };

        this.negligentColumn.setCellFactory(ignored -> new NegligenceReportCell<>(negligentConsumer));
        this.negligentColumn.setCellValueFactory(value -> new SimpleObjectProperty<>(value.getValue().getNegligent()));
        this.negligentColumn.setReorderable(false);

        final Consumer<DroneData> dataConsumer = data ->
                Platform.runLater(() -> this.detailsController.updateDetails(data));

        this.dataColumn.setCellFactory(ignored -> new NegligenceReportCell<>(dataConsumer));
        this.dataColumn.setCellValueFactory(value -> new SimpleObjectProperty<>(value.getValue().getData()));
        this.dataColumn.setReorderable(false);

        // todo check if needed
        this.table.itemsProperty().addListener(new NegligenceReportListener<>(() ->
                this.table.getColumns().forEach(col -> col.setResizable(false))));
    }

    /**
     * Set the report type.
     * @param isClosed true if reports are closed
     */
    public void setReportType(final boolean isClosed) {
        if (isClosed) {
            final Consumer<NegligenceActionForm> actionFormConsumer = actionForm ->
                    Platform.runLater(() -> this.detailsController.updateDetails(actionForm));
            this.actionFormColumn.setCellFactory(ignored -> new NegligenceReportCell<>(actionFormConsumer));
            this.actionFormColumn.setCellValueFactory(value ->
                    new SimpleObjectProperty<>(((ClosedNegligenceReport) value.getValue()).getActionForm()));
            this.actionFormColumn.setReorderable(false);
        } else
            this.table.getColumns().remove(this.actionFormColumn);
    }

    /**
     * Empty the detail nodes.
     */
    public void emptyDetails() {
        this.detailsController.emptyDetails();
    }

    /**
     * Retrieve the selected item of the table.
     * @return the selected report
     */
    public T getSelectedOpenReport() {
        return this.table.getSelectionModel().getSelectedItem();
    }

    /**
     * Set the items to the table.
     * @param observableList {@link ObservableList} to set
     */
    public void setItems(final ObservableList<T> observableList) {
        this.table.setItems(observableList);
    }

    /**
     * Class to create default {@link TextFieldTableCell} adding a {@link Consumer} to trigger code on mouse clicked
     * on a non-empty cell.
     * @param <T> table type value extending {@link NegligenceReport}
     * @param <S> cell type value
     */
    private static final class NegligenceReportCell<T extends NegligenceReport, S> extends TextFieldTableCell<T, S> {

        private final Consumer<S> consumer;

        private NegligenceReportCell(final Consumer<S> consumer) {
            this.consumer = consumer;
        }

        @Override
        public void updateItem(final S item, final boolean empty) {
            super.updateItem(item, empty);

            setOnMouseClicked(event -> {
                if (!empty)
                    this.consumer.accept(item);
            });
        }
    }

    /**
     * {@link ChangeListener} that on changed event it runs a {@link Runnable} and remove itself from listening over.
     * @param <T> type parameter
     */
    private static final class NegligenceReportListener<T extends NegligenceReport>
            implements ChangeListener<ObservableList<T>> {

        private final Runnable changedRunnable;

        private NegligenceReportListener(final Runnable changedRunnable) {
            this.changedRunnable = changedRunnable;
        }

        @Override
        public void changed(final @NotNull ObservableValue<? extends ObservableList<T>> observable,
                            final ObservableList<T> oldValue,
                            final ObservableList<T> newValue) {
            this.changedRunnable.run();
            observable.removeListener(this);
        }
    }


}
