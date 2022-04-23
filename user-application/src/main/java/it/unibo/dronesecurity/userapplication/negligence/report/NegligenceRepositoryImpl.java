package it.unibo.dronesecurity.userapplication.negligence.report;

import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.mongo.MongoClient;

/**
 * Implementation of {@link NegligenceRepository}.
 */
public final class NegligenceRepositoryImpl implements NegligenceRepository {

    private static final String COLLECTION_NAME = "negligenceReports";
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
        this.database.save(COLLECTION_NAME, JsonObject.mapFrom(report));
    }

}
