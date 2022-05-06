package it.unibo.dronesecurity.userapplication.issue.courier.repo;

import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.mongo.MongoClient;
import it.unibo.dronesecurity.userapplication.issue.courier.issues.OpenIssue;
import it.unibo.dronesecurity.userapplication.issue.courier.issues.BaseIssue;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

//TODO refactor repositories (make an abstract class for mongo initialization)
/**
 * Implementation of {@link IssueReportRepository}.
 */
public final class IssueReportRepositoryImpl implements IssueReportRepository {

    private static final String COLLECTION_NAME = "issueReports";
    private static IssueReportRepository singleton;
    private final MongoClient database;

    private IssueReportRepositoryImpl() {
        final JsonObject config = new JsonObject();
        config.put("db_name", "drone");
        //TODO move vertx instantiating to a previous phase.
        this.database = MongoClient.create(Vertx.vertx(), config);
    }

    /**
     * Get the Singleton instance.
     * @return the singleton
     */
    public static IssueReportRepository getInstance() {
        synchronized (IssueReportRepositoryImpl.class) {
            if (singleton == null)
                singleton = new IssueReportRepositoryImpl();
            return singleton;
        }
    }

    private Future<Long> getLastID() {
        return this.database.count(COLLECTION_NAME, new JsonObject());
    }

    @Override
    public void addIssue(final BaseIssue issue) {
        final JsonObject newIssue = new JsonObject();
        this.getLastID().onComplete(id -> {
            if (id.succeeded())
                newIssue.put("ID", id.result() + 1);
            else
                newIssue.put("ID", 1);
            newIssue.mergeIn(JsonObject.mapFrom(issue), true);
            this.database.save(COLLECTION_NAME, newIssue);
        });
    }

    @Override
    public Future<List<OpenIssue>> getIssues() {
        return this.database.find(COLLECTION_NAME, new JsonObject())
                .transform(issues -> {
                    List<OpenIssue> result = Collections.emptyList();
                    if (!issues.result().isEmpty())
                        result = issues.result().stream()
                                .map(json -> Json.decodeValue(json.toString(), OpenIssue.class))
                                .collect(Collectors.toList());

                    return Future.succeededFuture(result);
                });
    }
}
