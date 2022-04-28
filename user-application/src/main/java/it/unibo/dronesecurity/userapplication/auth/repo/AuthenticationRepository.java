package it.unibo.dronesecurity.userapplication.auth.repo;

import io.vertx.core.Future;
import it.unibo.dronesecurity.userapplication.auth.entities.Courier;
import it.unibo.dronesecurity.userapplication.auth.entities.Maintainer;
import it.unibo.dronesecurity.userapplication.auth.entities.User;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

/**
 * Repository to perform different actions on {@link User} entity.
 */
public interface AuthenticationRepository {

    /**
     * Authenticate the user, returning if he can access or not.
     * @param user the user to authenticate
     * @return the {@link Future} containing the result
     */
    Future<Boolean> authenticate(User user);

    /**
     * Retrieve the {@link Courier} starting from his username.
     * @param username username to find
     * @return the {@link Courier}
     */
    Future<Courier> retrieveCourierFromUsername(String username);

    /**
     * Retrieve the {@link Maintainer} starting from his username.
     * @param username username to find
     * @return the {@link Maintainer}
     */
    Future<Maintainer> retrieveMaintainerFromUsername(String username);

    /**
     * Get the instance of this repository.
     * @return the instance
     */
    @Contract(value = " -> new", pure = true)
    static @NotNull AuthenticationRepository getInstance() {
        return AuthenticationRepositoryImpl.getInstance();
    }
}
