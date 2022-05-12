package it.unibo.dronesecurity.userapplication.utilities;

import it.unibo.dronesecurity.userapplication.auth.entities.LoggedUser;

/**
 * Helper class to set and get the current {@link LoggedUser}.
 */
public final class UserHelper {

    private static LoggedUser user;

    private UserHelper() { }

    /**
     * Set the logged user.
     * @param loggedUser the user to set logged
     */
    public static void setLoggedUser(final LoggedUser loggedUser) {
        user = loggedUser;
    }

    /**
     * Get the logged user.
     * @return the user logged
     */
    public static LoggedUser getLoggedUser() {
        return user;
    }
}
