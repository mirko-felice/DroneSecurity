/*
 * Copyright (c) 2021-2022, Mirko Felice & Maxim Derevyanchenko. All rights reserved.
 * Licensed under the MIT license. See LICENSE file in the project root for details.
 */

package io.github.dronesecurity.userapplication.domain.user.services;

import io.github.dronesecurity.userapplication.domain.user.objects.Username;
import io.github.dronesecurity.userapplication.exceptions.UserAlreadyLoggedException;
import io.github.dronesecurity.userapplication.exceptions.UserNotLoggedException;

/**
 * Domain Service dedicated to authentication.
 */
public interface AuthenticationService {

    /**
     * Log in a user, using his {@link Username} and password.
     * @param username {@link Username} to check
     * @param password password to check
     * @return true if user is authenticated, false otherwise
     * @throws UserAlreadyLoggedException if any user is already logged in
     */
    boolean logIn(Username username, String password);

    /**
     * Log out the user if he's already logged in.
     * @return true if user is logged out, false otherwise
     * @throws UserNotLoggedException if user did not log in previously
     */
    boolean logOut();
}
