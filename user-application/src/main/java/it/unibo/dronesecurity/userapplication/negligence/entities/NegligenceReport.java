package it.unibo.dronesecurity.userapplication.negligence.entities;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.node.ObjectNode;
import it.unibo.dronesecurity.userapplication.auth.entities.Courier;
import it.unibo.dronesecurity.userapplication.auth.entities.Maintainer;
import it.unibo.dronesecurity.userapplication.negligence.serializers.NegligenceReportDeserializer;

/**
 * Represents the report used to record the commitment of a negligence by a {@link Courier}.
 */
@JsonDeserialize(using = NegligenceReportDeserializer.class)
public interface NegligenceReport {

    /**
     * Gets the negligent as a {@link Courier}.
     * @return the negligent
     */
    String getNegligent();

    /**
     * Gets the data collected in that instant.
     * @return the {@link ObjectNode} representing the data
     */
    DroneData getData();

    /**
     * Gets the {@link Maintainer} assigned to the report.
     * @return the assignee
     */
    String assignedTo();

}
