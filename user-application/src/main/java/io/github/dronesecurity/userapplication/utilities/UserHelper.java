/*
 * Copyright (c) 2021-2022, Mirko Felice & Maxim Derevyanchenko. All rights reserved.
 * Licensed under the MIT license. See LICENSE file in the project root for details.
 */

package io.github.dronesecurity.userapplication.utilities;

import io.github.dronesecurity.userapplication.auth.entities.LoggedUser;

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
