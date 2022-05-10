package it.unibo.dronesecurity.userapplication.issue.courier.issues;

import it.unibo.dronesecurity.userapplication.issue.courier.serialization.IssueStringHelper;
import org.jetbrains.annotations.NotNull;

import java.time.Instant;

/**
 * Class representing a solved issue report.
 */
public class ClosedIssue extends CreatedIssue {

    /**
     * Builds a new issue report.
     *
     * @param subject the short description of the issue (alias title/subject)
     * @param id the id of the created issue
     * @param issueInfo the information regarding the issue report
     * @param courierUsername username of the courier who sends the issue
     * @param sendingTime the timestamp of when the issue was sent
     */
    public ClosedIssue(final String subject,
                     @NotNull final int id,
                     final String issueInfo,
                     final String courierUsername,
                     final Instant sendingTime) {
        super(subject, id, issueInfo, courierUsername, sendingTime);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getState() {
        return IssueStringHelper.STATUS_CLOSED;
    }
}
