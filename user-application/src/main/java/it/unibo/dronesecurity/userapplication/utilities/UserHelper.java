package it.unibo.dronesecurity.userapplication.utilities;

import it.unibo.dronesecurity.userapplication.auth.entities.User;

/**
 * Helper class to set and get the current logged {@link User}.
 */
public final class UserHelper {

    private static User user;

    private UserHelper() { }

    /**
     * Set the logged user.
     * @param loggedUser the user to set logged
     */
    public static void setLoggedUser(final User loggedUser) {
        UserHelper.user = loggedUser;
    }

    /**
     * Get the logged user.
     * @return the user logged
     */
    public static User getLoggedUser() {
        return user;
    }
}
