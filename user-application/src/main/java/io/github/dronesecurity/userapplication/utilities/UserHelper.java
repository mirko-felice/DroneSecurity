/*
 * Copyright (c) 2021-2022, Mirko Felice & Maxim Derevyanchenko. All rights reserved.
 * Licensed under the MIT license. See LICENSE file in the project root for details.
 */

package io.github.dronesecurity.userapplication.utilities;

import io.github.dronesecurity.userapplication.domain.auth.entities.LoggedUser;
import io.github.dronesecurity.userapplication.exceptions.UserAlreadyLoggedException;
import io.github.dronesecurity.userapplication.exceptions.UserNotLoggedException;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

/**
 * Helper class to set and get the current {@link LoggedUser}.
 */
public final class UserHelper {

    private static Optional<LoggedUser> user = Optional.empty();

    private UserHelper() { }

    /**
     * Set the logged user.
     * @param loggedUser the user logged in
     */
    public static void loggedIn(final @NotNull LoggedUser loggedUser) {
        if (user.isPresent())
            throw new UserAlreadyLoggedException();
        user = Optional.of(loggedUser);
    }

    /**
     * Get the logged user.
     * @return the user logged
     */
    public static @NotNull LoggedUser logged() {
        if (user.isEmpty())
            throw new UserNotLoggedException();
        return user.get();
    }

    /**
     * Logout the current user.
     */
    public static void logout() {
        user = Optional.empty();
    }
}
