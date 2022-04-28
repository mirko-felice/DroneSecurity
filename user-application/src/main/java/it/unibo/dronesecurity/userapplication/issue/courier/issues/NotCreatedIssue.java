package it.unibo.dronesecurity.userapplication.issue.courier.issues;

/**
 * Entity representing an issue report that has to be sent and created.
 */
public class NotCreatedIssue implements Issue {

    private final String issueInfo;

    /**
     * Builds a new issue report.
     * @param issueInfo the information regarding the issue report
     */
    public NotCreatedIssue(final String issueInfo) {
        this.issueInfo = issueInfo;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getDetails() {
        return this.issueInfo;
    }
}
