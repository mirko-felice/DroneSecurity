/*
 * Copyright (c) 2021-2022, Mirko Felice & Maxim Derevyanchenko. All rights reserved.
 * Licensed under the MIT license. See LICENSE file in the project root for details.
 */

package io.github.dronesecurity.userapplication.domain.user.repo;

import io.github.dronesecurity.userapplication.domain.user.entities.contracts.Courier;
import io.github.dronesecurity.userapplication.domain.user.entities.contracts.Maintainer;
import io.github.dronesecurity.userapplication.domain.user.entities.contracts.Role;
import io.github.dronesecurity.userapplication.domain.user.entities.contracts.User;
import io.github.dronesecurity.userapplication.domain.user.objects.Username;

import java.util.Optional;

/**
 * Repository to persist changed on {@link User} aggregate.
 */
public interface UserRepository {

    /**
     * Checks if a user is authenticated.
     * @param username {@link Username} to check
     * @param password password to check
     * @return true if user is authenticated, false otherwise
     */
    boolean isAuthenticated(Username username, String password);

    /**
     * Persist the logged-in user.
     * @param user {@link User} to save
     */
    void loggedIn(User user);

    /**
     * Persist the logged-out user.
     * @param user {@link User} to save
     */
    void loggedOut(User user);

    /**
     * Retrieve the {@link Role} of the {@link User} currently logged into the system if he's present.
     * @return the {@link Optional} containing {@link Role} of the {@link User} if any user is currently logged into
     * the system, {@code null} otherwise
     */
    Optional<Role> checkLoggedUserRole();

    /**
     * Retrieve the {@link Courier} currently logged into the system if he's present.
     * @return the {@link Optional} containing the logged {@link Courier} if present, {@code null} otherwise
     */
    Optional<Courier> retrieveLoggedCourierIfPresent();

    /**
     * Retrieve the {@link Maintainer} currently logged into the system if he's present.
     * @return the {@link Optional} containing the logged {@link Maintainer} if present, {@code null} otherwise
     */
    Optional<Maintainer> retrieveLoggedMaintainerIfPresent();

    /**
     * Retrieve the {@link Courier} associated with the given {@link Username}.
     * @param username courier's {@link Username}
     * @return the {@link Optional} containing the {@link Courier}
     */
    Optional<Courier> retrieveCourierByUsername(Username username);

    /**
     * Retrieve the {@link Maintainer} associated with the given {@link Username}.
     * @param username maintainer's {@link Username}
     * @return the {@link Optional} containing the {@link Maintainer}
     */
    Optional<Maintainer> retrieveMaintainerByUsername(Username username);

    /**
     * Retrieve a generic {@link User} associated with the given {@link Username}.
     * @param username {@link Username} used to retrieve
     * @return the {@link Optional} containing the {@link User}
     */
    Optional<User> retrieveUserByUsername(Username username);
}
