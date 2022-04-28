package it.unibo.dronesecurity.userapplication.issue.courier;

import io.vertx.core.Future;
import it.unibo.dronesecurity.userapplication.issue.courier.issues.CreatedIssue;
import it.unibo.dronesecurity.userapplication.issue.courier.issues.NotCreatedIssue;
import it.unibo.dronesecurity.userapplication.issue.courier.repo.IssueReportRepository;

import java.util.List;

/**
 * The service for issue reporting.
 */
public final class IssueReportService {

    /**
     * Lists the issues reported.
     * @return the future of the list of issues reported
     */
    public Future<List<CreatedIssue>> getIssueReports() {
        final IssueReportRepository repo = IssueReportRepository.getInstance();
        return repo.getIssues();
    }

    /**
     * Creates a new issue.
     * @param issue the issue to send
     */
    public void addIssueReport(final NotCreatedIssue issue) {
        final IssueReportRepository repo = IssueReportRepository.getInstance();
        repo.addIssue(issue);
    }
}
