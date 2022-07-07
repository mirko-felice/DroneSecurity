/*
 * Copyright (c) 2021-2022, Mirko Felice & Maxim Derevyanchenko. All rights reserved.
 * Licensed under the MIT license. See LICENSE file in the project root for details.
 */

package io.github.dronesecurity.userapplication.application.user;

import io.github.dronesecurity.userapplication.domain.user.entities.contracts.Courier;
import io.github.dronesecurity.userapplication.domain.user.entities.contracts.Maintainer;
import io.github.dronesecurity.userapplication.domain.user.entities.contracts.Role;
import io.github.dronesecurity.userapplication.domain.user.objects.Username;
import io.github.dronesecurity.userapplication.domain.user.repo.UserRepository;
import io.github.dronesecurity.userapplication.domain.user.services.UserManager;

import java.util.Optional;

/**
 * Implementation of {@link UserManager}.
 */
public final class UserManagerImpl implements UserManager {

    private final UserRepository repository;

    /**
     * Build the user manager.
     * @param repository {@link UserRepository} to retrieve entities
     */
    public UserManagerImpl(final UserRepository repository) {
        this.repository = repository;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<Courier> retrieveCourierByUsername(final Username username) {
        return this.repository.retrieveCourierByUsername(username);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<Maintainer> retrieveMaintainerByUsername(final Username username) {
        return this.repository.retrieveMaintainerByUsername(username);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<Role> checkLoggedUserRole() {
        return this.repository.checkLoggedUserRole();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<Courier> retrieveLoggedCourierIfPresent() {
        return this.repository.retrieveLoggedCourierIfPresent();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<Maintainer> retrieveLoggedMaintainerIfPresent() {
        return this.repository.retrieveLoggedMaintainerIfPresent();
    }

}
