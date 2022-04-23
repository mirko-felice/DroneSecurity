package it.unibo.dronesecurity.dronesystem.drone;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import it.unibo.dronesecurity.dronesystem.drone.report.DroneReportService;
import it.unibo.dronesecurity.dronesystem.drone.report.NegligenceReport;
import it.unibo.dronesecurity.lib.*;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.slf4j.LoggerFactory;

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
    private Double cameraSensorData;

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
                }
            } catch (JsonProcessingException e) {
                LoggerFactory.getLogger(getClass()).error("Can NOT read json correctly.", e);
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

        Connection.getInstance().subscribe(MqttTopicConstants.ORDER_TOPIC, msg -> {
            try {
                final JsonNode json = new ObjectMapper().readTree(new String(msg.getPayload(), StandardCharsets.UTF_8));
                if (MqttMessageValueConstants.DRONE_CALLBACK_MESSAGE
                        .equals(json.get(MqttMessageParameterConstants.SYNC_PARAMETER).asText())) {
                    PublishHelper.publishCurrentStatus(MqttMessageValueConstants.RETURN_ACKNOWLEDGEMENT_MESSAGE,
                            this.currentOrderId);
                    this.executor.schedule(this::stopDrone, TRAVELING_TIME, TimeUnit.MILLISECONDS);
                }
            } catch (JsonProcessingException e) {
                LoggerFactory.getLogger(getClass()).error("Can NOT read json correctly.", e);
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
                    this.accelerometerSensorData = this.drone.getAccelerometerSensorData();
                    this.cameraSensorData = this.drone.getCameraSensorData();
                    PublishHelper.publishData(
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
        this.analyzeCamera();
    }

    private void analyzeProximity() {
        final AlertLevel previous = this.currentProximityAlertLevel;
        this.currentProximityAlertLevel = this.dataAnalyzer
                .checkProximitySensorDataAlertLevel(this.proximitySensorData);
        if (this.currentProximityAlertLevel != previous) {
            PublishHelper.publishCurrentAlertLevel(this.currentProximityAlertLevel, AlertType.DISTANCE);
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
            PublishHelper.publishCurrentAlertLevel(this.currentAccelerometerAlertLevel, AlertType.ANGLE);
            if (this.currentAccelerometerAlertLevel == AlertLevel.CRITICAL) {
                this.drone.halt();
                this.reportNegligence();
            }
        }
    }

    private void analyzeCamera() {
        LoggerFactory.getLogger(this.getClass()).debug("{}", this.cameraSensorData);
    }

    private void reportNegligence() {
        final NegligenceReport report = new NegligenceReport(
                this.currentCourier,
                this.proximitySensorData,
                this.accelerometerSensorData);
        this.droneReportService.reportsNegligence(report);
    }
}
