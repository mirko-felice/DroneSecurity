package it.unibo.dronesecurity.userapplication.auth.entities;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

/**
 * Entity representing the Courier user.
 */
public final class Courier extends BaseUser {

    private final Maintainer supervisor;

    private Courier(final String username, final String password, final Maintainer supervisor) {
        super(username, password, Role.COURIER);
        this.supervisor = supervisor;
    }

    /**
     * Build the Courier using only username and password, useful for authentication.
     * @param username his username
     * @param password his password
     * @return a new {@link Courier}
     */
    @Contract("_, _ -> new")
    public static @NotNull User forAuthentication(final String username, final String password) {
        return new Courier(username, password, null);
    }

    /**
     * Build the Courier using all needed information, useful for all other operations.
     * @param username his username
     * @param password his password
     * @param supervisor his supervisor
     * @return a new {@link Courier}
     */
    @Contract("_, _, _ -> new")
    public static @NotNull Courier complete(final String username, final String password, final Maintainer supervisor) {
        return new Courier(username, password, supervisor);
    }

    /**
     * Gets his supervisor, as a {@link Maintainer}.
     * @return the maintainer
     */
    public Maintainer getSupervisor() {
        return this.supervisor;
    }
}
