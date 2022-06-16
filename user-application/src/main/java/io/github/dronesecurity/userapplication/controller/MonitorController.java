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
import io.github.dronesecurity.userapplication.reporting.negligence.services.NegligenceReportService;
import io.github.dronesecurity.userapplication.shipping.courier.utilities.ShippingServiceHelper;
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
import java.util.function.Consumer;

/**
 * Controller dedicated to monitoring delivery.
 */
public final class MonitorController implements Initializable {

    private final UserMonitoringService monitoringService;
    private final CourierNegligenceReportService negligenceReportService;
    private final long orderId;

    private final Consumer<NewNegligence> newNegligenceHandler;
    private final Consumer<CriticalSituation> criticalSituationHandler;
    private final Consumer<DangerousSituation> dangerousSituationHandler;
    private final Consumer<StableSituation> stableSituationHandler;
    private final Consumer<DataRead> dataReadHandler;
    private final Consumer<StatusChanged> statusChangedHandler;
    private final Consumer<DroneMovingStateChangeEvent> movingStateChangedHandler;

    @FXML private Label proximityCurrentData;
    @FXML private TableView<Map<String, Integer>> accelerometerCurrentData;
    @FXML private Label cameraCurrentData;

    @FXML private TableView<Double> proximityPreviousData;
    @FXML private TableView<Map<String, Integer>> accelerometerPreviousData;
    @FXML private TableView<Long> cameraPreviousData;

    @FXML private TableColumn<Double, Double> proximityPreviousDataColumn;
    @FXML private TableColumn<Map<String, Integer>, Integer> accelerometerPreviousXDataColumn;
    @FXML private TableColumn<Map<String, Integer>, Integer> accelerometerPreviousYDataColumn;
    @FXML private TableColumn<Map<String, Integer>, Integer> accelerometerPreviousZDataColumn;
    @FXML private TableColumn<Long, Long> cameraPreviousDataColumn;

    @FXML private TableColumn<Map<String, Integer>, Integer> currentAccelerometerXValue;
    @FXML private TableColumn<Map<String, Integer>, Integer> currentAccelerometerYValue;
    @FXML private TableColumn<Map<String, Integer>, Integer> currentAccelerometerZValue;

    // Controls
    @FXML private Accordion accordion;
    @FXML private TitledPane controlsPane;
    @FXML private ToggleSwitch switchMode;
    @FXML private Button proceedButton;
    @FXML private Button haltButton;
    @FXML private Label deliveryStatusLabel;
    @FXML private Label droneStateLabel;
    @FXML private Label currentSituationLabel;
    @FXML private Button recallButton;

    /**
     * Build the Controller to interact with services.
     * @param orderId order identifier to monitoring
     */
    public MonitorController(final long orderId) {
        this.orderId = orderId;
        this.monitoringService = new UserMonitoringService(orderId);
        this.negligenceReportService = new NegligenceReportService();
        this.newNegligenceHandler = this::onNewNegligence;

        this.criticalSituationHandler = this::onCriticalSituation;
        this.dangerousSituationHandler = this::onDangerousSituation;
        this.stableSituationHandler = this::backOnStableSituation;
        this.dataReadHandler = this::onDataRead;
        this.statusChangedHandler = this::onStatusChanged;
        this.movingStateChangedHandler = this::onMovingStateChanged;
    }

    @Override
    public void initialize(final URL location, final ResourceBundle resources) {
        this.accordion.setExpandedPane(this.controlsPane);

        DomainEvents.register(NewNegligence.class, this.newNegligenceHandler);
        this.negligenceReportService.subscribeToNewNegligence();

        DomainEvents.register(CriticalSituation.class, this.criticalSituationHandler);
        DomainEvents.register(DangerousSituation.class, this.dangerousSituationHandler);
        DomainEvents.register(StableSituation.class, this.stableSituationHandler);
        DomainEvents.register(DataRead.class, this.dataReadHandler);
        DomainEvents.register(StatusChanged.class, this.statusChangedHandler);
        DomainEvents.register(DroneMovingStateChangeEvent.class, this.movingStateChangedHandler);

        this.monitoringService.subscribeToDataRead();
        this.monitoringService.subscribeToOrderStatusChange();
        this.monitoringService.subscribeToAlerts();

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
            final JsonObject body = new JsonObject().put(ShippingServiceHelper.ORDER_ID_KEY, this.orderId);
            if (Boolean.TRUE.equals(isAutomatic))
                ShippingServiceHelper.postJson(ShippingServiceHelper.Operation.CHANGE_MODE,
                        body.put(ShippingServiceHelper.DRIVING_MODE_KEY, DrivingMode.AUTOMATIC.toString()));
            else
                ShippingServiceHelper.postJson(ShippingServiceHelper.Operation.CHANGE_MODE,
                        body.put(ShippingServiceHelper.DRIVING_MODE_KEY, DrivingMode.MANUAL.toString()));
            this.checkButtons();
        });
    }

    private void onNewNegligence(final NewNegligence newNegligence) {
        Platform.runLater(() -> DialogUtils.showInfoNotification("INFO",
                "You have committed a negligence. The drone has been halted for security purpose."
                        + "\nMaintainer " + newNegligence.getReport().assignedTo()
                        + " will take care of this. Go to the 'reports' window to show more information about it.",
                this.switchMode.getScene().getWindow()));
    }

    private void onDataRead(final DataRead dataRead) {
        Platform.runLater(() -> {
            if (!this.proximityCurrentData.getText().isEmpty())
                this.proximityPreviousData.getItems().add(0,
                        Double.valueOf(this.proximityCurrentData.getText().split(" ")[0]));

            final ObservableList<Map<String, Integer>> accelerometerValues = this.accelerometerCurrentData.getItems();
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

    private void onDangerousSituation(final DangerousSituation dangerousSituation) {
        Platform.runLater(() -> {
            this.currentSituationLabel.setText(dangerousSituation.toString());
            this.currentSituationLabel.setStyle("-fx-text-fill: orange;");
            this.checkButtons();
        });
    }

    private void onCriticalSituation(final CriticalSituation criticalSituation) {
        Platform.runLater(() -> {
            this.currentSituationLabel.setText(criticalSituation.toString());
            this.currentSituationLabel.setStyle("-fx-text-fill: red;");
            this.checkButtons();
        });
    }

    private void onStatusChanged(final StatusChanged statusEvent) {
        Platform.runLater(() -> {
            this.deliveryStatusLabel.setText(statusEvent.getStatus());
            this.checkButtons();
            switch (statusEvent.getStatus()) {
                case MqttMessageValueConstants.DELIVERING_MESSAGE:
                    this.sendSaveDeliveryMessage(statusEvent.getStatus());
                    break;
                case MqttMessageValueConstants.DELIVERY_SUCCESSFUL_MESSAGE:
                    this.deliveryStatusLabel.setStyle("-fx-text-fill: green;");
                    this.sendSaveDeliveryMessage(statusEvent.getStatus());
                    break;
                case MqttMessageValueConstants.DELIVERY_FAILED_MESSAGE:
                    this.deliveryStatusLabel.setStyle("-fx-text-fill: red;");
                    this.sendSaveDeliveryMessage(statusEvent.getStatus());
                    break;
                case MqttMessageValueConstants.RETURNING_ACKNOWLEDGEMENT_MESSAGE:
                    this.deliveryStatusLabel.setStyle("-fx-text-fill: black;");
                    break;
                case MqttMessageValueConstants.RETURNED_ACKNOWLEDGEMENT_MESSAGE:
                    this.deliveryStatusLabel.setStyle("-fx-text-fill: cyan;");
                    DialogUtils.showInfoDialog("Drone successfully returned.", () -> {
                        DomainEvents.unregister(NewNegligence.class, this.newNegligenceHandler);
                        DomainEvents.unregister(CriticalSituation.class, this.criticalSituationHandler);
                        DomainEvents.unregister(DangerousSituation.class, this.dangerousSituationHandler);
                        DomainEvents.unregister(StableSituation.class, this.stableSituationHandler);
                        DomainEvents.unregister(DataRead.class, this.dataReadHandler);
                        DomainEvents.unregister(StatusChanged.class, this.statusChangedHandler);
                        DomainEvents.unregister(DroneMovingStateChangeEvent.class, this.movingStateChangedHandler);

                        ((Stage) this.accordion.getScene().getWindow()).close();
                    });
                    break;
                default:
            }
        });
    }

    private void backOnStableSituation(final @NotNull StableSituation stableSituation) {
        Platform.runLater(() -> {
            this.currentSituationLabel.setText(stableSituation.toString());
            this.currentSituationLabel.setStyle("-fx-text-fill: black;");
            this.checkButtons();
        });
    }

    private void onMovingStateChanged(final DroneMovingStateChangeEvent droneMovingStateChangeEvent) {
        Platform.runLater(() -> {
            this.droneStateLabel.setText(droneMovingStateChangeEvent.getMovingState());
            this.checkButtons();
        });
    }

    @FXML
    private void recallDrone() {
        ShippingServiceHelper.postJson(ShippingServiceHelper.Operation.CALL_BACK,
                new JsonObject().put(ShippingServiceHelper.ORDER_ID_KEY, this.orderId));
        this.recallButton.setDisable(true);
    }

    @FXML
    private void proceed() {
        ShippingServiceHelper.postJson(ShippingServiceHelper.Operation.PROCEED,
                new JsonObject().put(ShippingServiceHelper.ORDER_ID_KEY, this.orderId));
        this.proceedButton.setDisable(true);
        this.haltButton.setDisable(false);
    }

    @FXML
    private void halt() {
        ShippingServiceHelper.postJson(ShippingServiceHelper.Operation.HALT,
                new JsonObject().put(ShippingServiceHelper.ORDER_ID_KEY, this.orderId));
        this.haltButton.setDisable(true);
        this.proceedButton.setDisable(false);
    }

    private void sendSaveDeliveryMessage(final String status) {
        final JsonObject body = new JsonObject();
        body.put(ShippingServiceHelper.ORDER_ID_KEY, this.orderId);
        body.put(ShippingServiceHelper.STATE_KEY, status);
        ShippingServiceHelper.postJson(ShippingServiceHelper.Operation.SAVE_DELIVERY, body)
                .onSuccess(ignored -> DomainEvents.raise(new OrdersUpdate()));
    }

    private void checkButtons() {
        final String currentSituation = this.currentSituationLabel.getText();
        if (this.switchMode.isSelected()) {
            if (SituationConstants.STABLE.equals(currentSituation)) {
                switch (this.deliveryStatusLabel.getText()) {
                    case MqttMessageValueConstants.DELIVERING_MESSAGE:
                    case MqttMessageValueConstants.RETURNING_ACKNOWLEDGEMENT_MESSAGE:
                        if (MqttMessageValueConstants.DRONE_STOPPED_STATE_MESSAGE
                                .equals(this.droneStateLabel.getText())) {
                            this.proceedButton.setDisable(false);
                            this.haltButton.setDisable(true);
                        } else {
                            this.proceedButton.setDisable(true);
                            this.haltButton.setDisable(false);
                        }
                        this.recallButton.setDisable(true);
                        break;
                    case MqttMessageValueConstants.DELIVERY_SUCCESSFUL_MESSAGE:
                    case MqttMessageValueConstants.DELIVERY_FAILED_MESSAGE:
                        this.proceedButton.setDisable(true);
                        this.haltButton.setDisable(true);
                        this.recallButton.setDisable(false);
                        break;
                    case MqttMessageValueConstants.RETURNED_ACKNOWLEDGEMENT_MESSAGE:
                        this.proceedButton.setDisable(true);
                        this.haltButton.setDisable(true);
                        this.recallButton.setDisable(true);
                        break;
                    default:
                }
            } else if (SituationConstants.CRITICAL_ANGLE.equals(currentSituation)
                    || SituationConstants.CRITICAL_DISTANCE.equals(currentSituation)) {
                this.proceedButton.setDisable(true);
                this.haltButton.setDisable(true);
                this.recallButton.setDisable(true);
            }
        } else {
            this.proceedButton.setDisable(true);
            this.haltButton.setDisable(true);
            this.recallButton.setDisable(true);
        }
    }
}
