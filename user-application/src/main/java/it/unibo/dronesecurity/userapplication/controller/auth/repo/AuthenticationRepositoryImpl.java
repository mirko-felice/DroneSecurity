package it.unibo.dronesecurity.userapplication.controller.auth.repo;

import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.mongo.MongoClient;
import it.unibo.dronesecurity.userapplication.controller.auth.entities.BaseUser;
import it.unibo.dronesecurity.userapplication.utilities.CustomLogger;

import java.util.Objects;

/**
 * Implementation of {@link AuthenticationRepository}.
 */
public final class AuthenticationRepositoryImpl implements AuthenticationRepository {

    private static AuthenticationRepositoryImpl singleton;
    private final transient MongoClient database;

    private AuthenticationRepositoryImpl() {
        final JsonObject config = new JsonObject();
        config.put("db_name", "drone");
        this.database = MongoClient.create(Vertx.vertx(), config);
    }

    @Override
    public Future<Boolean> authenticate(final BaseUser user) {
        final JsonObject query = JsonObject.mapFrom(user);
        CustomLogger.getLogger(getClass().getName()).info(query.encodePrettily());
        return this.database.findOne("users", query, null)
                .map(Objects::nonNull);
    }

    /**
     * Get the Singleton instance.
     * @return the singleton
     */
    public static AuthenticationRepository getInstance() {
        synchronized (AuthenticationRepositoryImpl.class) {
            if (singleton == null)
                singleton = new AuthenticationRepositoryImpl();
            return singleton;
        }
    }
}
