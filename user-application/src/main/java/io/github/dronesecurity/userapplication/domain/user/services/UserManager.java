/*
 * Copyright (c) 2021-2022, Mirko Felice & Maxim Derevyanchenko. All rights reserved.
 * Licensed under the MIT license. See LICENSE file in the project root for details.
 */

package io.github.dronesecurity.userapplication.domain.user.services;

import io.github.dronesecurity.userapplication.domain.user.entities.contracts.Courier;
import io.github.dronesecurity.userapplication.domain.user.entities.contracts.Maintainer;
import io.github.dronesecurity.userapplication.domain.user.entities.contracts.Role;
import io.github.dronesecurity.userapplication.domain.user.entities.contracts.User;
import io.github.dronesecurity.userapplication.domain.user.objects.Username;

import java.util.Optional;

/**
 * Domain Service dedicated to manage users.
 */
public interface UserManager {

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
}
