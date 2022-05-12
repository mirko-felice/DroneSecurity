package it.unibo.dronesecurity.dronesystem.drone.report;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import it.unibo.dronesecurity.lib.Connection;
import it.unibo.dronesecurity.lib.MqttTopicConstants;

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
        Connection.getInstance().publish(MqttTopicConstants.NEGLIGENCE_REPORTS_TOPIC, json);
    }
}
