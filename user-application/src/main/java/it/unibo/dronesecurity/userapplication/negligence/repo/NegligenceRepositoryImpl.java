package it.unibo.dronesecurity.userapplication.negligence.repo;

import io.vertx.core.Future;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import it.unibo.dronesecurity.userapplication.negligence.entities.*;
import it.unibo.dronesecurity.userapplication.negligence.utilities.NegligenceConstants;
import it.unibo.dronesecurity.userapplication.utilities.VertxHelper;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Implementation of {@link NegligenceRepository}.
 */
public final class NegligenceRepositoryImpl implements NegligenceRepository {

    private static final String REPORTS_COLLECTION_NAME = "negligenceReports";
    private static final String FORMS_COLLECTION_NAME = "negligenceActionForms";
    private static NegligenceRepositoryImpl singleton;

    /**
     * Get the Singleton instance.
     * @return the singleton
     */
    public static NegligenceRepositoryImpl getInstance() {
        synchronized (NegligenceRepositoryImpl.class) {
            if (singleton == null)
                singleton = new NegligenceRepositoryImpl();
            return singleton;
        }
    }

    @Override
    public void createReport(final NegligenceReport report) {
        final JsonArray command = new JsonArray().add(
                new JsonObject()
                        .put("$group", new JsonObject()
                                .putNull("_id")
                                .put("maxID", new JsonObject()
                                        .put("$max", "$" + NegligenceConstants.ID)
                                )
                        )
        );
        final long[] id = {1};
        VertxHelper.MONGO_CLIENT.aggregate(REPORTS_COLLECTION_NAME, command)
                .handler(result -> id[0] = result.getLong("maxID") + 1)
                .endHandler(ignored -> {
                    final NegligenceReportWithID reportWithID = NegligenceReportFactory.withID(id[0], report);
                    VertxHelper.MONGO_CLIENT.save(REPORTS_COLLECTION_NAME, JsonObject.mapFrom(reportWithID));
                });
    }

    @Override
    public Future<Void> takeAction(final NegligenceActionForm form) {
        VertxHelper.MONGO_CLIENT.save(FORMS_COLLECTION_NAME, JsonObject.mapFrom(form));
        final NegligenceReportWithID report = form.getReport();
        if (!(report instanceof OpenNegligenceReport))
            throw new IllegalArgumentException("Can NOT take action because report " + report + " is not open.");
        final ClosedNegligenceReport closedReport = ((OpenNegligenceReport) report).close(Instant.now());
        final JsonObject query = new JsonObject().put(NegligenceConstants.ID, report.getId());
        return VertxHelper.MONGO_CLIENT.findOneAndReplace(REPORTS_COLLECTION_NAME,
                query,
                JsonObject.mapFrom(closedReport))
                .mapEmpty();
    }

    @Override
    public Future<List<OpenNegligenceReport>> retrieveOpenReportsForCourier(final String username) {
        final JsonObject query = new JsonObject().put(NegligenceConstants.NEGLIGENT, username);
        return this.retrieveReportsForUser(query, OpenNegligenceReport.class);
    }

    @Override
    public Future<List<ClosedNegligenceReport>> retrieveClosedReportsForCourier(final String username) {
        final JsonObject query = new JsonObject().put(NegligenceConstants.NEGLIGENT, username);
        return this.retrieveReportsForUser(query, ClosedNegligenceReport.class);
    }

    @Override
    public Future<List<OpenNegligenceReport>> retrieveOpenReportsForMaintainer(final String username) {
        final JsonObject query = new JsonObject().put(NegligenceConstants.ASSIGNEE, username);
        return this.retrieveReportsForUser(query, OpenNegligenceReport.class);
    }

    @Override
    public Future<List<ClosedNegligenceReport>> retrieveClosedReportsForMaintainer(final String username) {
        final JsonObject query = new JsonObject().put(NegligenceConstants.ASSIGNEE, username);
        return this.retrieveReportsForUser(query, ClosedNegligenceReport.class);
    }

    private <T extends NegligenceReportWithID> Future<List<T>> retrieveReportsForUser(final JsonObject query,
                                                                                final Class<T> clazz) {
        return VertxHelper.MONGO_CLIENT.find(REPORTS_COLLECTION_NAME, query)
                .map(reports -> reports.stream()
                        .map(report -> Json.decodeValue(report.toString(), NegligenceReportWithID.class))
                        .filter(clazz::isInstance)
                        .map(clazz::cast)
                        .collect(Collectors.toList()));
    }
}
