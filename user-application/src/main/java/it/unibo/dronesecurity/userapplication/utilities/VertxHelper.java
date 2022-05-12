package it.unibo.dronesecurity.userapplication.utilities;

import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.mongo.MongoClient;
import io.vertx.ext.web.client.WebClient;

/**
 * Helper class containing Vert.x constants that should be instantiated once.
 */
public final class VertxHelper {
    // TODO aggiungere 'DB' alla fine di drone
    private static final JsonObject CONFIG = new JsonObject().put("db_name", "drone");

    /**
     * {@link Vertx} instance.
     */
    public static final Vertx VERTX = Vertx.vertx();

    /**
     * {@link WebClient} used to interact with services.
     */
    public static final WebClient WEB_CLIENT = WebClient.create(VERTX);

    /**
     * {@link MongoClient} used to interact with a MongoDB service.
     */
    public static final MongoClient MONGO_CLIENT = MongoClient.create(VertxHelper.VERTX, CONFIG);

    private VertxHelper() { }
}
