/*
 * Copyright (c) 2021-2022, Mirko Felice & Maxim Derevyanchenko. All rights reserved.
 * Licensed under the MIT license. See LICENSE file in the project root for details.
 */

package io.github.dronesecurity.dronesystem.drone;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.dronesecurity.dronesystem.drone.report.DroneReportService;
import io.github.dronesecurity.dronesystem.drone.report.NegligenceReport;
import io.github.dronesecurity.lib.*;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.slf4j.LoggerFactory;
import software.amazon.awssdk.crt.mqtt.MqttMessage;

import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Service providing data about drone status and its sensors.
 */
public class DroneService {

    private static final String JSON_ERROR_MESSAGE = "Can NOT read json correctly.";
    private static final long TRAVELING_TIME = 5000;
    private static final long CLIENT_WAITING_TIME = 1000;
    private static final long ANALIZER_SLEEP_DURATION = 500;
    private static final int RANDOM_GENERATION_RANGE = 100;
    private static final int SUCCESS_PERCENTAGE = 70;

    //Drone
    private final Drone drone;
    private final DataAnalyzer dataAnalyzer;
    private final Thread dataMonitoringAgent;
    private final SecureRandom randomGenerator;
    private final CountDownLatch latch;
    private final ScheduledExecutorService executor;

    // Connection
    private Double proximitySensorData;
    private Map<String, Double> accelerometerSensorData;
    private Byte[] cameraSensorData;

    private String currentOrderId;
    private String currentCourier;

    // Reporting
    private final DroneReportService droneReportService;
    private AlertLevel currentProximityAlertLevel;
    private AlertLevel currentAccelerometerAlertLevel;

    /**
     * Constructs the drone to be observed by this drone service.
     */
    public DroneService() {
        this.drone = new Drone();
        this.dataMonitoringAgent = this.initDataMonitoringAgent();
        this.dataAnalyzer = new DataAnalyzer();
        this.randomGenerator = new SecureRandom();
        this.latch = new CountDownLatch(1);
        this.executor = Executors.newScheduledThreadPool(2);
        this.droneReportService = new DroneReportService();
        this.currentProximityAlertLevel = AlertLevel.NONE;
        this.currentAccelerometerAlertLevel = AlertLevel.NONE;
    }

    /**
     * Waits for someone who wants to perform delivery.
     */
    public void waitForDeliveryAssignment() {
        this.dataMonitoringAgent.start();
        Connection.getInstance().subscribe(MqttTopicConstants.ORDER_TOPIC, msg -> {
            try {
                final JsonNode json = new ObjectMapper().readTree(new String(msg.getPayload(), StandardCharsets.UTF_8));
                if (MqttMessageValueConstants.PERFORM_DELIVERY_MESSAGE
                        .equals(json.get(MqttMessageParameterConstants.SYNC_PARAMETER).asText())) {
                    this.currentOrderId = json.get(MqttMessageParameterConstants.ORDER_ID_PARAMETER).asText();
                    this.currentCourier = json.get(MqttMessageParameterConstants.COURIER_PARAMETER).asText();
                    new Thread(this::startDrone).start();
                    final Connection connection = Connection.getInstance();
                    connection.subscribe(MqttTopicConstants.CONTROL_TOPIC + this.currentOrderId,
                            this::control);
                    connection.unsubscribe(MqttTopicConstants.ORDER_TOPIC);
                }
            } catch (JsonProcessingException e) {
                LoggerFactory.getLogger(getClass()).error(JSON_ERROR_MESSAGE, e);
            }
        });
    }

    private void startDrone() {
        this.drone.activate();
        this.latch.countDown();
        this.simulateDroneLifecycle();
    }

    private void stopDrone() {
        this.drone.deactivate();
        this.executor.shutdownNow();
        Connection.getInstance().closeConnection();
    }

    //Simulates drone lifecycle sending its status to aws with 70% of successful delivers.
    private void simulateDroneLifecycle() {
        PublishHelper.publishCurrentStatus(MqttMessageValueConstants.DELIVERING_MESSAGE, this.currentOrderId);
        this.executor.schedule(() -> {
            final int choice = this.randomGenerator.nextInt(RANDOM_GENERATION_RANGE - 1);
            if (choice < SUCCESS_PERCENTAGE)
                PublishHelper.publishCurrentStatus(MqttMessageValueConstants.DELIVERY_SUCCESSFUL_MESSAGE,
                        this.currentOrderId);
            else
                PublishHelper.publishCurrentStatus(MqttMessageValueConstants.DELIVERY_FAILED_MESSAGE,
                        this.currentOrderId);
        }, TRAVELING_TIME + CLIENT_WAITING_TIME, TimeUnit.MILLISECONDS);

        Connection.getInstance().subscribe(MqttTopicConstants.ORDER_TOPIC + this.currentOrderId, msg -> {
            try {
                final JsonNode json = new ObjectMapper().readTree(new String(msg.getPayload(), StandardCharsets.UTF_8));
                if (MqttMessageValueConstants.DRONE_CALLBACK_MESSAGE
                        .equals(json.get(MqttMessageParameterConstants.SYNC_PARAMETER).asText())) {
                    PublishHelper.publishCurrentStatus(MqttMessageValueConstants.RETURN_ACKNOWLEDGEMENT_MESSAGE,
                            this.currentOrderId);
                    this.executor.schedule(this::stopDrone, TRAVELING_TIME, TimeUnit.MILLISECONDS);
                }
            } catch (JsonProcessingException e) {
                LoggerFactory.getLogger(getClass()).error(JSON_ERROR_MESSAGE, e);
            }
        });
    }

    @Contract(" -> new")
    private @NotNull Thread initDataMonitoringAgent() {
        return new Thread(() -> {
            try {
                this.latch.await();
                this.executor.scheduleWithFixedDelay(() -> {
                    this.drone.readAllData();

                    this.proximitySensorData = this.drone.getProximitySensorData();
                    this.accelerometerSensorData =
                            new DataProcessor().processAccelerometer(this.drone.getAccelerometerSensorData());
                    this.cameraSensorData = this.drone.getCameraSensorData();
                    PublishHelper.publishData(
                            this.currentOrderId,
                            this.proximitySensorData,
                            this.accelerometerSensorData,
                            this.cameraSensorData);

                    this.analyzeData();
                }, 0, ANALIZER_SLEEP_DURATION, TimeUnit.MILLISECONDS);
            } catch (InterruptedException e) {
                LoggerFactory.getLogger(getClass()).info(e.getMessage());
                Thread.currentThread().interrupt();
            }
        });
    }

    private void analyzeData() {
        this.analyzeProximity();
        this.analyzeAccelerometer();
    }

    private void analyzeProximity() {
        final AlertLevel previous = this.currentProximityAlertLevel;
        this.currentProximityAlertLevel = this.dataAnalyzer
                .checkProximitySensorDataAlertLevel(this.proximitySensorData);
        if (this.currentProximityAlertLevel != previous) {
            PublishHelper.publishCurrentAlertLevel(this.currentOrderId, this.currentProximityAlertLevel,
                    AlertType.DISTANCE);
            if (this.currentProximityAlertLevel == AlertLevel.CRITICAL) {
                this.drone.halt();
                this.reportNegligence();
            }
        }
    }

    private void analyzeAccelerometer() {
        final AlertLevel previous = this.currentAccelerometerAlertLevel;
        this.currentAccelerometerAlertLevel = this.dataAnalyzer
                .checkAccelerometerDataAlertLevel(this.accelerometerSensorData);
        if (this.currentAccelerometerAlertLevel != previous) {
            PublishHelper.publishCurrentAlertLevel(this.currentOrderId, this.currentAccelerometerAlertLevel,
                    AlertType.ANGLE);
            if (this.currentAccelerometerAlertLevel == AlertLevel.CRITICAL) {
                this.drone.halt();
                this.reportNegligence();
            }
        }
    }

    private void reportNegligence() {
        final NegligenceReport report = new NegligenceReport(
                this.currentCourier,
                this.proximitySensorData,
                this.accelerometerSensorData,
                this.cameraSensorData,
                this.currentOrderId);
        this.droneReportService.reportsNegligence(report);
    }

    private void control(final @NotNull MqttMessage message) {
        try {
            final JsonNode json = new ObjectMapper().readTree(new String(message.getPayload(), StandardCharsets.UTF_8));
            if (json.has(MqttMessageParameterConstants.MODE_PARAMETER)) {
                final String mode = json.get(MqttMessageParameterConstants.MODE_PARAMETER).asText();
                if (MqttMessageValueConstants.AUTOMATIC_MODE_MESSAGE.equals(mode))
                    this.drone.changeMode(DrivingMode.AUTOMATIC);
                else if (MqttMessageValueConstants.MANUAL_MODE_MESSAGE.equals(mode))
                    this.drone.changeMode(DrivingMode.MANUAL);
            } else if (json.has(MqttMessageParameterConstants.MOVE_PARAMETER)) {
                final String move = json.get(MqttMessageParameterConstants.MOVE_PARAMETER).asText();
                if (MqttMessageValueConstants.PROCEED_MESSAGE.equals(move))
                    this.drone.proceed();
                else if (MqttMessageValueConstants.HALT_MESSAGE.equals(move)) {
                    this.drone.halt();
                }
            }
        } catch (JsonProcessingException e) {
            LoggerFactory.getLogger(getClass()).error(JSON_ERROR_MESSAGE, e);
        }
    }
}
