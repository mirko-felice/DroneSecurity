package it.unibo.dronesecurity.userapplication.negligence.report;

import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.mongo.MongoClient;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Implementation of {@link NegligenceRepository}.
 */
public final class NegligenceRepositoryImpl implements NegligenceRepository {

    private static final String REPORTS_COLLECTION_NAME = "negligenceReports";
    private static final String FORMS_COLLECTION_NAME = "negligenceActionForms";
    private static NegligenceRepositoryImpl singleton;
    private final MongoClient database;

    private NegligenceRepositoryImpl() {
        final JsonObject config = new JsonObject();
        config.put("db_name", "drone"); //TODO aggiungere 'DB' alla fine di drone
        this.database = MongoClient.create(Vertx.vertx(), config);
    }

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
        this.database.save(REPORTS_COLLECTION_NAME, JsonObject.mapFrom(report));
    }

    @Override
    public void takeAction(final NegligenceActionForm form) {
        this.database.save(FORMS_COLLECTION_NAME, JsonObject.mapFrom(form));
    }

    @Override
    public Future<List<NegligenceReport>> retrieveReportsForCourier(final String username) {
        final JsonObject query = new JsonObject().put("negligent", username);
        return this.database.find(REPORTS_COLLECTION_NAME, query)
                .map(all -> all.stream()
                        .map(report -> Json.decodeValue(report.toString(), NegligenceReport.class))
                        .collect(Collectors.toList()));
    }
}
