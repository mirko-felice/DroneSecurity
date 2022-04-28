package it.unibo.dronesecurity.userapplication.issue.courier.repo;

import io.vertx.core.Future;
import it.unibo.dronesecurity.userapplication.issue.courier.issues.CreatedIssue;
import it.unibo.dronesecurity.userapplication.issue.courier.issues.NotCreatedIssue;
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
    void addIssue(NotCreatedIssue issue);

    /**
     * Gets all issues.
     * @return the list of issues contained in repository
     */
    Future<List<CreatedIssue>> getIssues();

    /**
     * Get the instance of this repository.
     * @return the instance
     */
    @Contract(value = " -> new", pure = true)
    static @NotNull IssueReportRepository getInstance() {
        return IssueReportRepositoryImpl.getInstance();
    }
}
