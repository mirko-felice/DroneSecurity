/*
 * Copyright (c) 2021-2022, Mirko Felice & Maxim Derevyanchenko. All rights reserved.
 * Licensed under the MIT license. See LICENSE file in the project root for details.
 */

package io.github.dronesecurity.userapplication.auth;

import io.github.dronesecurity.userapplication.auth.entities.Courier;
import io.github.dronesecurity.userapplication.auth.entities.LoggedUser;
import io.github.dronesecurity.userapplication.auth.entities.Maintainer;
import io.github.dronesecurity.userapplication.auth.entities.NotLoggedUser;
import io.github.dronesecurity.userapplication.auth.repo.AuthenticationRepository;
import io.vertx.core.Future;

/**
 * Service dedicated to perform operations related to the authentication concept.
 */
public class AuthenticationService {

    private static final AuthenticationRepository REPOSITORY = AuthenticationRepository.getInstance();
    private static AuthenticationService singleton;

    /**
     * Authenticate a {@link NotLoggedUser} returning a {@link LoggedUser}.
     * @param user the user to authenticate
     * @return the {@link Future} containing the result
     */
    public Future<LoggedUser> authenticate(final NotLoggedUser user) {
        return REPOSITORY.authenticate(user);
    }

    /**
     * Retrieve the {@link Courier} associated with the given username.
     * @param username courier's username
     * @return the {@link Future} containing the result
     */
    public Future<Courier> retrieveCourier(final String username) {
        return REPOSITORY.retrieveCourier(username);
    }

    /**
     * Retrieve the {@link Maintainer} associated with the given username.
     * @param username maintainer's username
     * @return the {@link Future} containing the result
     */
    public Future<Maintainer> retrieveMaintainer(final String username) {
        return REPOSITORY.retrieveMaintainer(username);
    }

    /**
     * Get the Singleton instance.
     * @return the singleton
     */
    public static AuthenticationService getInstance() {
        synchronized (AuthenticationService.class) {
            if (singleton == null)
                singleton = new AuthenticationService();
            return singleton;
        }
    }
}
