package it.unibo.dronesecurity.userapplication.auth.entities;

/**
 * Entity representing the Maintainer user.
 */
public class Maintainer extends BaseUser {

    /**
     * Build the Maintainer.
     * @param username his username
     * @param password his password
     */
    public Maintainer(final String username, final String password) {
        super(username, password, Role.MAINTAINER);
    }
}
