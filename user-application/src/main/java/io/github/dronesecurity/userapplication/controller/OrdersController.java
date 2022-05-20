/*
 * Copyright (c) 2021-2022, Mirko Felice & Maxim Derevyanchenko. All rights reserved.
 * Licensed under the MIT license. See LICENSE file in the project root for details.
 */

package io.github.dronesecurity.userapplication.controller;

import io.github.dronesecurity.lib.AlertUtils;
import io.github.dronesecurity.userapplication.auth.entities.Role;
import io.github.dronesecurity.userapplication.shipping.courier.entities.Order;
import io.github.dronesecurity.userapplication.shipping.courier.entities.PlacedOrder;
import io.github.dronesecurity.userapplication.shipping.courier.utilities.OrderConstants;
import io.github.dronesecurity.userapplication.utilities.DateHelper;
import io.github.dronesecurity.userapplication.utilities.FXHelper;
import io.github.dronesecurity.userapplication.utilities.UserHelper;
import io.github.dronesecurity.userapplication.utilities.VertxHelper;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonObject;
import javafx.application.Platform;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

/**
 * Controller dedicated to starting application.
 */
public final class OrdersController implements Initializable {

    private static final String HOST = "localhost";
    private static final int PORT = 15_000;
    private static final String BASE_URI = "/courierShippingService";
    private static final String LIST_ORDERS_URI = BASE_URI + "/listOrders";
    private static final String PERFORM_DELIVERY_URI = BASE_URI + "/performDelivery";
    private static final String RESCHEDULE_DELIVERY_URI = BASE_URI + "/rescheduleDelivery";
    private static final String MONITORING_FILENAME = "monitoring.fxml";
    private static final String NEGLIGENCE_DATA_FILENAME = "negligenceData.fxml";
    private static final String COURIER_ISSUE_VIEW_FILE_NAME = "issue.fxml";
    private static final String MAINTAINER_ISSUE_VIEW_FILE_NAME = "maintainerIssue.fxml";
    private static final Runnable NOT_SELECTED_RUNNABLE = () ->
            AlertUtils.showWarningAlert("You MUST first select an order.");
    @FXML private TableView<Order> table;
    @FXML private TableColumn<Order, String> orderDateColumn;
    @FXML private TableColumn<Order, String> productColumn;
    @FXML private TableColumn<Order, String> stateColumn;
    @FXML private Button performDeliveryButton;
    @FXML private Button showReportsButton;

    @Override
    public void initialize(final URL location, final ResourceBundle resources) {
        this.orderDateColumn.setCellValueFactory(cell -> new SimpleObjectProperty<>(
                DateHelper.toString(cell.getValue().getPlacingDate())));
        this.productColumn.setCellValueFactory(cell ->
                new SimpleObjectProperty<>(cell.getValue().getProduct()));
        this.stateColumn.setCellValueFactory(new PropertyValueFactory<>("currentState"));
        VertxHelper.WEB_CLIENT.get(PORT, HOST, LIST_ORDERS_URI)
                .send(r -> {
                    if (r.succeeded()) {
                        final List<Order> orders = r.result().bodyAsJsonArray().stream()
                                .map(o -> Json.decodeValue(o.toString(), Order.class))
                                .collect(Collectors.toList());
                        this.table.setItems(FXCollections.observableList(orders));
                    }
        });
    }

    @FXML
    private void performDelivery() {
        final Optional<Order> selectedOrder = this.getSelectedOrder();
        selectedOrder.ifPresentOrElse(order -> {
            // TODO how to check ???
            if (order instanceof PlacedOrder) {
                final JsonObject body = new JsonObject()
                        .put(OrderConstants.ORDER_KEY, order)
                        .put(OrderConstants.COURIER_KEY, UserHelper.getLoggedUser().getUsername());
                VertxHelper.WEB_CLIENT.post(PORT, HOST, PERFORM_DELIVERY_URI)
                        .putHeader("Content-Type", "application/json")
                        .sendBuffer(body.toBuffer())
                        .onSuccess(h -> Platform.runLater(() -> {
                            ((Stage) this.performDeliveryButton.getScene().getWindow()).close();
                            final URL fileUrl = getClass().getResource(MONITORING_FILENAME);
                            final FXMLLoader fxmlLoader = new FXMLLoader(fileUrl);
                            FXHelper.initializeWindow(Modality.NONE, "Monitoring...", fxmlLoader)
                                    .ifPresent(Stage::show);
                        }));
            } else
                AlertUtils.showErrorAlert("You can NOT deliver an order that isn't placed.");
        }, NOT_SELECTED_RUNNABLE);
    }

    @FXML
    private void rescheduleDelivery() {
        final Optional<Order> selectedOrder = this.getSelectedOrder();
        // TODO think about checking if getCurrentState().contains("fail") or instanceof FailedOrder
        selectedOrder.ifPresentOrElse(order -> {
            if (order.getCurrentState().contains("fail"))
                VertxHelper.WEB_CLIENT.post(PORT, HOST, RESCHEDULE_DELIVERY_URI)
                        .putHeader("Content-Type", "application/json")
                        .sendBuffer(Json.encodeToBuffer(order));
            else
                AlertUtils.showErrorAlert("You can NOT reschedule an order that isn't failed.");
            }, NOT_SELECTED_RUNNABLE);
    }

    @FXML
    private void showReports() {
        final URL fileUrl = getClass().getResource(NEGLIGENCE_DATA_FILENAME);
        final FXMLLoader fxmlLoader = new FXMLLoader(fileUrl);
        FXHelper.initializeWindow(Modality.WINDOW_MODAL, "Reports", fxmlLoader).ifPresent(stage -> {
            stage.initOwner(this.showReportsButton.getScene().getWindow());
            stage.showAndWait();
        });
    }

    @FXML
    private void fillIssue() {
        Platform.runLater(() -> {
            String issueFileName = "";
            if (UserHelper.getLoggedUser().getRole() == Role.COURIER)
                issueFileName = COURIER_ISSUE_VIEW_FILE_NAME;
            else if (UserHelper.getLoggedUser().getRole() == Role.MAINTAINER)
                issueFileName = MAINTAINER_ISSUE_VIEW_FILE_NAME;
            final URL fileUrl = getClass().getResource(issueFileName);
            final FXMLLoader fxmlLoader = new FXMLLoader(fileUrl);
            FXHelper.initializeWindow(Modality.NONE, "Sending Issue...", fxmlLoader).ifPresent(stage -> {
                stage.setOnCloseRequest(null);
                stage.show();
            });
        });
    }

    private Optional<Order> getSelectedOrder() {
        return Optional.ofNullable(this.table.getSelectionModel().getSelectedItem());
    }
}
