/*
 * Copyright (c) 2021-2022, Mirko Felice & Maxim Derevyanchenko. All rights reserved.
 * Licensed under the MIT license. See LICENSE file in the project root for details.
 */

package io.github.dronesecurity.userapplication.controller;

import io.github.dronesecurity.lib.DrivingMode;
import io.github.dronesecurity.lib.MqttMessageParameterConstants;
import io.github.dronesecurity.lib.MqttMessageValueConstants;
import io.github.dronesecurity.userapplication.drone.monitoring.UserMonitoringService;
import io.github.dronesecurity.userapplication.events.*;
import io.github.dronesecurity.userapplication.reporting.negligence.services.CourierNegligenceReportService;
import io.github.dronesecurity.userapplication.shipping.courier.utilities.ServiceHelper;
import io.github.dronesecurity.userapplication.utilities.DialogUtils;
import io.github.dronesecurity.userapplication.utilities.FXHelper;
import io.vertx.core.json.JsonObject;
import javafx.application.Platform;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.controlsfx.control.ToggleSwitch;
import org.jetbrains.annotations.NotNull;

import java.net.URL;
import java.util.Map;
import java.util.ResourceBundle;

/**
 * Controller dedicated to monitoring delivery.
 */
public final class MonitorController implements Initializable {

    private final UserMonitoringService monitoringService;
    private final CourierNegligenceReportService negligenceReportService;
    private final String orderId;

    @FXML private Label proximityCurrentData;
    @FXML private TableView<Map<String, Double>> accelerometerCurrentData;
    @FXML private Label cameraCurrentData;

    @FXML private TableView<Double> proximityPreviousData;
    @FXML private TableView<Map<String, Double>> accelerometerPreviousData;
    @FXML private TableView<Long> cameraPreviousData;

    @FXML private TableColumn<Double, Double> proximityPreviousDataColumn;
    @FXML private TableColumn<Map<String, Double>, Double> accelerometerPreviousXDataColumn;
    @FXML private TableColumn<Map<String, Double>, Double> accelerometerPreviousYDataColumn;
    @FXML private TableColumn<Map<String, Double>, Double> accelerometerPreviousZDataColumn;
    @FXML private TableColumn<Long, Long> cameraPreviousDataColumn;

    @FXML private TableColumn<Map<String, Double>, Double> currentAccelerometerXValue;
    @FXML private TableColumn<Map<String, Double>, Double> currentAccelerometerYValue;
    @FXML private TableColumn<Map<String, Double>, Double> currentAccelerometerZValue;

    // Controls
    @FXML private Accordion accordion;
    @FXML private TitledPane controlsPane;
    @FXML private ToggleSwitch switchMode;
    @FXML private Button proceedButton;
    @FXML private Button haltButton;
    @FXML private Label deliveryStatusLabel;
    @FXML private Label currentSituationLabel;
    @FXML private Button recallButton;

    /**
     * Build the Controller to interact with services.
     * @param orderId order identifier to monitoring
     */
    public MonitorController(final String orderId) {
        this.orderId = orderId;
        this.monitoringService = new UserMonitoringService(orderId);
        this.negligenceReportService = CourierNegligenceReportService.getInstance();
    }

    @Override
    public void initialize(final URL location, final ResourceBundle resources) {
        this.accordion.setExpandedPane(this.controlsPane);

        this.negligenceReportService.subscribeToNewNegligence(newNegligence ->
                Platform.runLater(() -> DialogUtils.showInfoNotification("INFO",
                        "You have committed a negligence. Maintainer " + newNegligence.getReport().assignedTo()
                        + " will take care of this. Go to the 'reports' window to show more information about it.",
                        this.switchMode.getScene().getWindow())));

        this.monitoringService.subscribeToDataRead(this::onDataRead);
        this.monitoringService.subscribeToWarningSituation(this::onWarning);
        this.monitoringService.subscribeToCriticalSituation(this::onCritical);
        this.monitoringService.subscribeToOrderStatusChange(this::onStatusChanged);
        this.monitoringService.subscribeToStandardSituation(this::backOnStandardSituation);

        this.proximityPreviousDataColumn.setCellValueFactory(cell -> new SimpleObjectProperty<>(cell.getValue()));
        this.proximityPreviousDataColumn.setCellFactory(ignored -> new FXHelper.ProximityCell<>());
        this.proximityPreviousDataColumn.setReorderable(false);

        this.accelerometerPreviousXDataColumn.setCellValueFactory(cell ->
                new SimpleObjectProperty<>(cell.getValue().get(MqttMessageParameterConstants.ROLL)));
        this.accelerometerPreviousXDataColumn.setCellFactory(ignored -> new FXHelper.AngleCell<>());
        this.accelerometerPreviousXDataColumn.setReorderable(false);

        this.accelerometerPreviousYDataColumn.setCellValueFactory(cell ->
                new SimpleObjectProperty<>(cell.getValue().get(MqttMessageParameterConstants.PITCH)));
        this.accelerometerPreviousYDataColumn.setCellFactory(ignored -> new FXHelper.AngleCell<>());
        this.accelerometerPreviousYDataColumn.setReorderable(false);

        this.accelerometerPreviousZDataColumn.setCellValueFactory(cell ->
                new SimpleObjectProperty<>(cell.getValue().get(MqttMessageParameterConstants.YAW)));
        this.accelerometerPreviousZDataColumn.setCellFactory(ignored -> new FXHelper.AngleCell<>());
        this.accelerometerPreviousZDataColumn.setReorderable(false);

        this.cameraPreviousDataColumn.setCellValueFactory(cell -> new SimpleObjectProperty<>(cell.getValue()));
        this.cameraPreviousDataColumn.setCellFactory(ignored -> new FXHelper.CameraCell<>());
        this.cameraPreviousDataColumn.setReorderable(false);

        this.currentAccelerometerXValue.setCellValueFactory(cell ->
                new SimpleObjectProperty<>(cell.getValue().get(MqttMessageParameterConstants.ROLL)));
        this.currentAccelerometerXValue.setCellFactory(ignored -> new FXHelper.AngleCell<>());
        this.currentAccelerometerXValue.setReorderable(false);

        this.currentAccelerometerYValue.setCellValueFactory(cell ->
                new SimpleObjectProperty<>(cell.getValue().get(MqttMessageParameterConstants.PITCH)));
        this.currentAccelerometerYValue.setCellFactory(ignored -> new FXHelper.AngleCell<>());
        this.currentAccelerometerYValue.setReorderable(false);

        this.currentAccelerometerZValue.setCellValueFactory(cell ->
                new SimpleObjectProperty<>(cell.getValue().get(MqttMessageParameterConstants.YAW)));
        this.currentAccelerometerZValue.setCellFactory(ignored -> new FXHelper.AngleCell<>());
        this.currentAccelerometerZValue.setReorderable(false);

        this.switchMode.selectedProperty().addListener((ignored, unused, isAutomatic) -> {
            if (Boolean.TRUE.equals(isAutomatic))
                this.monitoringService.changeMode(DrivingMode.AUTOMATIC);
            else
                this.monitoringService.changeMode(DrivingMode.MANUAL);
            this.proceedButton.setDisable(!isAutomatic
                   || MqttMessageValueConstants.DELIVERY_SUCCESSFUL_MESSAGE.equals(this.deliveryStatusLabel.getText())
                   || MqttMessageValueConstants.DELIVERY_FAILED_MESSAGE.equals(this.deliveryStatusLabel.getText()));
            this.haltButton.setDisable(true);
            this.recallButton.setDisable(!isAutomatic
                   || !MqttMessageValueConstants.DELIVERY_SUCCESSFUL_MESSAGE.equals(this.deliveryStatusLabel.getText())
                   && !MqttMessageValueConstants.DELIVERY_FAILED_MESSAGE.equals(this.deliveryStatusLabel.getText()));
        });
    }

    private void onDataRead(final DataRead dataRead) {
        Platform.runLater(() -> {
            if (!this.proximityCurrentData.getText().isEmpty())
                this.proximityPreviousData.getItems().add(0,
                        Double.valueOf(this.proximityCurrentData.getText().split(" ")[0]));

            final ObservableList<Map<String, Double>> accelerometerValues = this.accelerometerCurrentData.getItems();
            if (!accelerometerValues.isEmpty())
                this.accelerometerPreviousData.getItems().add(0, accelerometerValues.get(0));

            if (!this.cameraCurrentData.getText().isEmpty())
                this.cameraPreviousData.getItems().add(0,
                        Long.valueOf(this.cameraCurrentData.getText().split(" ")[0]));

            this.proximityCurrentData.setText(dataRead.getProximity() + " cm");

            accelerometerValues.clear();
            accelerometerValues.add(dataRead.getAccelerometerData());

            this.cameraCurrentData.setText(dataRead.getCameraData() + " bytes");
        });
    }

    private void onWarning(final WarningSituation warningSituation) {
        Platform.runLater(() -> {
            this.currentSituationLabel.setText(warningSituation.getType().toString());
            this.currentSituationLabel.setStyle("-fx-text-fill: orange;");
        });
    }

    private void onCritical(final CriticalSituation criticalSituation) {
        Platform.runLater(() -> {
            this.currentSituationLabel.setText(criticalSituation.getType().toString());
            this.currentSituationLabel.setStyle("-fx-text-fill: red;");
        });
    }

    private void onStatusChanged(final StatusChanged statusEvent) {
        Platform.runLater(() -> {
            this.deliveryStatusLabel.setText(statusEvent.getStatus());
            switch (statusEvent.getStatus()) {
                case MqttMessageValueConstants.DELIVERING_MESSAGE:
                    this.haltButton.setDisable(false);
                    this.switchMode.setDisable(false);
                    this.sendSaveDeliveryMessage(statusEvent.getStatus());
                    break;
                case MqttMessageValueConstants.DELIVERY_SUCCESSFUL_MESSAGE:
                    this.deliveryStatusLabel.setStyle("-fx-text-fill: green;");
                    this.proceedButton.setDisable(true);
                    this.haltButton.setDisable(true);
                    this.recallButton.setDisable(false);
                    this.sendSaveDeliveryMessage(statusEvent.getStatus());
                    break;
                case MqttMessageValueConstants.DELIVERY_FAILED_MESSAGE:
                    this.deliveryStatusLabel.setStyle("-fx-text-fill: red;");
                    this.proceedButton.setDisable(true);
                    this.haltButton.setDisable(true);
                    this.recallButton.setDisable(false);
                    this.sendSaveDeliveryMessage(statusEvent.getStatus());
                    break;
                case MqttMessageValueConstants.RETURN_ACKNOWLEDGEMENT_MESSAGE:
                    this.deliveryStatusLabel.setStyle("-fx-text-fill: black;");
                    this.proceedButton.setDisable(true);
                    this.haltButton.setDisable(false);
                    break;
                case MqttMessageValueConstants.RETURNED_ACKNOWLEDGEMENT_MESSAGE:
                    this.deliveryStatusLabel.setStyle("-fx-text-fill: cyan;");
                    DialogUtils.showInfoDialog("Drone successfully returned.",
                            ((Stage) this.accordion.getScene().getWindow())::close);
                    break;
                default:
            }
        });
    }

    private void backOnStandardSituation(final @NotNull StandardSituation standardSituation) {
        Platform.runLater(() -> {
            this.currentSituationLabel.setText(standardSituation.toString());
            this.currentSituationLabel.setStyle("-fx-text-fill: black;");
        });
    }

    @FXML
    private void recallDrone() {
        ServiceHelper.postJson(ServiceHelper.Operation.CALL_BACK,
                new JsonObject().put(ServiceHelper.ORDER_ID_KEY, this.orderId));
        this.recallButton.setDisable(true);
    }

    @FXML
    private void proceed() {
        this.monitoringService.proceed();
        this.proceedButton.setDisable(true);
        this.haltButton.setDisable(false);
    }

    @FXML
    private void halt() {
        this.monitoringService.halt();
        this.haltButton.setDisable(true);
        this.proceedButton.setDisable(false);
    }

    private void sendSaveDeliveryMessage(final String status) {
        final JsonObject body = new JsonObject();
        body.put(ServiceHelper.ORDER_ID_KEY, this.orderId);
        body.put(ServiceHelper.STATE_KEY, status);
        ServiceHelper.postJson(ServiceHelper.Operation.SAVE_DELIVERY, body)
                .onSuccess(ignored -> DomainEvents.raise(new OrdersUpdate()));
    }
}
