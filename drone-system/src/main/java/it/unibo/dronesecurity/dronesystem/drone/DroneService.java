package it.unibo.dronesecurity.dronesystem.drone;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import it.unibo.dronesecurity.lib.*;

import java.util.Map;
import java.util.Random;

/**
 * Service providing data about drone status and its sensors.
 */
public class DroneService {

    private static final int BUSY_WAITING_SLEEP_TIME = 250;
    private static final int TRAVELING_TIME = 5000;
    private static final int CLIENT_WAITING_TIME = 1000;
    private static final int ANALIZER_SLEEP_DURATION = 500;
    private static final int RANDOM_GENERATION_RANGE = 100;
    private static final int SUCCESS_PERCENTAGE = 70;
    private static final int PORT = 10_001;

    //Drone
    private final transient Drone drone;
    private final transient Analyzer analyzer;
    private transient Thread dataAnalyzer;
    private transient boolean active;
    private transient boolean started;

    // Connection
    private transient Double proximitySensorData;
    private transient Map<String, Double> accelerometerSensorData;
    private transient Double cameraSensorData;

    /**
     * Constructs the drone to be observed by this drone service.
     */
    public DroneService() {
        this.drone = new Drone();
        this.initAnalyzer();
        this.active = false;
        this.analyzer = new Analyzer();
    }

    /**
     * Activates the drone.
     */
    public void activateDrone() {
        this.active = true;
        this.dataAnalyzer.start();
        Connection.getInstance().subscribe(MqttTopicConstants.ORDER_TOPIC, msg -> {
            try {
                final JsonNode json = new ObjectMapper().readTree(new String(msg.getPayload()));
                if (MqttMessageValueConstants.PERFORM_DELIVERY_MESSAGE
                        .equals(json.get(MqttMessageParameterConstants.MESSAGE_PARAMETER).asText()))
                    new Thread(this::startDrone).start();
            } catch (JsonProcessingException e) {
                CustomLogger.getLogger(getClass().getName()).info(e.getMessage());
            }
        });
    }

    /**
     * Deactivates the drone.
     */
    public void stopDrone() {
        this.active = false;
        this.started = false;
        this.drone.halt();
        this.drone.getAccelerometerSensor().deactivate();
        this.drone.getCameraSensor().deactivate();
        this.drone.getProximitySensor().deactivate();
        Connection.getInstance().closeConnection();
    }

    /**
     * Provides data useful to connect to the drone.
     *
     * @return the port on which the drone service is waiting for the connection
     */
    public int getConnectionData() {
        return PORT;
    }

    /**
     * Gets the last read data from proximity sensor.
     *
     * @return last read data from proximity sensor
     */
    public Double getProximitySensorData() {
        return this.proximitySensorData;
    }

    /**
     * Gets the last read data from accelerometer sensor.
     *
     * @return last read data from accelerometer sensor
     */
    public Map<String, Double> getAccelerometerSensorData() {
        return this.accelerometerSensorData;
    }

    /**
     * Gets the last read data from camera.
     *
     * @return last read data from camera
     */
    public Double getCameraSensorData() {
        return this.cameraSensorData;
    }

    private void startDrone() {
        this.started = true;
        this.drone.start();
        this.simulateDroneLifecycle();
    }

    //Simulates drone lifecycle sending its status to aws with 70% of successful delivers.
    private void simulateDroneLifecycle() {
        final ObjectNode payload = new ObjectMapper().createObjectNode();

        try {
            payload.put(MqttMessageParameterConstants.STATUS_PARAMETER,
                    MqttMessageValueConstants.DELIVERING_MESSAGE);
            Connection.getInstance().publish(MqttTopicConstants.LIFECYCLE_TOPIC, payload);

            Thread.sleep(TRAVELING_TIME);

            final int choice = new Random().nextInt(RANDOM_GENERATION_RANGE - 1);
            if (choice < SUCCESS_PERCENTAGE)
                payload.put(MqttMessageParameterConstants.STATUS_PARAMETER,
                        MqttMessageValueConstants.DELIVERY_SUCCESSFUL_MESSAGE);
            else
                payload.put(MqttMessageParameterConstants.STATUS_PARAMETER,
                        MqttMessageValueConstants.DELIVERY_FAILED_MESSAGE);
            Thread.sleep(CLIENT_WAITING_TIME);
            Connection.getInstance().publish(MqttTopicConstants.LIFECYCLE_TOPIC, payload);
        } catch (InterruptedException e) {
            CustomLogger.getLogger(getClass().getName()).info(e.getMessage());
        }

        Connection.getInstance().subscribe(MqttTopicConstants.ORDER_TOPIC, msg -> {
            try {
                final JsonNode json = new ObjectMapper().readTree(new String(msg.getPayload()));
                if (MqttMessageValueConstants.DRONE_CALLBACK_MESSAGE
                        .equals(json.get(MqttMessageParameterConstants.MESSAGE_PARAMETER).asText())) {
                    payload.put(MqttMessageParameterConstants.STATUS_PARAMETER,
                            MqttMessageValueConstants.RETURN_ACKNOWLEDGEMENT_MESSAGE);
                    Connection.getInstance().publish(MqttTopicConstants.LIFECYCLE_TOPIC, payload);
                    new Thread(this::returnSimulation).start();
                }
            } catch (JsonProcessingException e) {
                CustomLogger.getLogger(getClass().getName()).info(e.getMessage());
            }
        });
    }

    private void returnSimulation() {
        try {
            Thread.sleep(TRAVELING_TIME);

            this.stopDrone();
        } catch (InterruptedException e) {
            CustomLogger.getLogger(getClass().getName()).info(e.getMessage());
        }
    }

    private void initAnalyzer() {
        this.dataAnalyzer = new Thread(() -> {
            try {
                while (this.active) {
                    if (this.started)
                        break;
                    Thread.sleep(BUSY_WAITING_SLEEP_TIME);
                }
                while (this.started) {
                    this.drone.analyzeData();

                    this.proximitySensorData = this.drone.getProximitySensor().getReadableValue();
                    this.accelerometerSensorData = this.drone.getAccelerometerSensor().getReadableValue();
                    this.cameraSensorData = this.drone.getCameraSensor().getReadableValue();
                    this.sendData();

                    this.analyzeData();
                    Thread.sleep(ANALIZER_SLEEP_DURATION);
                }
            } catch (InterruptedException e) {
                CustomLogger.getLogger(getClass().getName()).info(e.getMessage());
                //Restart Drone if thread interrupted
                this.startDrone();
            }
        });
    }

    private void analyzeData() {
        if (this.analyzer.isProximityCritical(this.proximitySensorData)
                || this.analyzer.isCriticalInclinationAngle(this.accelerometerSensorData))
            this.drone.halt();
    }

    private void sendData() {
        final ObjectMapper mapper = new ObjectMapper();
        final ObjectNode mapJson = mapper.createObjectNode();
        mapJson.put(MqttMessageParameterConstants.PROXIMITY_PARAMETER, this.proximitySensorData);
        final ObjectNode accelerometerValues = mapper.createObjectNode();
        this.accelerometerSensorData.forEach(accelerometerValues::put);
        mapJson.set(MqttMessageParameterConstants.ACCELEROMETER_PARAMETER, accelerometerValues);
        mapJson.put(MqttMessageParameterConstants.CAMERA_PARAMETER, this.cameraSensorData);

        Connection.getInstance().publish(MqttTopicConstants.DATA_TOPIC, mapJson);
    }
}
