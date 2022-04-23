package it.unibo.dronesecurity.userapplication.utilities;

import it.unibo.dronesecurity.userapplication.auth.entities.BaseUser;

/**
 * Helper class to set and get the current logged {@link BaseUser}.
 */
public final class UserHelper {

    private static BaseUser user;

    private UserHelper() { }

    /**
     * Set the logged user.
     * @param loggedUser the user to set logged
     */
    public static void setLoggedUser(final BaseUser loggedUser) {
        UserHelper.user = loggedUser;
    }

    /**
     * Get the logged user.
     * @return the user logged
     */
    public static BaseUser getLoggedUser() {
        return user;
    }
}
