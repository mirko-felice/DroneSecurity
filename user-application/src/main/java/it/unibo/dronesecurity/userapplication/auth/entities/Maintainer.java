package it.unibo.dronesecurity.userapplication.auth.entities;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

/**
 * Entity representing the Maintainer user.
 */
public final class Maintainer extends BaseUser {

    private Maintainer(final String username, final String password) {
        super(username, password, Role.MAINTAINER);
    }

    /**
     * Build the Maintainer using only username and password, useful for authentication.
     * @param username his username
     * @param password his password
     * @return a new {@link Maintainer}
     */
    @Contract("_, _ -> new")
    public static @NotNull Maintainer complete(final String username, final String password) {
        return new Maintainer(username, password);
    }
}
