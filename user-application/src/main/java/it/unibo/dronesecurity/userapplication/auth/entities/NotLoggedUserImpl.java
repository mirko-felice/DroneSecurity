package it.unibo.dronesecurity.userapplication.auth.entities;

/**
 * Implementation of {@link NotLoggedUser} extending {@link BaseUser}.
 */
public class NotLoggedUserImpl extends BaseUser implements NotLoggedUser {

    private final String password;

    /**
     * Build the user.
     * @param username his username
     * @param password his password
     */
    public NotLoggedUserImpl(final String username, final String password) {
        super(username);
        this.password = password;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getPassword() {
        return this.password;
    }

}
