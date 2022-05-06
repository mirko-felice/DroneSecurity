package it.unibo.dronesecurity.userapplication.auth.entities;

/**
 * Entity representing the Maintainer user.
 */
public final class Maintainer extends BaseLoggedUser {

    /**
     * Build the Maintainer.
     * @param username his username
     */
    public Maintainer(final String username) {
        super(username, Role.MAINTAINER);
    }

}
