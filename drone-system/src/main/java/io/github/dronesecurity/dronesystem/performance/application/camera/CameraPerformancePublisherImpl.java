/*
 * Copyright (c) 2021-2022, Mirko Felice & Maxim Derevyanchenko. All rights reserved.
 * Licensed under the MIT license. See LICENSE file in the project root for details.
 */

package io.github.dronesecurity.dronesystem.performance.application.camera;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import io.github.dronesecurity.dronesystem.performance.domain.camera.objects.CameraPerformanceData;
import io.github.dronesecurity.dronesystem.performance.domain.camera.services.CameraPerformancePublisher;
import io.github.dronesecurity.dronesystem.performance.utilities.PerformanceStringConstants;
import io.github.dronesecurity.lib.Connection;
import io.github.dronesecurity.lib.MqttMessageParameterConstants;
import io.github.dronesecurity.lib.MqttTopicConstants;
import org.jetbrains.annotations.NotNull;

/**
 * Implementation of the {@link CameraPerformancePublisher}.
 */
public class CameraPerformancePublisherImpl implements CameraPerformancePublisher {

    /**
     * {@inheritDoc}
     */
    @Override
    public void publishCamera(final @NotNull CameraPerformanceData cameraPerformanceData) {
        final ObjectNode cameraJson = new ObjectMapper().createObjectNode();
        cameraJson.put(PerformanceStringConstants.IMAGE_SIZE, cameraPerformanceData.getCameraData().getImageLength());
        cameraJson.put(PerformanceStringConstants.TIMESTAMP, cameraPerformanceData.getTimestamp());
        cameraJson.put(PerformanceStringConstants.INDEX, cameraPerformanceData.getIndex());

        Connection.getInstance().publish(
                MqttTopicConstants.PERFORMANCE_TOPIC + " " + MqttMessageParameterConstants.CAMERA_PARAMETER,
                cameraJson);
    }
}
