package it.unibo.dronesecurity.userapplication.issue.courier;

import io.vertx.core.Future;
import it.unibo.dronesecurity.userapplication.issue.courier.issues.*;
import it.unibo.dronesecurity.userapplication.issue.courier.repo.IssueReportRepository;

import java.util.List;

//TODO fare interfacce limitanti per Courier e Maintainer.
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

    /**
     * Informs the database that an {@link OpenIssue} is now visioned by a maintainer.
     * @param issue the issue that is visioned
     * @return whether the update operation has succeeded or not
     */
    public Future<Boolean> visionIssue(final OpenIssue issue) {
        return IssueReportRepository.getInstance().visionOpenIssue(issue);
    }

    /**
     * Informs the database that a {@link VisionedIssue} is being closed by a maintainer.
     * @param issue the issue that is closed
     * @param solution string representing the solution used by the maintainer
     * @return whether the update operation has succeeded or not
     */
    public Future<Boolean> closeIssue(final VisionedIssue issue, final String solution) {
        return IssueReportRepository.getInstance().closeVisionedIssue(issue, solution);
    }
}
