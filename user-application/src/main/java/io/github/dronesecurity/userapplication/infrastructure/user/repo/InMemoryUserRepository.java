/*
 * Copyright (c) 2021-2022, Mirko Felice & Maxim Derevyanchenko. All rights reserved.
 * Licensed under the MIT license. See LICENSE file in the project root for details.
 */

package io.github.dronesecurity.userapplication.infrastructure.user.repo;

import io.github.dronesecurity.userapplication.domain.user.entities.contracts.Courier;
import io.github.dronesecurity.userapplication.domain.user.entities.contracts.Maintainer;
import io.github.dronesecurity.userapplication.domain.user.entities.contracts.Role;
import io.github.dronesecurity.userapplication.domain.user.entities.contracts.User;
import io.github.dronesecurity.userapplication.domain.user.entities.impl.CourierImpl;
import io.github.dronesecurity.userapplication.domain.user.entities.impl.MaintainerImpl;
import io.github.dronesecurity.userapplication.domain.user.objects.Username;
import io.github.dronesecurity.userapplication.domain.user.repo.UserRepository;
import io.github.dronesecurity.userapplication.utilities.user.PasswordHelper;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Implementation of non-persistent {@link UserRepository} useful to test business logic.
 */
public final class InMemoryUserRepository extends AbstractUserRepository {

    private final List<User> users = new ArrayList<>();
    private final String courierPassword;
    private final String maintainerPassword;

    /**
     * Build the repo, initializing default values.
     */
    public InMemoryUserRepository() {
        this.courierPassword = PasswordHelper.createHash("courierPassword");
        this.maintainerPassword = PasswordHelper.createHash("maintainerPassword");
        final Username maintainerUsername = Username.parse("maintainer");
        this.users.add(new MaintainerImpl(maintainerUsername));
        this.users.add(new CourierImpl(Username.parse("courier"), maintainerUsername, new ArrayList<>()));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isAuthenticated(final Username username, final String password) {
        return this.users.stream().anyMatch(u -> {
            final String storedPassword;
            if (u.getRole() == Role.COURIER)
                storedPassword = this.courierPassword;
            else
                storedPassword = this.maintainerPassword;
            return u.getUsername().isSameValueAs(username) && PasswordHelper.validatePassword(password, storedPassword);
        });
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public @NotNull Optional<Courier> retrieveCourierByUsername(final Username username) {
        return this.users.stream().filter(u -> u.getUsername().isSameValueAs(username)).findFirst()
                .map(Courier.class::cast);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public @NotNull Optional<Maintainer> retrieveMaintainerByUsername(final Username username) {
        return this.users.stream().filter(u -> u.getUsername().isSameValueAs(username)).findFirst()
                .map(Maintainer.class::cast);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public @NotNull Optional<User> retrieveUserByUsername(final Username username) {
        return this.users.stream().filter(u -> u.getUsername().isSameValueAs(username)).findFirst();
    }


}
