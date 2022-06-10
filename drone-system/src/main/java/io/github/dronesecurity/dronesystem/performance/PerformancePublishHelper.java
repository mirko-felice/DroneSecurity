/*
 * Copyright (c) 2021-2022, Mirko Felice & Maxim Derevyanchenko. All rights reserved.
 * Licensed under the MIT license. See LICENSE file in the project root for details.
 */

package io.github.dronesecurity.dronesystem.performance;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import io.github.dronesecurity.dronesystem.performance.drone.sensordata.AccelerometerData;
import io.github.dronesecurity.dronesystem.performance.drone.sensordata.CameraData;
import io.github.dronesecurity.dronesystem.performance.drone.sensordata.ProximityData;
import io.github.dronesecurity.lib.Connection;
import io.github.dronesecurity.lib.MqttMessageParameterConstants;
import io.github.dronesecurity.lib.MqttTopicConstants;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

/**
 * Publisher that helps to build correctly the messages and send them to respective topics.
 */
public final class PerformancePublishHelper {

    private PerformancePublishHelper() { }

    /**
     * Builds and publishes data read by the camera to its topic.
     * @param cameraData data of the
     *                   {@link io.github.dronesecurity.dronesystem.performance.drone.CameraTimed} to be sent
     * @param accelerometerPerformanceData data of the
     *                          {@link io.github.dronesecurity.dronesystem.performance.drone.AccelerometerTimed} to be
     *                          sent
     * @param proximityPerformanceData data of the
     *                              {@link io.github.dronesecurity.dronesystem.performance.drone.ProximityTimed} to be
     *                              sent
     */
    public static void publishData(final @NotNull CameraData cameraData,
                                   final @NotNull AccelerometerData accelerometerPerformanceData,
                                   final @NotNull ProximityData proximityPerformanceData) {

        final ObjectNode cameraJson = new ObjectMapper().createObjectNode();
        cameraJson.put(PerformanceStringConstants.IMAGE_SIZE, cameraData.getImageSize());
        cameraJson.put(PerformanceStringConstants.TIMESTAMP, cameraData.getTimestamp());
        cameraJson.put(PerformanceStringConstants.INDEX, cameraData.getIndex());

        final ObjectNode accelerometerJson = new ObjectMapper().createObjectNode();
        final Map<String, Double> accelerometerData = accelerometerPerformanceData.getData();
        accelerometerJson.put(MqttMessageParameterConstants.PITCH,
                accelerometerData.get(MqttMessageParameterConstants.PITCH));
        accelerometerJson.put(MqttMessageParameterConstants.ROLL,
                accelerometerData.get(MqttMessageParameterConstants.ROLL));
        accelerometerJson.put(MqttMessageParameterConstants.YAW,
                accelerometerData.get(MqttMessageParameterConstants.YAW));
        accelerometerJson.put(PerformanceStringConstants.TIMESTAMP, accelerometerPerformanceData.getTimestamp());
        accelerometerJson.put(PerformanceStringConstants.INDEX, accelerometerPerformanceData.getIndex());

        final ObjectNode proximityJson = new ObjectMapper().createObjectNode();
        proximityJson.put(PerformanceStringConstants.DISTANCE_PARAMETER, proximityPerformanceData.getData());
        proximityJson.put(PerformanceStringConstants.TIMESTAMP, proximityPerformanceData.getTimestamp());
        proximityJson.put(PerformanceStringConstants.INDEX, proximityPerformanceData.getIndex());


        final ObjectNode completeData = new ObjectMapper().createObjectNode();
        completeData.set(MqttMessageParameterConstants.CAMERA_PARAMETER, cameraJson);
        completeData.set(MqttMessageParameterConstants.ACCELEROMETER_PARAMETER, accelerometerJson);
        completeData.set(MqttMessageParameterConstants.PROXIMITY_PARAMETER, proximityJson);

        Connection.getInstance().publish(MqttTopicConstants.PERFORMANCE_TOPIC, completeData);
    }

}
