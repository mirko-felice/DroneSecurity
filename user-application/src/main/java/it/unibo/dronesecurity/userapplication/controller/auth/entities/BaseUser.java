package it.unibo.dronesecurity.userapplication.controller.auth.entities;

import com.fasterxml.jackson.annotation.JsonProperty;
import it.unibo.dronesecurity.userapplication.controller.auth.Role;

/**
 * Base implementation of {@link User}.
 */
public class BaseUser implements User {

    @JsonProperty private final transient String username;
    @JsonProperty private final transient String password;
    @JsonProperty private final transient Role role;

    /**
     * Build the User.
     * @param username his username
     * @param password his password
     * @param role his {@link Role}
     */
    protected BaseUser(final String username, final String password, final Role role) {
        this.username = username;
        this.password = password;
        this.role = role;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getUsername() {
        return this.username;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getPassword() {
        return this.password;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Role getRole() {
        return this.role;
    }
}
