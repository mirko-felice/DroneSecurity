package it.unibo.dronesecurity.userapplication.auth.entities;

import java.util.Objects;

/**
 * Implementation of {@link User}.
 */
class BaseUser implements User {

    private final String username;

    /**
     * Build the Base User.
     * @param username his username
     */
    protected BaseUser(final String username) {
        this.username = username;
    }

    @Override
    public String getUsername() {
        return this.username;
    }

    @Override
    public final boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || this.getClass() != o.getClass()) return false;
        final BaseUser baseUser = (BaseUser) o;
        return this.getUsername().equals(baseUser.getUsername());
    }

    @Override
    public final int hashCode() {
        return Objects.hash(this.getUsername());
    }
}
