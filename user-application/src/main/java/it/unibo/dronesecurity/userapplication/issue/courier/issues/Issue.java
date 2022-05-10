package it.unibo.dronesecurity.userapplication.issue.courier.issues;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import it.unibo.dronesecurity.userapplication.auth.entities.Courier;
import it.unibo.dronesecurity.userapplication.issue.courier.serialization.IssueDeserializer;
import it.unibo.dronesecurity.userapplication.issue.courier.serialization.IssueSerializer;

import java.time.Instant;

/**
 * Interface of an issue report.
 */
@JsonDeserialize(using = IssueDeserializer.class)
@JsonSerialize(using = IssueSerializer.class)
public interface Issue {

    /**
     * Gets the subject of the issue, specified by the {@link Courier}.
     * @return the subject of this issue
     */
    String getSubject();

    /**
     * Gets the details of the issue.
     * @return the details related to this issue
     */
    String getDetails();

    /**
     * Gets the Courier that created the issue report.
     * @return the Courier that created the report
     */
    String getCourier();

    /**
     * Gets the timestamp of when the report was sent.
     * @return the creation timestamp
     */
    Instant getReportingDate();

    /**
     * Gets the string representation of the state of the object.
     * @return the string representation of the state
     */
    String getState();
}
