/*
 * Copyright (c) 2021-2022, Mirko Felice & Maxim Derevyanchenko. All rights reserved.
 * Licensed under the MIT license. See LICENSE file in the project root for details.
 */

package io.github.dronesecurity.userapplication.controller;

import io.github.dronesecurity.lib.DateHelper;
import io.github.dronesecurity.userapplication.auth.entities.Courier;
import io.github.dronesecurity.userapplication.events.DomainEvents;
import io.github.dronesecurity.userapplication.events.OrdersUpdate;
import io.github.dronesecurity.userapplication.shipping.courier.entities.*;
import io.github.dronesecurity.userapplication.shipping.courier.utilities.OrderConstants;
import io.github.dronesecurity.userapplication.shipping.courier.utilities.ServiceHelper;
import io.github.dronesecurity.userapplication.utilities.*;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonObject;
import javafx.application.Platform;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.jetbrains.annotations.NotNull;

import java.net.URL;
import java.time.Instant;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

/**
 * Controller dedicated to show and manage main courier window.
 */
public final class OrdersController implements Initializable {

    private static final String MONITORING_FXML = "monitoring.fxml";
    private static final String DATA_FXML = "data.fxml";
    @FXML private TableView<Order> table;
    @FXML private TableColumn<Order, String> orderIdColumn;
    @FXML private TableColumn<Order, String> orderDateColumn;
    @FXML private TableColumn<Order, String> productColumn;
    @FXML private TableColumn<Order, String> stateColumn;
    @FXML private TableColumn<Order, String> estimatedArrivalColumn;
    @FXML private TableColumn<Order, String> clientColumn;
    @FXML private Button performDeliveryButton;
    @FXML private Button rescheduleDeliveryButton;
    @FXML private Button showDataHistoryButton;

    @Override
    public void initialize(final URL location, final ResourceBundle resources) {
        this.orderIdColumn.setCellValueFactory(cell ->
                new SimpleObjectProperty<>("#" + cell.getValue().getId()));
        this.orderIdColumn.setReorderable(false);

        this.orderDateColumn.setCellValueFactory(cell ->
                new SimpleObjectProperty<>(DateHelper.toString(cell.getValue().getPlacingDate())));
        this.orderDateColumn.setReorderable(false);

        this.productColumn.setCellValueFactory(cell ->
                new SimpleObjectProperty<>(cell.getValue().getProduct()));
        this.productColumn.setReorderable(false);

        this.stateColumn.setCellValueFactory(cell ->
                new SimpleObjectProperty<>(cell.getValue().getCurrentState()));
        this.stateColumn.setReorderable(false);

        this.estimatedArrivalColumn.setCellValueFactory(cell ->
                new SimpleObjectProperty<>(DateHelper.toString(cell.getValue().getEstimatedArrival())));
        this.estimatedArrivalColumn.setReorderable(false);

        this.clientColumn.setCellValueFactory(cell ->
                new SimpleObjectProperty<>(cell.getValue().getClient()));
        this.clientColumn.setReorderable(false);

        this.refreshOrders();
        this.table.getSelectionModel().selectedItemProperty().addListener((observable, oldOrder, order) -> {
            if (order == null) {
                this.performDeliveryButton.setDisable(true);
            } else {
                if (OrderConstants.PLACED_ORDER_STATE.equals(order.getCurrentState())) {
                    this.performDeliveryButton.setDisable(false);
                    this.rescheduleDeliveryButton.setDisable(true);
                    this.showDataHistoryButton.setDisable(true);
                } else if (OrderConstants.FAILED_ORDER_STATE.equals(order.getCurrentState())) {
                    this.performDeliveryButton.setDisable(true);
                    this.rescheduleDeliveryButton.setDisable(false);
                    this.showDataHistoryButton.setDisable(false);
                } else if (OrderConstants.DELIVERED_ORDER_STATE.equals(order.getCurrentState())) {
                    this.performDeliveryButton.setDisable(true);
                    this.rescheduleDeliveryButton.setDisable(true);
                    this.showDataHistoryButton.setDisable(false);
                } else {
                    this.performDeliveryButton.setDisable(true);
                    this.rescheduleDeliveryButton.setDisable(true);
                    this.showDataHistoryButton.setDisable(true);
                }
            }
        });

        DomainEvents.register(OrdersUpdate.class, ignored -> this.refreshOrders());
    }

    @FXML
    private void performDelivery() {
        this.getSelectedOrder().flatMap(o -> CastHelper.safeCast(o, PlacedOrder.class)).ifPresent(order ->
            this.createDronePickerDialog().showAndWait().ifPresent(droneId -> {
                final JsonObject body = new JsonObject()
                        .put(ServiceHelper.ORDER_KEY, order)
                        .put(ServiceHelper.COURIER_KEY, UserHelper.logged().getUsername())
                        .put(ServiceHelper.DRONE_ID_KEY, droneId);
                ServiceHelper.postJson(ServiceHelper.Operation.PERFORM_DELIVERY, body)
                        .onSuccess(h -> Platform.runLater(() -> {
                            ((Courier) UserHelper.logged()).removeDrone(droneId);
                            this.table.getSelectionModel().clearSelection();
                            final URL fileUrl = getClass().getResource(MONITORING_FXML);
                            final FXMLLoader fxmlLoader = new FXMLLoader(fileUrl);
                            fxmlLoader.setController(new MonitorController(order.getId()));
                            FXHelper.initializeWindow(Modality.WINDOW_MODAL, "Monitoring...", fxmlLoader)
                                    .ifPresent(stage -> {
                                        stage.setOnCloseRequest(Event::consume);
                                        stage.setOnHidden(ignored -> ((Courier) UserHelper.logged()).addDrone(droneId));
                                        stage.show();
                                    });
                        }));
            }));
    }

    @FXML
    private void rescheduleDelivery() {
        this.getSelectedOrder().flatMap(o -> CastHelper.safeCast(o, FailedOrder.class)).ifPresent(order ->
                this.createDatePickerDialog().showAndWait().ifPresent(newEstimatedArrival -> {
                    final JsonObject body = new JsonObject()
                            .put(ServiceHelper.ORDER_KEY, order)
                            .put(ServiceHelper.NEW_ESTIMATED_ARRIVAL_KEY, DateHelper.toString(newEstimatedArrival));
                    ServiceHelper.postJson(ServiceHelper.Operation.RESCHEDULE_DELIVERY, body).onSuccess(ignored ->
                            this.refreshOrders());
                }));
    }

    @FXML
    private void showDataHistory() {
        this.getSelectedOrder().ifPresent(order -> {
            final FXMLLoader loader = new FXMLLoader(OrdersController.class.getResource(DATA_FXML));
            loader.setController(new DataController(order.getId()));
            FXHelper.initializeWindow(Modality.WINDOW_MODAL, "Data History", loader).ifPresent(Stage::show);
        });
    }

    private void refreshOrders() {
        ServiceHelper.getOperation(ServiceHelper.Operation.LIST_ORDERS).onSuccess(res -> {
            final List<Order> orders = res.bodyAsJsonArray().stream()
                    .map(o -> Json.decodeValue(o.toString(), Order.class))
                    .collect(Collectors.toList());
           Platform.runLater(() -> {
                this.table.setItems(FXCollections.observableList(orders));
                this.table.refresh();
            });
        });
    }

    private Optional<Order> getSelectedOrder() {
        return Optional.ofNullable(this.table.getSelectionModel().getSelectedItem());
    }

    private @NotNull Dialog<Instant> createDatePickerDialog() {
        final Dialog<Instant> dialog = DialogUtils.createCustomDialog("Reschedule Delivery",
                "Choose the new estimated arrival date", ButtonType.OK, ButtonType.CANCEL);

        final DatePicker datePicker = new DatePicker(LocalDate.now().plus(1, ChronoUnit.DAYS));
        datePicker.setDayCellFactory(ignored -> new OnlyFutureDateCell());
        dialog.getDialogPane().setContent(datePicker);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == ButtonType.OK)
                return DateHelper.fromLocalDate(datePicker.getValue());
            else
                return null;
        });
        return dialog;
    }

    private @NotNull Dialog<String> createDronePickerDialog() {
        final Dialog<String> dialog = DialogUtils.createCustomDialog("Drone Selection",
                "Choose the Drone to use for delivery", ButtonType.OK, ButtonType.CANCEL);

        final List<String> drones = ((Courier) UserHelper.logged()).getDrones();
        final ChoiceBox<String> choiceBox = new ChoiceBox<>(FXCollections.observableList(drones));
        choiceBox.setValue(drones.get(0));
        dialog.getDialogPane().setContent(choiceBox);

        dialog.setResultConverter(button -> {
            if (button == ButtonType.OK)
                return choiceBox.getValue();
            else
                return null;
        });
        return dialog;
    }

    /**
     * {@link DatePicker} cell disabling date if it's chronologically before today.
     */
    private static class OnlyFutureDateCell extends DateCell {
        @Override
        public void updateItem(final LocalDate date, final boolean empty) {
            super.updateItem(date, empty);
            setDisable(empty || date.compareTo(LocalDate.now()) <= 0);
        }
    }
}
