package it.unibo.dronesecurity.userapplication.auth.repo;

import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.mongo.MongoClient;
import it.unibo.dronesecurity.userapplication.auth.entities.User;
import it.unibo.dronesecurity.userapplication.utilities.PasswordHelper;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

/**
 * Implementation of {@link AuthenticationRepository}.
 */
public final class AuthenticationRepositoryImpl implements AuthenticationRepository {

    private static AuthenticationRepositoryImpl singleton;
    private final MongoClient database;

    private AuthenticationRepositoryImpl() {
        final JsonObject config = new JsonObject();
        config.put("db_name", "drone");
        this.database = MongoClient.create(Vertx.vertx(), config);
    }

    @Override
    public Future<Boolean> authenticate(final User user) {
        final JsonObject query = JsonObject.mapFrom(user);
        query.remove("password");
        return this.database.findOne("users", query, null)
                .map(userFound -> {
                    try {
                        return userFound != null
                                && PasswordHelper.validatePassword(user.getPassword(),
                                userFound.getString("password"));
                    } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
                        return Boolean.FALSE;
                    }
                });
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
