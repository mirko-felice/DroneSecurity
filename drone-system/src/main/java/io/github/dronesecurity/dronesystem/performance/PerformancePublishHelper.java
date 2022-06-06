/*
 * Copyright (c) 2021-2022, Mirko Felice & Maxim Derevyanchenko. All rights reserved.
 * Licensed under the MIT license. See LICENSE file in the project root for details.
 */

package io.github.dronesecurity.dronesystem.performance;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import io.github.dronesecurity.lib.Connection;
import io.github.dronesecurity.lib.MqttMessageParameterConstants;
import io.github.dronesecurity.lib.MqttTopicConstants;

/**
 * Publisher that helps to build correctly the messages and send them to respective topics.
 */
public final class PerformancePublishHelper {

    private PerformancePublishHelper() { }

    /**
     * Builds and publishes data read by the camera to its topic.
     * @param frame The bytearray representing the frame read by the camera
     * @param timestamp Timestamp of the moment the frame was read
     */
    public static void publishCamera(final Byte[] frame, final long timestamp) {

        final ObjectNode cameraData = new ObjectMapper().createObjectNode();
        cameraData.put(MqttMessageParameterConstants.CAMERA_PARAMETER, frame.length);
        cameraData.put(MqttMessageParameterConstants.TIMESTAMP, timestamp);

        Connection.getInstance().publish(MqttTopicConstants.PERFORMANCE_CAMERA, cameraData);
    }
}
