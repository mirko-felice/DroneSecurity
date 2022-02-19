package it.unibo.dronesecurity.dronesystem.drone;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import it.unibo.dronesecurity.lib.Connection;
import it.unibo.dronesecurity.lib.MqttMessageParameterConstants;
import it.unibo.dronesecurity.lib.MqttMessageValueConstants;
import it.unibo.dronesecurity.lib.MqttTopicConstants;
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

    private static final int TRAVELING_TIME = 5000;
    private static final int CLIENT_WAITING_TIME = 1000;
    private static final int ANALIZER_SLEEP_DURATION = 500;
    private static final int RANDOM_GENERATION_RANGE = 100;
    private static final int SUCCESS_PERCENTAGE = 70;

    //Drone
    private final transient Drone drone;
    private final transient DataAnalyzer dataAnalyzer;
    private final transient Thread dataMonitoringAgent;
    private final transient SecureRandom randomGenerator;
    private final transient CountDownLatch latch;
    private final transient ScheduledExecutorService executor;

    // Connection
    private transient Double proximitySensorData;
    private transient Map<String, Double> accelerometerSensorData;
    private transient Double cameraSensorData;

    private transient String currentOrderId;

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
                        .equals(json.get(MqttMessageParameterConstants.MESSAGE_PARAMETER).asText())) {
                    this.currentOrderId = json.get(MqttMessageParameterConstants.ORDER_ID_PARAMETER).asText();
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
        this.publishCurrentStatus(MqttMessageValueConstants.DELIVERING_MESSAGE);
        this.executor.schedule(() -> {
            final int choice = this.randomGenerator.nextInt(RANDOM_GENERATION_RANGE - 1);
            if (choice < SUCCESS_PERCENTAGE)
                this.publishCurrentStatus(MqttMessageValueConstants.DELIVERY_SUCCESSFUL_MESSAGE);
            else
                this.publishCurrentStatus(MqttMessageValueConstants.DELIVERY_FAILED_MESSAGE);
        }, TRAVELING_TIME + CLIENT_WAITING_TIME, TimeUnit.MILLISECONDS);

        Connection.getInstance().subscribe(MqttTopicConstants.ORDER_TOPIC, msg -> {
            try {
                final JsonNode json = new ObjectMapper().readTree(new String(msg.getPayload(), StandardCharsets.UTF_8));
                if (MqttMessageValueConstants.DRONE_CALLBACK_MESSAGE
                        .equals(json.get(MqttMessageParameterConstants.MESSAGE_PARAMETER).asText())) {
                    this.publishCurrentStatus(MqttMessageValueConstants.RETURN_ACKNOWLEDGEMENT_MESSAGE);
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
                    this.publishData();

                    this.analyzeData();
                }, 0, ANALIZER_SLEEP_DURATION, TimeUnit.MILLISECONDS);
            } catch (InterruptedException e) {
                LoggerFactory.getLogger(getClass()).info(e.getMessage());
                //Restart Drone if thread interrupted
//                this.startDrone();
            }
        });
    }

    private void analyzeData() {
        switch (this.dataAnalyzer.checkProximitySensorDataAlertLevel(this.proximitySensorData)) {
            case CRITICAL:
                this.drone.halt();
                this.publishAlert(MqttTopicConstants.CRITICAL_TOPIC,
                        MqttMessageValueConstants.CRITICAL_PROXIMITY_MESSAGE);
                break;
            case WARNING:
                this.publishAlert(MqttTopicConstants.WARNING_TOPIC,
                        MqttMessageValueConstants.WARNING_PROXIMITY_MESSAGE);
                break;
            default:
        }
        switch (this.dataAnalyzer.checkAccelerometerDataAlertLevel(this.accelerometerSensorData)) {
            case CRITICAL:
                this.drone.halt();
                this.publishAlert(MqttTopicConstants.CRITICAL_TOPIC,
                        MqttMessageValueConstants.CRITICAL_ANGLE_MESSAGE);
                break;
            case WARNING:
                this.publishAlert(MqttTopicConstants.WARNING_TOPIC,
                        MqttMessageValueConstants.WARNING_ANGLE_MESSAGE);
                break;
            default:
        }
    }

    private void publishData() {
        final ObjectMapper mapper = new ObjectMapper();
        final ObjectNode mapJson = mapper.createObjectNode();
        mapJson.put(MqttMessageParameterConstants.PROXIMITY_PARAMETER, this.proximitySensorData);
        final ObjectNode accelerometerValues = mapper.createObjectNode();
        this.accelerometerSensorData.forEach(accelerometerValues::put);
        mapJson.set(MqttMessageParameterConstants.ACCELEROMETER_PARAMETER, accelerometerValues);
        mapJson.put(MqttMessageParameterConstants.CAMERA_PARAMETER, this.cameraSensorData);

        Connection.getInstance().publish(MqttTopicConstants.DATA_TOPIC, mapJson);
    }

    private void publishAlert(final String topic, final @NotNull String message) {
        final ObjectNode payload = new ObjectMapper().createObjectNode();
        payload.put(MqttMessageParameterConstants.MESSAGE_PARAMETER, message);
        Connection.getInstance().publish(topic, payload);
    }

    private void publishCurrentStatus(final String status) {
        final ObjectNode payload = new ObjectMapper().createObjectNode();
        payload.put(MqttMessageParameterConstants.ORDER_ID_PARAMETER, this.currentOrderId);
        payload.put(MqttMessageParameterConstants.STATUS_PARAMETER, status);
        Connection.getInstance().publish(MqttTopicConstants.LIFECYCLE_TOPIC, payload);
    }
}
