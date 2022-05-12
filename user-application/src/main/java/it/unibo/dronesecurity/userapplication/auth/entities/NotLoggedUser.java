package it.unibo.dronesecurity.userapplication.auth.entities;

/**
 * Entity representing a {@link User} not already logged in.
 */
public interface NotLoggedUser extends User {

    /**
     * Get the user's password.
     * @return his password
     */
    String getPassword();
}
