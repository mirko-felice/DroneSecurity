/*
 * Copyright (c) 2021-2022, Mirko Felice & Maxim Derevyanchenko. All rights reserved.
 * Licensed under the MIT license. See LICENSE file in the project root for details.
 */

package io.github.dronesecurity.userapplication.domain.auth.repo;

import io.github.dronesecurity.userapplication.domain.auth.entities.*;
import io.vertx.core.Future;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

/**
 * Repository to perform different actions on {@link User} entity.
 */
public interface AuthenticationRepository {

    /**
     * Authenticate a {@link NotLoggedUser} returning a {@link LoggedUser}.
     * @param user the user to authenticate
     * @return the {@link Future} containing the result
     */
    Future<LoggedUser> authenticate(NotLoggedUser user);

    /**
     * Retrieve the {@link Courier} associated with the given username.
     * @param username courier's username
     * @return the {@link Future} containing the result
     */
    Future<Courier> retrieveCourier(String username);

    /**
     * Retrieve the {@link Maintainer} associated with the given username.
     * @param username maintainer's username
     * @return the {@link Future} containing the result
     */
    Future<Maintainer> retrieveMaintainer(String username);

    /**
     * Get the instance of this repository.
     * @return the instance
     */
    @Contract(value = " -> new", pure = true)
    static @NotNull AuthenticationRepository getInstance() {
        return AuthenticationRepositoryImpl.getInstance();
    }
}
