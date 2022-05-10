package it.unibo.dronesecurity.userapplication.issue.courier;

import io.vertx.core.Future;
import it.unibo.dronesecurity.userapplication.issue.courier.issues.ClosedIssue;
import it.unibo.dronesecurity.userapplication.issue.courier.issues.CreatedIssue;
import it.unibo.dronesecurity.userapplication.issue.courier.issues.Issue;
import it.unibo.dronesecurity.userapplication.issue.courier.repo.IssueReportRepository;

import java.util.List;

/**
 * The service for issue reporting.
 */
public final class IssueReportService {

    /**
     * Lists open issues reported.
     * @return the future of the list of open issues reported
     */
    public Future<List<CreatedIssue>> getOpenIssueReports() {
        return IssueReportRepository.getInstance().getOpenIssues();
    }

    /**
     * Lists issues reported that have been closed.
     * @return the future of the list of closed issues
     */
    public Future<List<ClosedIssue>> getClosedIssueReports() {
        return IssueReportRepository.getInstance().getClosedIssues();
    }

    /**
     * Creates a new issue.
     * @param issue the issue to send
     */
    public void addIssueReport(final Issue issue) {
        final IssueReportRepository repo = IssueReportRepository.getInstance();
        repo.addIssue(issue);
    }
}
