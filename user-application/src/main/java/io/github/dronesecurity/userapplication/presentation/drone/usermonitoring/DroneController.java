/*
 * Copyright (c) 2021-2022, Mirko Felice & Maxim Derevyanchenko. All rights reserved.
 * Licensed under the MIT license. See LICENSE file in the project root for details.
 */

package io.github.dronesecurity.userapplication.presentation.drone.usermonitoring;

import io.github.dronesecurity.lib.shared.DrivingMode;
import io.github.dronesecurity.lib.connection.MqttMessageValueConstants;
import io.github.dronesecurity.userapplication.application.drone.usermonitoring.UserMonitoringServiceImpl;
import io.github.dronesecurity.userapplication.domain.drone.usermonitoring.events.*;
import io.github.dronesecurity.userapplication.domain.drone.usermonitoring.services.UserMonitoringService;
import io.github.dronesecurity.userapplication.events.DomainEvents;
import io.github.dronesecurity.userapplication.events.SituationConstants;
import io.github.dronesecurity.userapplication.infrastructure.drone.usermonitoring.repo.DataRepositoryImpl;
import io.github.dronesecurity.userapplication.utilities.DialogUtils;
import io.github.dronesecurity.userapplication.utilities.shipping.DroneAPIHelper;
import io.github.dronesecurity.userapplication.utilities.shipping.ShippingAPIHelper;
import io.github.dronesecurity.userapplication.utilities.user.UserAPIHelper;
import io.vertx.core.json.JsonObject;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import org.controlsfx.control.ToggleSwitch;
import org.jetbrains.annotations.NotNull;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.function.Consumer;

/**
 * UI controller that observes delivery status and remotely controls the drone.
 */
public final class DroneController implements Initializable {

    private static final String RED_TEXT = "-fx-text-fill: red;";
    private static final String BLACK_TEXT = "-fx-text-fill: black;";

    private final UserMonitoringService monitoringService;
    private final long orderId;

    private final Consumer<CriticalSituation> criticalSituationHandler;
    private final Consumer<DangerousSituation> dangerousSituationHandler;
    private final Consumer<StableSituation> stableSituationHandler;
    private final Consumer<StatusChanged> statusChangedHandler;
    private final Consumer<MovingStateChanged> movingStateChangedHandler;

    @FXML private ToggleSwitch switchMode;
    @FXML private Button proceedButton;
    @FXML private Button haltButton;
    @FXML private Label deliveryStatusLabel;
    @FXML private Label droneStateLabel;
    @FXML private Label currentSituationLabel;
    @FXML private Button recallButton;

    /**
     * Build the drone controller related to an order.
     * @param orderId order identifier to control
     */
    public DroneController(final long orderId) {
        this.orderId = orderId;
        this.monitoringService = new UserMonitoringServiceImpl(new DataRepositoryImpl());
        this.monitoringService.startOrderMonitoring(orderId);

        this.criticalSituationHandler = this::onCriticalSituation;
        this.dangerousSituationHandler = this::onDangerousSituation;
        this.stableSituationHandler = this::backOnStableSituation;
        this.statusChangedHandler = this::onStatusChanged;
        this.movingStateChangedHandler = this::onMovingStateChanged;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void initialize(final URL location, final ResourceBundle resources) {
        DomainEvents.register(CriticalSituation.class, this.criticalSituationHandler);
        DomainEvents.register(DangerousSituation.class, this.dangerousSituationHandler);
        DomainEvents.register(StableSituation.class, this.stableSituationHandler);
        DomainEvents.register(StatusChanged.class, this.statusChangedHandler);
        DomainEvents.register(MovingStateChanged.class, this.movingStateChangedHandler);

        this.switchMode.selectedProperty().addListener((ignored, unused, isAutomatic) -> {
            final JsonObject body = new JsonObject().put(DroneAPIHelper.ORDER_ID_KEY, this.orderId);
            // TODO check if asLong necessary
            if (Boolean.TRUE.equals(isAutomatic))
                DroneAPIHelper.postJson(DroneAPIHelper.Operation.CHANGE_MODE,
                        body.put(DroneAPIHelper.DRIVING_MODE_KEY, DrivingMode.AUTOMATIC.toString()));
            // TODO check if toString necessary
            else
                DroneAPIHelper.postJson(DroneAPIHelper.Operation.CHANGE_MODE,
                        body.put(DroneAPIHelper.DRIVING_MODE_KEY, DrivingMode.MANUAL.toString()));
            this.setButtonsCorrectly();
        });
    }

    private void onDangerousSituation(final DangerousSituation dangerousSituation) {
        Platform.runLater(() -> {
            this.currentSituationLabel.setText(dangerousSituation.toString());
            this.currentSituationLabel.setStyle("-fx-text-fill: orange;");
            this.setButtonsCorrectly();
        });
    }

    private void onCriticalSituation(final CriticalSituation criticalSituation) {
        Platform.runLater(() -> {
            this.currentSituationLabel.setText(criticalSituation.toString());
            this.currentSituationLabel.setStyle(RED_TEXT);
            this.setButtonsCorrectly();
        });
    }

    private void onStatusChanged(final StatusChanged statusEvent) {
        Platform.runLater(() -> {
            this.deliveryStatusLabel.setText(statusEvent.getStatus());
            this.setButtonsCorrectly();
            this.setNewState(statusEvent.getStatus());
        });
    }

    private void backOnStableSituation(final @NotNull StableSituation stableSituation) {
        Platform.runLater(() -> {
            this.currentSituationLabel.setText(stableSituation.toString());
            this.currentSituationLabel.setStyle(BLACK_TEXT);
            this.setButtonsCorrectly();
        });
    }

    private void onMovingStateChanged(final MovingStateChanged movingStateChanged) {
        Platform.runLater(() -> {
            this.droneStateLabel.setText(movingStateChanged.getMovingState());
            this.setButtonsCorrectly();
        });
    }

    @FXML
    private void recallDrone() {
        DroneAPIHelper.postJson(DroneAPIHelper.Operation.CALL_BACK,
                new JsonObject().put(DroneAPIHelper.ORDER_ID_KEY, this.orderId));
        this.recallButton.setDisable(true);
    }

    @FXML
    private void proceed() {
        DroneAPIHelper.postJson(DroneAPIHelper.Operation.PROCEED,
                new JsonObject().put(DroneAPIHelper.ORDER_ID_KEY, this.orderId));
        this.proceedButton.setDisable(true);
        this.haltButton.setDisable(false);
    }

    @FXML
    private void halt() {
        DroneAPIHelper.postJson(DroneAPIHelper.Operation.HALT,
                new JsonObject().put(DroneAPIHelper.ORDER_ID_KEY, this.orderId));
        this.haltButton.setDisable(true);
        this.proceedButton.setDisable(false);
    }

    private void setButtonsCorrectly() {
        final String currentSituation = this.currentSituationLabel.getText();
        if (this.switchMode.isSelected()) {
            if (SituationConstants.STABLE.equals(currentSituation))
                this.setButtonsWhenStable();
            else if (SituationConstants.CRITICAL_ANGLE.equals(currentSituation)
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

    private void setButtonsWhenStable() {
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
    }

    private void setNewState(final @NotNull String state) {
        switch (state) {
            case MqttMessageValueConstants.DELIVERING_MESSAGE:
                UserAPIHelper.postJson(UserAPIHelper.Operation.REMOVE_DRONE,
                        new JsonObject().put(UserAPIHelper.DRONE_KEY, "TODO")); // TODO
                break;
            case MqttMessageValueConstants.DELIVERY_SUCCESSFUL_MESSAGE:
                this.deliveryStatusLabel.setStyle("-fx-text-fill: green;");
                this.performShippingOperation(ShippingAPIHelper.Operation.SUCCEED_DELIVERY);
                break;
            case MqttMessageValueConstants.DELIVERY_FAILED_MESSAGE:
                this.deliveryStatusLabel.setStyle(RED_TEXT);
                this.performShippingOperation(ShippingAPIHelper.Operation.FAIL_DELIVERY);
                break;
            case MqttMessageValueConstants.RETURNING_ACKNOWLEDGEMENT_MESSAGE:
                this.deliveryStatusLabel.setStyle(BLACK_TEXT);
                break;
            case MqttMessageValueConstants.RETURNED_ACKNOWLEDGEMENT_MESSAGE:
                this.deliveryStatusLabel.setStyle("-fx-text-fill: cyan;");
                UserAPIHelper.postJson(UserAPIHelper.Operation.ADD_DRONE,
                        new JsonObject().put(UserAPIHelper.DRONE_KEY, "TODO")); // TODO
                DialogUtils.showInfoDialog("Drone successfully returned.", () -> {
                    DomainEvents.unregister(CriticalSituation.class, this.criticalSituationHandler);
                    DomainEvents.unregister(DangerousSituation.class, this.dangerousSituationHandler);
                    DomainEvents.unregister(StableSituation.class, this.stableSituationHandler);
                    DomainEvents.unregister(StatusChanged.class, this.statusChangedHandler);
                    DomainEvents.unregister(MovingStateChanged.class, this.movingStateChangedHandler);
                    this.monitoringService.stopOrderMonitoring(this.orderId);
                    ((Stage) this.droneStateLabel.getScene().getWindow()).close();
                });
                break;
            default:
        }
    }

    private void performShippingOperation(final ShippingAPIHelper.Operation operation) {
        final JsonObject body = new JsonObject();
        body.put(ShippingAPIHelper.ORDER_ID_KEY, this.orderId);
        ShippingAPIHelper.postJson(operation, body);
    }
}
