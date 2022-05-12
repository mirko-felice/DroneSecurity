package it.unibo.dronesecurity.userapplication.issue.courier.issues;

import it.unibo.dronesecurity.userapplication.issue.courier.serialization.IssueStringHelper;
import org.jetbrains.annotations.NotNull;

import java.time.Instant;

/**
 * Class representing a solved issue report.
 */
public class ClosedIssue extends CreatedIssue {

    private final String issueSolution;

    /**
     * Builds a new issue report.
     *
     * @param subject the short description of the issue (alias title/subject)
     * @param id the id of the created issue
     * @param issueInfo the information regarding the issue report
     * @param courierUsername username of the courier who sends the issue
     * @param sendingTime the timestamp of when the issue was sent
     * @param issueSolution string representing the solution used by the maintainer
     */
    public ClosedIssue(final String subject,
                     @NotNull final int id,
                     final String issueInfo,
                     final String courierUsername,
                     final Instant sendingTime,
                     final String issueSolution) {
        super(subject, id, issueInfo, courierUsername, sendingTime);
        this.issueSolution = issueSolution;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getState() {
        return IssueStringHelper.STATUS_CLOSED;
    }

    /**
     * Gets the solution specified by the maintainer to the issue.
     * @return the string describing the solution chosen by the maintainer
     */
    public String getIssueSolution() {
        return this.issueSolution;
    }
}
