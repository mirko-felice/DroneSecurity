/*
 * Copyright (c) 2021-2022, Mirko Felice & Maxim Derevyanchenko. All rights reserved.
 * Licensed under the MIT license. See LICENSE file in the project root for details.
 */

package io.github.dronesecurity.userapplication.presentation.shipping;

import io.github.dronesecurity.userapplication.application.user.ohs.pl.Courier;
import io.github.dronesecurity.userapplication.domain.shipping.shipping.entities.contracts.Order;
import io.github.dronesecurity.userapplication.domain.shipping.shipping.entities.contracts.OrderState;
import io.github.dronesecurity.userapplication.domain.shipping.shipping.entities.contracts.RescheduledOrder;
import io.github.dronesecurity.userapplication.domain.shipping.shipping.objects.OrderDate;
import io.github.dronesecurity.userapplication.events.DomainEvents;
import io.github.dronesecurity.userapplication.events.OrderUpdated;
import io.github.dronesecurity.userapplication.utilities.DialogUtils;
import io.github.dronesecurity.userapplication.utilities.UIHelper;
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

    private final Consumer<OrderUpdated> orderUpdatedHandler;
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
    public OrdersUIController() {
        this.orderUpdatedHandler = ignored -> this.refreshOrders();
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
            final OrderDate arrivalDate = order instanceof RescheduledOrder
                    ? ((RescheduledOrder) order).getNewEstimatedArrival()
                    : order.getEstimatedArrival();
            return new SimpleObjectProperty<>(arrivalDate.asString());
        });
        this.estimatedArrivalColumn.setReorderable(false);

        this.clientColumn.setCellValueFactory(cell ->
                new SimpleObjectProperty<>(cell.getValue().getClient().name()));
        this.clientColumn.setReorderable(false);

        this.refreshOrders();
        this.table.getSelectionModel().selectedItemProperty().addListener((observable, oldOrder, order) -> {
            this.table.getScene().getWindow().setOnHidden(ignored ->
                    DomainEvents.unregister(OrderUpdated.class, this.orderUpdatedHandler));
            if (order == null) {
                this.performDeliveryButton.setDisable(true);
                this.rescheduleDeliveryButton.setDisable(true);
                this.showDataHistoryButton.setDisable(true);
            } else {
                if (order.getCurrentState() == OrderState.PLACED || order.getCurrentState() == OrderState.RESCHEDULED) {
                    this.performDeliveryButton.setDisable(false);
                    this.rescheduleDeliveryButton.setDisable(true);
                    this.showDataHistoryButton.setDisable(true);
                } else if (order.getCurrentState() == OrderState.FAILED) {
                    this.performDeliveryButton.setDisable(true);
                    this.rescheduleDeliveryButton.setDisable(false);
                    this.showDataHistoryButton.setDisable(false);
                } else if (order.getCurrentState() == OrderState.SUCCEEDED) {
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

        DomainEvents.register(OrderUpdated.class, this.orderUpdatedHandler);
    }

    @FXML
    private void performDelivery() {
        UserAPIHelper.get(UserAPIHelper.Operation.RETRIEVE_LOGGED_COURIER_IF_PRESENT, BodyCodec.json(Courier.class))
                .onSuccess(res -> {
                    final Courier courier = res.body();
                    DialogUtils.createDronePickerDialog("Choose the Drone to use for delivery",
                                    courier.getAssignedDrones())
                            .showAndWait().ifPresent(droneId -> {
                                final Order order = this.getSelectedOrder().orElseThrow();
                                final JsonObject body = new JsonObject()
                                        .put(ShippingAPIHelper.ORDER_ID_KEY, order.getId().asLong())
                                        .put(ShippingAPIHelper.DRONE_ID_KEY, droneId);
                                ShippingAPIHelper.postJson(ShippingAPIHelper.Operation.PERFORM_DELIVERY, body)
                                        .onSuccess(ignored -> Platform.runLater(() -> {
                                            this.table.getSelectionModel().clearSelection();
                                            UIHelper.showMonitoringUI();
                                            UIHelper.showDroneControllerUI(order.getId().asLong());
                                        }));
                            });
                });
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
    private void showDataHistory() {
        this.getSelectedOrder().ifPresent(order -> UIHelper.showAccelerometerDataHistoryUI(order.getId().asLong()));
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

}
