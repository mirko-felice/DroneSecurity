package it.unibo.dronesecurity.userapplication.auth.entities;

/**
 * Entity representing the Courier user.
 */
public class Courier extends BaseUser {

    /**
     * Build the Courier.
     * @param username his username
     * @param password his password
     */
    public Courier(final String username, final String password) {
        super(username, password, Role.COURIER);
    }
}
