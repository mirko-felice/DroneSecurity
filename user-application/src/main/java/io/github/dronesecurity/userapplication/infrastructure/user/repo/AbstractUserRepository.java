/*
 * Copyright (c) 2021-2022, Mirko Felice & Maxim Derevyanchenko. All rights reserved.
 * Licensed under the MIT license. See LICENSE file in the project root for details.
 */

package io.github.dronesecurity.userapplication.infrastructure.user.repo;

import io.github.dronesecurity.userapplication.domain.user.entities.contracts.Courier;
import io.github.dronesecurity.userapplication.domain.user.entities.contracts.Maintainer;
import io.github.dronesecurity.userapplication.domain.user.entities.contracts.Role;
import io.github.dronesecurity.userapplication.domain.user.entities.contracts.User;
import io.github.dronesecurity.userapplication.domain.user.repo.UserRepository;
import io.github.dronesecurity.userapplication.infrastructure.MongoRepository;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;
import java.util.function.Function;

/**
 * Abstract base implementation of {@link UserRepository} to work with the logged user.
 */
public abstract class AbstractUserRepository extends MongoRepository implements UserRepository {

    private Optional<User> loggedUser = Optional.empty();

    /**
     * {@inheritDoc}
     */
    @Override
    public final void loggedIn(final User user) {
        this.loggedUser = Optional.of(user);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final void loggedOut(final User user) {
        this.loggedUser = Optional.empty();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final Optional<Role> checkLoggedUserRole() {
        return this.loggedUser.map(User::getRole);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final Optional<Courier> retrieveLoggedCourierIfPresent() {
        return this.loggedUser.map(this.mapper(Role.COURIER, Courier.class));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final Optional<Maintainer> retrieveLoggedMaintainerIfPresent() {
        return this.loggedUser.map(this.mapper(Role.MAINTAINER, Maintainer.class));
    }

    @Contract(pure = true)
    private <T extends User> @NotNull Function<User, T> mapper(final Role role, final Class<T> clazz) {
        return user -> {
            if (user.getRole() == role)
                return clazz.cast(user);
            else
                return null;
        };
    }
}
