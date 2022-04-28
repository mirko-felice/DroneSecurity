package it.unibo.dronesecurity.userapplication.issue.courier.issues;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import it.unibo.dronesecurity.userapplication.issue.courier.serialization.IssueDeserializer;
import it.unibo.dronesecurity.userapplication.issue.courier.serialization.IssueSerializer;

/**
 * Interface of an issue report.
 */
@JsonDeserialize(using = IssueDeserializer.class)
@JsonSerialize(using = IssueSerializer.class)
public interface Issue {

    /**
     * Gets the details of the issue.
     * @return the details related to this issue
     */
    String getDetails();
}
