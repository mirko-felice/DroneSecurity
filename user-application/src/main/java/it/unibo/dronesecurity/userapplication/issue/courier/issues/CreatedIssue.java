package it.unibo.dronesecurity.userapplication.issue.courier.issues;

import org.jetbrains.annotations.NotNull;

/**
 * Class representing an issue that has been created and sent.
 */
public class CreatedIssue extends NotCreatedIssue {

    private final int id;

    /**
     * Builds a new issue report.
     *
     * @param id the id of the created issue
     * @param issueInfo the information regarding the issue report
     */
    public CreatedIssue(@NotNull final int id, final String issueInfo) {
        super(issueInfo);
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
