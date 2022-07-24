/*
 * Copyright (c) 2021-2022, Mirko Felice & Maxim Derevyanchenko. All rights reserved.
 * Licensed under the MIT license. See LICENSE file in the project root for details.
 */

package io.github.dronesecurity.userapplication.presentation.shipping;

import io.github.dronesecurity.userapplication.application.user.ohs.pl.Courier;
import io.github.dronesecurity.userapplication.domain.shipping.shipping.entities.contracts.Order;
import io.github.dronesecurity.userapplication.domain.shipping.shipping.entities.contracts.OrderState;
import io.github.dronesecurity.userapplication.domain.shipping.shipping.entities.contracts.RescheduledOrder;
import io.github.dronesecurity.userapplication.domain.shipping.shipping.events.OrderFailed;
import io.github.dronesecurity.userapplication.domain.shipping.shipping.events.OrderSucceeded;
import io.github.dronesecurity.userapplication.events.DomainEvents;
import io.github.dronesecurity.userapplication.presentation.UIHelper;
import io.github.dronesecurity.userapplication.utilities.DialogUtils;
import io.github.dronesecurity.userapplication.utilities.shipping.ShippingAPIHelper;
import io.github.dronesecurity.userapplication.utilities.user.UserAPIHelper;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.codec.BodyCodec;
import javafx.application.Platform;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

import java.net.URL;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.function.Consumer;
import java.util.stream.Collectors;

/**
 * Controller dedicated to show and manage main courier window.
 */
public final class OrdersUIController implements Initializable {

    private final Consumer<OrderSucceeded> orderSucceededHandler;
    private final Consumer<OrderFailed> orderFailedHandler;
    @FXML private TableView<Order> table;
    @FXML private TableColumn<Order, String> orderIdColumn;
    @FXML private TableColumn<Order, String> orderDateColumn;
    @FXML private TableColumn<Order, String> productColumn;
    @FXML private TableColumn<Order, String> stateColumn;
    @FXML private TableColumn<Order, String> estimatedArrivalColumn;
    @FXML private TableColumn<Order, String> clientColumn;
    @FXML private Button performDeliveryButton;
    @FXML private Button rescheduleDeliveryButton;
    @FXML private Button showProximityDataHistoryButton;
    @FXML private Button showAccelerometerDataHistoryButton;
    @FXML private Button showCameraDataHistoryButton;

    /**
     * Build the controller.
     */
    public OrdersUIController() {
        this.orderSucceededHandler = this.generateOrderConsumer();
        this.orderFailedHandler = this.generateOrderConsumer();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void initialize(final URL location, final ResourceBundle resources) {
        this.orderIdColumn.setCellValueFactory(cell ->
                new SimpleObjectProperty<>("#" + cell.getValue().getId().asLong()));
        this.orderIdColumn.setReorderable(false);

        this.orderDateColumn.setCellValueFactory(cell ->
                new SimpleObjectProperty<>(cell.getValue().getPlacingDate().asString()));
        this.orderDateColumn.setReorderable(false);

        this.productColumn.setCellValueFactory(cell ->
                new SimpleObjectProperty<>(cell.getValue().getProduct().name()));
        this.productColumn.setReorderable(false);

        this.stateColumn.setCellValueFactory(cell ->
                new SimpleObjectProperty<>(cell.getValue().getCurrentState().toString()));
        this.stateColumn.setReorderable(false);

        this.estimatedArrivalColumn.setCellValueFactory(cell -> {
            final Order order = cell.getValue();
            return new SimpleObjectProperty<>(
                    (order instanceof RescheduledOrder
                            ? ((RescheduledOrder) order).getNewEstimatedArrival()
                            : order.getEstimatedArrival())
                    .asString());
        });
        this.estimatedArrivalColumn.setReorderable(false);

        this.clientColumn.setCellValueFactory(cell ->
                new SimpleObjectProperty<>(cell.getValue().getClient().name()));
        this.clientColumn.setReorderable(false);

        this.refreshOrders();
        this.table.getSelectionModel().selectedItemProperty().addListener((observable, oldOrder, order) -> {
            this.table.getScene().getWindow().setOnHidden(ignored -> {
                DomainEvents.unregister(OrderSucceeded.class, this.orderSucceededHandler);
                DomainEvents.unregister(OrderFailed.class, this.orderFailedHandler);
            });
            this.checkOrderType(order);
        });

        DomainEvents.register(OrderSucceeded.class, this.orderSucceededHandler);
        DomainEvents.register(OrderFailed.class, this.orderFailedHandler);
    }

    @FXML
    private void performDelivery() {
        UserAPIHelper.get(UserAPIHelper.Operation.RETRIEVE_LOGGED_COURIER_IF_PRESENT, BodyCodec.json(Courier.class))
                .onSuccess(res -> Platform.runLater(() -> {
                    final Courier courier = res.body();
                    DialogUtils.createDronePickerDialog("Choose the Drone to use for delivery",
                                    courier.getAssignedDrones())
                            .showAndWait().ifPresent(droneId -> {
                                final Order order = this.getSelectedOrder().orElseThrow();
                                final JsonObject body = new JsonObject()
                                        .put(ShippingAPIHelper.ORDER_ID_KEY, order.getId().asLong())
                                        .put(ShippingAPIHelper.DRONE_ID_KEY, droneId)
                                        .put(ShippingAPIHelper.COURIER_USERNAME_KEY, courier.getUsername());
                                ShippingAPIHelper.postJson(ShippingAPIHelper.Operation.PERFORM_DELIVERY, body)
                                        .onSuccess(ignored -> Platform.runLater(() -> {
                                            this.table.getSelectionModel().clearSelection();
                                            this.refreshOrders();
                                            UIHelper.showMonitoringUI();
                                            UIHelper.showDroneControllerUI(order.getId().asLong(), droneId);
                                        }));
                            });
                }));
    }

    @FXML
    private void rescheduleDelivery() {
        DialogUtils.createDatePickerDialog().showAndWait().ifPresent(newEstimatedArrival -> {
            final Order order = this.getSelectedOrder().orElseThrow();
            final JsonObject body = new JsonObject()
                    .put(ShippingAPIHelper.ORDER_ID_KEY, order.getId().asLong())
                    .put(ShippingAPIHelper.NEW_ESTIMATED_ARRIVAL_KEY, newEstimatedArrival.asString());
            ShippingAPIHelper.postJson(ShippingAPIHelper.Operation.RESCHEDULE_DELIVERY, body)
                    .onSuccess(ignored -> {
                        this.table.getSelectionModel().clearSelection();
                        this.refreshOrders();
                    });
        });
    }

    @FXML
    private void showProximityDataHistory() {
        this.getSelectedOrder().ifPresent(order -> UIHelper.showProximityDataHistoryUI(order.getId().asLong()));
    }

    @FXML
    private void showAccelerometerDataHistory() {
        this.getSelectedOrder().ifPresent(order -> UIHelper.showAccelerometerDataHistoryUI(order.getId().asLong()));
    }

    @FXML
    private void showCameraDataHistory() {
        this.getSelectedOrder().ifPresent(order -> UIHelper.showCameraDataHistoryUI(order.getId().asLong()));
    }

    private void refreshOrders() {
        ShippingAPIHelper.get(ShippingAPIHelper.Operation.LIST_ORDERS).onSuccess(res -> {
            final List<Order> orders = res.bodyAsJsonArray().stream()
                    .map(o -> Json.decodeValue(o.toString(), Order.class))
                    .sorted(Comparator.comparing(order -> order.getId().asLong()))
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

    private void checkOrderType(final Order order) {
        if (order == null) {
            this.performDeliveryButton.setDisable(true);
            this.rescheduleDeliveryButton.setDisable(true);
            this.showProximityDataHistoryButton.setDisable(true);
            this.showAccelerometerDataHistoryButton.setDisable(true);
            this.showCameraDataHistoryButton.setDisable(true);
        } else {
            if (order.getCurrentState() == OrderState.PLACED || order.getCurrentState() == OrderState.RESCHEDULED) {
                this.performDeliveryButton.setDisable(false);
                this.rescheduleDeliveryButton.setDisable(true);
                this.showProximityDataHistoryButton.setDisable(true);
                this.showAccelerometerDataHistoryButton.setDisable(true);
                this.showCameraDataHistoryButton.setDisable(true);
            } else if (order.getCurrentState() == OrderState.FAILED) {
                this.performDeliveryButton.setDisable(true);
                this.rescheduleDeliveryButton.setDisable(false);
                this.showProximityDataHistoryButton.setDisable(false);
                this.showAccelerometerDataHistoryButton.setDisable(false);
                this.showCameraDataHistoryButton.setDisable(false);
            } else if (order.getCurrentState() == OrderState.SUCCEEDED) {
                this.performDeliveryButton.setDisable(true);
                this.rescheduleDeliveryButton.setDisable(true);
                this.showProximityDataHistoryButton.setDisable(false);
                this.showAccelerometerDataHistoryButton.setDisable(false);
                this.showCameraDataHistoryButton.setDisable(false);
            } else {
                this.performDeliveryButton.setDisable(true);
                this.rescheduleDeliveryButton.setDisable(true);
                this.showProximityDataHistoryButton.setDisable(true);
                this.showAccelerometerDataHistoryButton.setDisable(true);
                this.showCameraDataHistoryButton.setDisable(true);
            }
        }
    }

    private <T> Consumer<T> generateOrderConsumer() {
        return ignored -> this.refreshOrders();
    }
}
