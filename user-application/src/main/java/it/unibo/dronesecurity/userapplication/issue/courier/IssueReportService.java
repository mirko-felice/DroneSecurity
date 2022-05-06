package it.unibo.dronesecurity.userapplication.issue.courier;

import io.vertx.core.Future;
import it.unibo.dronesecurity.userapplication.issue.courier.issues.OpenIssue;
import it.unibo.dronesecurity.userapplication.issue.courier.issues.BaseIssue;
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
    public Future<List<OpenIssue>> getIssueReports() {
        final IssueReportRepository repo = IssueReportRepository.getInstance();
        return repo.getIssues();
    }

    /**
     * Creates a new issue.
     * @param issue the issue to send
     */
    public void addIssueReport(final BaseIssue issue) {
        final IssueReportRepository repo = IssueReportRepository.getInstance();
        repo.addIssue(issue);
    }
}
