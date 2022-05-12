package it.unibo.dronesecurity.userapplication.negligence.entities;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import it.unibo.dronesecurity.userapplication.negligence.serializers.NegligenceReportWithIDSerializer;

/**
 * Represents a {@link NegligenceReport} adding unique identifier.
 */
@JsonSerialize(using = NegligenceReportWithIDSerializer.class)
public interface NegligenceReportWithID extends NegligenceReport {

    /**
     * Gets the unique ID associated with this report.
     * @return the ID
     */
    long getId();
}
