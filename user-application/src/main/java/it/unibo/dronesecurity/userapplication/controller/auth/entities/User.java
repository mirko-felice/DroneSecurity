package it.unibo.dronesecurity.userapplication.controller.auth.entities;

import it.unibo.dronesecurity.userapplication.controller.auth.Role;

/**
 * Entity representing any user of the system.
 */
public interface User {

    /**
     * Get the user's username.
     * @return his username
     */
    String getUsername();

    /**
     * Get the user's password.
     * @return his password
     */
    String getPassword();

    /**
     * Get the user's role.
     * @return his role
     */
    Role getRole();
}
