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
import io.github.dronesecurity.userapplication.shipping.courier.utilities.ShippingServiceHelper;
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

import java.net.URL;
import java.time.Instant;
import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Collectors;

/**
 * Controller dedicated to show and manage main courier window.
 */
public final class OrdersController implements Initializable {

    private static final String MONITORING_FXML = "monitoring.fxml";
    private static final String DATA_FXML = "data.fxml";
    private final Consumer<OrdersUpdate> ordersUpdateHandler;
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

    /**
     * Build the controller.
     */
    public OrdersController() {
        this.ordersUpdateHandler = ignored -> this.refreshOrders();
    }

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

        this.estimatedArrivalColumn.setCellValueFactory(cell -> {
            final Order order = cell.getValue();
            final Instant arrivalDate = order instanceof RescheduledOrder
                    ? ((RescheduledOrder) order).getNewEstimatedArrival()
                    : order.getEstimatedArrival();
            return new SimpleObjectProperty<>(DateHelper.toString(arrivalDate));
        });
        this.estimatedArrivalColumn.setReorderable(false);

        this.clientColumn.setCellValueFactory(cell ->
                new SimpleObjectProperty<>(cell.getValue().getClient()));
        this.clientColumn.setReorderable(false);

        this.refreshOrders();
        this.table.getSelectionModel().selectedItemProperty().addListener((observable, oldOrder, order) -> {
            this.table.getScene().getWindow().setOnHidden(ignored ->
                    DomainEvents.unregister(OrdersUpdate.class, this.ordersUpdateHandler));
            if (order == null) {
                this.performDeliveryButton.setDisable(true);
                this.rescheduleDeliveryButton.setDisable(true);
                this.showDataHistoryButton.setDisable(true);
            } else {
                if (OrderConstants.PLACED_ORDER_STATE.equals(order.getCurrentState())
                    || OrderConstants.RESCHEDULED_ORDER_STATE.equals(order.getCurrentState())) {
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

        DomainEvents.register(OrdersUpdate.class, this.ordersUpdateHandler);
    }

    @FXML
    private void performDelivery() {
        this.getSelectedOrder().flatMap(o -> CastHelper.safeCast(o, PlacedOrder.class)).ifPresent(order ->
            DialogUtils.createDronePickerDialog("Choose the Drone to use for delivery")
                    .showAndWait().ifPresent(droneId -> {
                        final JsonObject body = new JsonObject()
                                .put(ShippingServiceHelper.ORDER_KEY, order)
                                .put(ShippingServiceHelper.COURIER_KEY, UserHelper.logged().getUsername())
                                .put(ShippingServiceHelper.DRONE_ID_KEY, droneId);
                        ShippingServiceHelper.postJson(ShippingServiceHelper.Operation.PERFORM_DELIVERY, body)
                                .onSuccess(h -> Platform.runLater(() -> {
                                    ((Courier) UserHelper.logged()).removeDrone(droneId);
                                    this.table.getSelectionModel().clearSelection();
                                    final URL fileUrl = getClass().getResource(MONITORING_FXML);
                                    final FXMLLoader fxmlLoader = new FXMLLoader(fileUrl);
                                    fxmlLoader.setController(new MonitorController(order.getId()));
                                    FXHelper.initializeWindow(Modality.WINDOW_MODAL, "Monitoring...", fxmlLoader)
                                            .ifPresent(stage -> {
                                                stage.setOnCloseRequest(Event::consume);
                                                stage.setOnHidden(ignored ->
                                                        ((Courier) UserHelper.logged()).addDrone(droneId));
                                                stage.show();
                                            });
                                }));
                    }));
    }

    @FXML
    private void rescheduleDelivery() {
        this.getSelectedOrder().flatMap(o -> CastHelper.safeCast(o, FailedOrder.class)).ifPresent(order ->
                DialogUtils.createDatePickerDialog().showAndWait().ifPresent(newEstimatedArrival -> {
                    final JsonObject body = new JsonObject()
                            .put(ShippingServiceHelper.ORDER_KEY, order)
                            .put(ShippingServiceHelper.NEW_ESTIMATED_ARRIVAL_KEY,
                                    DateHelper.toString(newEstimatedArrival));
                    ShippingServiceHelper.postJson(ShippingServiceHelper.Operation.RESCHEDULE_DELIVERY, body)
                            .onSuccess(ignored -> {
                                this.table.getSelectionModel().clearSelection();
                                this.refreshOrders();
                            });
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
        ShippingServiceHelper.getOperation(ShippingServiceHelper.Operation.LIST_ORDERS).onSuccess(res -> {
            final List<Order> orders = res.bodyAsJsonArray().stream()
                    .map(o -> Json.decodeValue(o.toString(), Order.class))
                    .sorted(Comparator.comparing(Order::getId))
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

}
