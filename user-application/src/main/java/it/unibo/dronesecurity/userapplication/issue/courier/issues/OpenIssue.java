package it.unibo.dronesecurity.userapplication.issue.courier.issues;

import org.jetbrains.annotations.NotNull;

import java.time.Instant;

/**
 * Class representing an issue that has been created and sent.
 */
public class OpenIssue extends BaseIssue {

    private final int id;

    /**
     * Builds a new issue report.
     *
     * @param subject the short description of the issue (alias title/subject)
     * @param id the id of the created issue
     * @param issueInfo the information regarding the issue report
     * @param courierUsername username of the courier who sends the issue
     * @param sendingTime the timestamp of when the issue was sent
     */
    public OpenIssue(final String subject,
                     @NotNull final int id,
                     final String issueInfo,
                     final String courierUsername,
                     final Instant sendingTime) {
        super(subject, issueInfo, courierUsername, sendingTime);
        this.id = id;
    }

    /**
     * Gets the ID of the issue.
     *
     * @return the id of the issue
     */
    public int getId() {
        return this.id;
    }
}
