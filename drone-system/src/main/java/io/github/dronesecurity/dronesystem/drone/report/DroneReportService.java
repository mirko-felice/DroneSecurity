/*
 * Copyright (c) 2021-2022, Mirko Felice & Maxim Derevyanchenko. All rights reserved.
 * Licensed under the MIT license. See LICENSE file in the project root for details.
 */

package io.github.dronesecurity.dronesystem.drone.report;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.dronesecurity.lib.Connection;
import io.github.dronesecurity.lib.MqttTopicConstants;

/**
 * Service providing methods to reports negligence.
 */
public final class DroneReportService {

    /**
     * Publish the {@link NegligenceReport} to particular MQTT topic based on {@link MqttTopicConstants}.
     * @param report the report to record
     */
    public void reportsNegligence(final NegligenceReport report) {
        final JsonNode json = new ObjectMapper().convertValue(report, JsonNode.class);
        Connection.getInstance().publish(MqttTopicConstants.NEGLIGENCE_REPORTS_TOPIC + report.getNegligent(), json);
    }
}
