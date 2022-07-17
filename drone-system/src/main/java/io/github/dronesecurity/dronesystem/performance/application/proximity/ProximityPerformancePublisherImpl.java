/*
 * Copyright (c) 2021-2022, Mirko Felice & Maxim Derevyanchenko. All rights reserved.
 * Licensed under the MIT license. See LICENSE file in the project root for details.
 */

package io.github.dronesecurity.dronesystem.performance.application.proximity;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import io.github.dronesecurity.dronesystem.performance.domain.proximity.objects.ProximityPerformanceData;
import io.github.dronesecurity.dronesystem.performance.domain.proximity.services.ProximityPerformancePublisher;
import io.github.dronesecurity.dronesystem.performance.utilities.PerformanceStringConstants;
import io.github.dronesecurity.lib.Connection;
import io.github.dronesecurity.lib.MqttMessageParameterConstants;
import io.github.dronesecurity.lib.MqttTopicConstants;
import org.jetbrains.annotations.NotNull;

/**
 * Implementation of the {@link ProximityPerformancePublisher}.
 */
public class ProximityPerformancePublisherImpl implements ProximityPerformancePublisher {

    /**
     * {@inheritDoc}
     */
    @Override
    public void publishProximity(final @NotNull ProximityPerformanceData proximityPerformanceData) {
        final ObjectNode proximityJson = new ObjectMapper().createObjectNode();
        proximityJson.put(PerformanceStringConstants.DISTANCE_PARAMETER,
                proximityPerformanceData.getProximityData().getDistance());
        proximityJson.put(PerformanceStringConstants.TIMESTAMP, proximityPerformanceData.getTimestamp());
        proximityJson.put(PerformanceStringConstants.INDEX, proximityPerformanceData.getIndex());

        Connection.getInstance().publish(
                MqttTopicConstants.PERFORMANCE_TOPIC + " " + MqttMessageParameterConstants.PROXIMITY_PARAMETER,
                proximityJson);

    }
}
