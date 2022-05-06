package it.unibo.dronesecurity.userapplication.auth.entities;

/**
 * Entity representing the Courier user, extending {@link BaseLoggedUser}.
 */
public final class Courier extends BaseLoggedUser {

    private final String supervisor;

    /**
     * Build the Courier.
     * @param username his username
     * @param supervisor his supervisor
     */
    public Courier(final String username, final String supervisor) {
        super(username, Role.COURIER);
        this.supervisor = supervisor;
    }

    /**
     * Gets his supervisor as a {@link String}.
     * @return the maintainer
     */
    public String getSupervisor() {
        return this.supervisor;
    }
}
