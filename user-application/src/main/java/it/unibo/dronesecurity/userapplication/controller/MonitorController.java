package it.unibo.dronesecurity.userapplication.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import it.unibo.dronesecurity.lib.*;
import it.unibo.dronesecurity.userapplication.drone.monitoring.UserMonitoringService;
import it.unibo.dronesecurity.userapplication.events.*;
import javafx.application.Platform;
import javafx.beans.property.SimpleObjectProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

import java.net.URL;
import java.util.Map;
import java.util.ResourceBundle;

/**
 * Controller dedicated to monitoring delivery.
 */
public final class MonitorController implements Initializable {

    private static final DomainEvents<DataReadEvent> DATA_READER_DOMAIN_EVENTS = new DomainEvents<>();
    private static final DomainEvents<WarningEvent> WARNING_DOMAIN_EVENTS = new DomainEvents<>();
    private static final DomainEvents<CriticalEvent> CRITICAL_DOMAIN_EVENTS = new DomainEvents<>();
    private static final DomainEvents<StatusChangedEvent> STATUS_CHANGED_DOMAIN_EVENTS = new DomainEvents<>();

    private static final String STATUS_STRING = "Status: ";
    private static final String STARTING_STRING = "starting";

    private final transient UserMonitoringService monitoringService;

    @FXML private transient Label statusLabel;
    @FXML private transient Button recallButton;
    @FXML private transient Label proximityCurrentData;
    @FXML private transient TableView<Map<String, Double>> accelerometerCurrentData;
    @FXML private transient Label cameraCurrentData;

    @FXML private transient TableView<Double> proximityPreviousData;
    @FXML private transient TableView<Map<String, Double>> accelerometerPreviousData;
    @FXML private transient TableView<Double> cameraPreviousData;

    @FXML private transient TableColumn<Double, Double> proximityPreviousDataColumn;
    @FXML private transient TableColumn<Map<String, Double>, Double> accelerometerPreviousXDataColumn;
    @FXML private transient TableColumn<Map<String, Double>, Double> accelerometerPreviousYDataColumn;
    @FXML private transient TableColumn<Map<String, Double>, Double> accelerometerPreviousZDataColumn;
    @FXML private transient TableColumn<Double, Double> cameraPreviousDataColumn;

    @FXML private transient TableColumn<Map<String, Double>, Double> currentAccelerometerXValue;
    @FXML private transient TableColumn<Map<String, Double>, Double> currentAccelerometerYValue;
    @FXML private transient TableColumn<Map<String, Double>, Double> currentAccelerometerZValue;

    /**
     * Build the Controller to interact with services.
     */
    public MonitorController() {
        this.monitoringService = new UserMonitoringService();
    }

    @Override
    public void initialize(final URL location, final ResourceBundle resources) {
        this.statusLabel.setText(STATUS_STRING + STARTING_STRING);
        this.recallButton.setDisable(true);
        DATA_READER_DOMAIN_EVENTS.register(this::onDataRead);
        WARNING_DOMAIN_EVENTS.register(this::onWarning);
        CRITICAL_DOMAIN_EVENTS.register(this::onCritical);
        STATUS_CHANGED_DOMAIN_EVENTS.register(this::onStatusChanged);

        this.monitoringService.subscribeToDataReading(DATA_READER_DOMAIN_EVENTS);
        this.monitoringService.subscribeToWarning(WARNING_DOMAIN_EVENTS);
        this.monitoringService.subscribeToCritical(CRITICAL_DOMAIN_EVENTS);
        this.monitoringService.subscribeToStatusChanges(STATUS_CHANGED_DOMAIN_EVENTS);

        this.proximityPreviousDataColumn.setCellValueFactory(cell ->
                new SimpleObjectProperty<>(cell.getValue()));
        this.accelerometerPreviousXDataColumn.setCellValueFactory(cell ->
                new SimpleObjectProperty<>(cell.getValue()
                        .get(MqttMessageParameterConstants.ACCELEROMETER_X_PARAMETER)));
        this.accelerometerPreviousYDataColumn.setCellValueFactory(cell ->
                new SimpleObjectProperty<>(cell.getValue()
                        .get(MqttMessageParameterConstants.ACCELEROMETER_Y_PARAMETER)));
        this.accelerometerPreviousZDataColumn.setCellValueFactory(cell ->
                new SimpleObjectProperty<>(cell.getValue()
                        .get(MqttMessageParameterConstants.ACCELEROMETER_Z_PARAMETER)));
        this.cameraPreviousDataColumn.setCellValueFactory(cell ->
                new SimpleObjectProperty<>(cell.getValue()));

        this.currentAccelerometerXValue.setCellValueFactory(cell ->
                new SimpleObjectProperty<>(cell.getValue()
                        .get(MqttMessageParameterConstants.ACCELEROMETER_X_PARAMETER)));
        this.currentAccelerometerYValue.setCellValueFactory(cell ->
                new SimpleObjectProperty<>(cell.getValue()
                        .get(MqttMessageParameterConstants.ACCELEROMETER_Y_PARAMETER)));
        this.currentAccelerometerZValue.setCellValueFactory(cell ->
                new SimpleObjectProperty<>(cell.getValue()
                        .get(MqttMessageParameterConstants.ACCELEROMETER_Z_PARAMETER)));

    }

    private void onDataRead(final DataReadEvent dataReadEvent) {
        Platform.runLater(() -> {
            if (!this.proximityCurrentData.getText().isEmpty())
                this.proximityPreviousData.getItems().add(0, Double.parseDouble(this.proximityCurrentData.getText()));

            if (!this.accelerometerCurrentData.getItems().isEmpty())
                this.accelerometerPreviousData.getItems().add(0, this.accelerometerCurrentData.getItems().get(0));

            if (!this.cameraCurrentData.getText().isEmpty())
                this.cameraPreviousData.getItems().add(0, Double.parseDouble(this.cameraCurrentData.getText()));

            this.proximityCurrentData.setText(String.valueOf(dataReadEvent.getProximity()));

            this.accelerometerCurrentData.getItems().clear();
            this.accelerometerCurrentData.getItems().add(dataReadEvent.getAccelerometerData());

            this.cameraCurrentData.setText(String.valueOf(dataReadEvent.getCameraData()));
        });
    }

    private void onWarning(final WarningEvent warningEvent) {
        Platform.runLater(() -> {
            CustomLogger.getLogger(getClass().getName()).info(warningEvent.getMsg());
        });
    }

    private void onCritical(final CriticalEvent criticalEvent) {
        Platform.runLater(() -> {
            CustomLogger.getLogger(getClass().getName()).info(criticalEvent.getMsg());
        });
    }

    private void onStatusChanged(final StatusChangedEvent statusEvent) {
        Platform.runLater(() -> {
            this.statusLabel.setText(STATUS_STRING + statusEvent.getStatus());
            if (MqttMessageValueConstants.DELIVERY_SUCCESSFUL_MESSAGE.equals(statusEvent.getStatus())
                    || MqttMessageValueConstants.DELIVERY_FAILED_MESSAGE.equals(statusEvent.getStatus()))
                this.recallButton.setDisable(false);
        });
    }

    @FXML
    private void recallDrone() {
        final JsonNode recallMessage = new ObjectMapper().createObjectNode()
                .put(MqttMessageParameterConstants.MESSAGE_PARAMETER,
                        MqttMessageValueConstants.DRONE_CALLBACK_MESSAGE);
        Connection.getInstance().publish(MqttTopicConstants.ORDER_TOPIC, recallMessage);
        this.recallButton.setDisable(true);
    }
}
