package it.unibo.dronesecurity.userapplication.auth.repo;

import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.mongo.MongoClient;
import it.unibo.dronesecurity.userapplication.auth.entities.Courier;
import it.unibo.dronesecurity.userapplication.auth.entities.Maintainer;
import it.unibo.dronesecurity.userapplication.auth.entities.User;
import it.unibo.dronesecurity.userapplication.utilities.PasswordHelper;
import org.jetbrains.annotations.NotNull;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

/**
 * Implementation of {@link AuthenticationRepository}.
 */
public final class AuthenticationRepositoryImpl implements AuthenticationRepository {

    private static final String COLLECTION_NAME = "users";
    private static final String PASSWORD_FIELD = "password";
    private static AuthenticationRepositoryImpl singleton;
    private final MongoClient database;

    private AuthenticationRepositoryImpl() {
        final JsonObject config = new JsonObject();
        config.put("db_name", "drone");
        this.database = MongoClient.create(Vertx.vertx(), config);
    }

    @Override
    public Future<Boolean> authenticate(final @NotNull User user) {
        final JsonObject query = new JsonObject().put("username", user.getUsername());
        return this.database.findOne(COLLECTION_NAME, query, null)
                .map(userFound -> {
                    try {
                        return userFound != null
                                && PasswordHelper.validatePassword(user.getPassword(),
                                userFound.getString(PASSWORD_FIELD));
                    } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
                        return Boolean.FALSE;
                    }
                });
    }

    @Override
    public Future<Courier> retrieveCourierFromUsername(final String username) {
        return this.retrieveUserFromUsername(username)
                .flatMap(user -> {
                    final String password = user.getString(PASSWORD_FIELD);
                    final String supervisor = user.getString("supervisor");
                    final Future<Maintainer> maintainer = this.retrieveMaintainerFromUsername(supervisor);
                    return maintainer.map(sup -> Courier.complete(username, password, sup));
                });
    }

    @Override
    public Future<Maintainer> retrieveMaintainerFromUsername(final String username) {
        return this.retrieveUserFromUsername(username)
                .map(user -> Maintainer.complete(username, user.getString(PASSWORD_FIELD)));
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

    private Future<JsonObject> retrieveUserFromUsername(final String username) {
        final JsonObject query = new JsonObject().put("user", username);
        return this.database.findOne(COLLECTION_NAME, query, null);
    }
}
