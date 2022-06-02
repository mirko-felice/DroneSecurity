/*
 * Copyright (c) 2021-2022, Mirko Felice & Maxim Derevyanchenko. All rights reserved.
 * Licensed under the MIT license. See LICENSE file in the project root for details.
 */

package io.github.dronesecurity.userapplication.controller;

import io.github.dronesecurity.userapplication.auth.entities.Role;
import io.github.dronesecurity.userapplication.shipping.courier.entities.FailedOrder;
import io.github.dronesecurity.userapplication.shipping.courier.entities.Order;
import io.github.dronesecurity.userapplication.shipping.courier.entities.PlacedOrder;
import io.github.dronesecurity.userapplication.shipping.courier.utilities.ServiceHelper;
import io.github.dronesecurity.userapplication.utilities.DialogUtils;
import io.github.dronesecurity.userapplication.utilities.DateHelper;
import io.github.dronesecurity.userapplication.utilities.FXHelper;
import io.github.dronesecurity.userapplication.utilities.UserHelper;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonObject;
import javafx.application.Platform;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.jetbrains.annotations.NotNull;

import java.net.URL;
import java.time.Instant;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

/**
 * Controller dedicated to show and manage main courier window.
 */
public final class OrdersController implements Initializable {

    private static final String MONITORING_FILENAME = "monitoring.fxml";
    private static final String NEGLIGENCE_DATA_FILENAME = "negligenceData.fxml";
    private static final String COURIER_ISSUE_VIEW_FILE_NAME = "courierIssues.fxml";
    private static final String MAINTAINER_ISSUE_VIEW_FILE_NAME = "maintainerIssues.fxml";
    private static final Runnable NOT_SELECTED_RUNNABLE = () ->
            DialogUtils.showWarningDialog("You MUST first select an order.");
    @FXML private TableView<Order> table;
    @FXML private TableColumn<Order, String> orderDateColumn;
    @FXML private TableColumn<Order, String> productColumn;
    @FXML private TableColumn<Order, String> stateColumn;
    @FXML private Button showReportsButton;

    @Override
    public void initialize(final URL location, final ResourceBundle resources) {
        this.orderDateColumn.setCellValueFactory(cell -> new SimpleObjectProperty<>(
                DateHelper.toString(cell.getValue().getPlacingDate())));
        this.productColumn.setCellValueFactory(cell ->
                new SimpleObjectProperty<>(cell.getValue().getProduct()));
        this.stateColumn.setCellValueFactory(new PropertyValueFactory<>("currentState"));
        ServiceHelper.getOperation(ServiceHelper.Operation.LIST_ORDERS).onSuccess(res -> {
                        final List<Order> orders = res.bodyAsJsonArray().stream()
                                .map(o -> Json.decodeValue(o.toString(), Order.class))
                                .collect(Collectors.toList());
                        this.table.setItems(FXCollections.observableList(orders));
                    });
    }

    @FXML
    private void performDelivery() {
        final Optional<Order> selectedOrder = this.getSelectedOrder();
        selectedOrder.ifPresentOrElse(order -> {
            if (order instanceof PlacedOrder) {
                final JsonObject body = new JsonObject()
                        .put(ServiceHelper.ORDER_KEY, order)
                        .put(ServiceHelper.COURIER_KEY, UserHelper.getLoggedUser().getUsername());
                ServiceHelper.postJson(ServiceHelper.Operation.PERFORM_DELIVERY, body)
                        .onSuccess(h -> Platform.runLater(() -> {
                            final URL fileUrl = getClass().getResource(MONITORING_FILENAME);
                            final FXMLLoader fxmlLoader = new FXMLLoader(fileUrl);
                            fxmlLoader.setController(new MonitorController(order.getId()));
                            FXHelper.initializeWindow(Modality.WINDOW_MODAL, "Monitoring...", fxmlLoader)
                                    .ifPresent(Stage::show);
                        }));
            } else
                DialogUtils.showErrorDialog("You can NOT deliver an order that isn't placed.");
        }, NOT_SELECTED_RUNNABLE);
    }

    @FXML
    private void rescheduleDelivery() {
        final Optional<Order> selectedOrder = this.getSelectedOrder();
        selectedOrder.ifPresentOrElse(order -> {
            if (order instanceof FailedOrder) {
               this.createDatePickerDialog().showAndWait().ifPresent(newEstimatedArrival -> {
                    final JsonObject body = new JsonObject()
                            .put(ServiceHelper.ORDER_KEY, order)
                            .put(ServiceHelper.NEW_ESTIMATED_ARRIVAL_KEY, newEstimatedArrival);
                   ServiceHelper.postJson(ServiceHelper.Operation.RESCHEDULE_DELIVERY, body);
                });
            } else
                DialogUtils.showErrorDialog("You can NOT reschedule an order that isn't failed.");
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
    private void showIssues() {
        Platform.runLater(() -> {
            final String issueFileName;
            if (UserHelper.getLoggedUser().getRole() == Role.COURIER)
                issueFileName = COURIER_ISSUE_VIEW_FILE_NAME;
            else if (UserHelper.getLoggedUser().getRole() == Role.MAINTAINER)
                issueFileName = MAINTAINER_ISSUE_VIEW_FILE_NAME;
            else
                issueFileName = "";
            final URL fileUrl = getClass().getResource(issueFileName);
            final FXMLLoader fxmlLoader = new FXMLLoader(fileUrl);
            FXHelper.initializeWindow(Modality.NONE, "Issues", fxmlLoader).ifPresent(stage -> {
                stage.setOnCloseRequest(null);
                stage.show();
            });
        });
    }

    private Optional<Order> getSelectedOrder() {
        return Optional.ofNullable(this.table.getSelectionModel().getSelectedItem());
    }

    private @NotNull Dialog<Instant> createDatePickerDialog() {
        final Dialog<Instant> dialog = DialogUtils.createCustomDialog("Reschedule Delivery",
                "Choose the new estimated arrival date", ButtonType.OK, ButtonType.CANCEL);

        final DatePicker datePicker = new DatePicker(LocalDate.now());
        dialog.getDialogPane().setContent(datePicker);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == ButtonType.OK)
                return DateHelper.fromLocalDate(datePicker.getValue());
            else
                return null;
        });
        return dialog;
    }
}
