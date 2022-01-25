package it.unibo.dronesecurity.userapplication.utilities;

import it.unibo.dronesecurity.userapplication.controller.auth.entities.BaseUser;

/**
 * Helper class to set and get the current logged {@link BaseUser}.
 */
public final class LoggedUser {

    private static BaseUser user;

    private LoggedUser() { }

    /**
     * Set the logged user.
     * @param loggedUser the user to set logged
     */
    public static void set(final BaseUser loggedUser) {
        LoggedUser.user = loggedUser;
    }

    /**
     * Get the logged user.
     * @return the user logged
     */
    public BaseUser get() {
        return user;
    }
}
