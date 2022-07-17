/*
 * Copyright (c) 2021-2022, Mirko Felice & Maxim Derevyanchenko. All rights reserved.
 * Licensed under the MIT license. See LICENSE file in the project root for details.
 */

package io.github.dronesecurity.dronesystem.drone.application.drone.camera;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import io.github.dronesecurity.dronesystem.drone.domain.drone.camera.objects.ProcessedCameraData;
import io.github.dronesecurity.dronesystem.drone.domain.drone.camera.services.CameraDataPublisher;
import io.github.dronesecurity.dronesystem.drone.domain.drone.order.objects.OrderData;
import io.github.dronesecurity.lib.Connection;
import io.github.dronesecurity.lib.MqttMessageParameterConstants;
import io.github.dronesecurity.lib.MqttTopicConstants;
import org.jetbrains.annotations.NotNull;

/**
 * Implementation of {@link CameraDataPublisher} that publishes processed camera data
 * to an established AWS connection.
 */
public class CameraDataPublisherImpl implements CameraDataPublisher {

    /**
     * {@inheritDoc}
     */
    @Override
    public void publishCameraData(final @NotNull OrderData orderData, final @NotNull ProcessedCameraData cameraData) {
        final ObjectMapper mapper = new ObjectMapper();
        final ObjectNode mapJson = mapper.createObjectNode();
        mapJson.put(MqttMessageParameterConstants.CAMERA_PARAMETER, cameraData.getImageLength());

        Connection.getInstance().publish(MqttTopicConstants.DATA_TOPIC + orderData.getOrderId()
                + " " + MqttMessageParameterConstants.CAMERA_PARAMETER, mapJson);
    }
}
