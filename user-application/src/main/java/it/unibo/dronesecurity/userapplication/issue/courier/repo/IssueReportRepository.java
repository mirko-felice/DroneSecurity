package it.unibo.dronesecurity.userapplication.issue.courier.repo;

import io.vertx.core.Future;
import it.unibo.dronesecurity.userapplication.issue.courier.issues.ClosedIssue;
import it.unibo.dronesecurity.userapplication.issue.courier.issues.CreatedIssue;
import it.unibo.dronesecurity.userapplication.issue.courier.issues.Issue;
import it.unibo.dronesecurity.userapplication.issue.courier.issues.OpenIssue;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * Repository operating on Issue Reports regarding drones sent by the Courier.
 */
public interface IssueReportRepository {

    /**
     * Add new open issue.
     * @param issue the issue to add to repository
     */
    void addIssue(Issue issue);

    /**
     * Updates an existing Open issue and sets it as visioned.
     * @param issue the issue to be updated
     * @return whether the operation went successfully or not
     */
    Future<Boolean> visionOpenIssue(OpenIssue issue);

    /**
     * Gets all open issues of the currently logged user.
     * @return the list of open issues contained in repository
     */
    Future<List<CreatedIssue>> getOpenIssues();

    /**
     * Gets all closed issues of the currently logged user.
     * @return the list of closed issues contained in repository
     */
    Future<List<ClosedIssue>> getClosedIssues();

    /**
     * Get the instance of this repository.
     * @return the instance
     */
    @Contract(value = " -> new", pure = true)
    static @NotNull IssueReportRepository getInstance() {
        return IssueReportRepositoryImpl.getInstance();
    }
}
