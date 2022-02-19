package it.unibo.dronesecurity.userapplication.auth.repo;

import io.vertx.core.Future;
import it.unibo.dronesecurity.userapplication.auth.entities.BaseUser;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

/**
 * Repository to perform different actions on {@link BaseUser} entity.
 */
public interface AuthenticationRepository {

    /**
     * Authenticate the user, returning if he can access or not.
     * @param user the user to authenticate
     * @return the {@link Future} containing the result
     */
    Future<Boolean> authenticate(BaseUser user);

    /**
     * Get the instance of this repository.
     * @return the instance
     */
    @Contract(value = " -> new", pure = true)
    static @NotNull AuthenticationRepository getInstance() {
        return AuthenticationRepositoryImpl.getInstance();
    }
}
