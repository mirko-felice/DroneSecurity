/*
 * Copyright (c) 2021-2022, Mirko Felice & Maxim Derevyanchenko. All rights reserved.
 * Licensed under the MIT license. See LICENSE file in the project root for details.
 */

package io.github.dronesecurity.userapplication.application.user;

import io.github.dronesecurity.userapplication.domain.user.entities.contracts.Role;
import io.github.dronesecurity.userapplication.domain.user.entities.contracts.User;
import io.github.dronesecurity.userapplication.domain.user.events.LoggedIn;
import io.github.dronesecurity.userapplication.domain.user.events.LoggedOut;
import io.github.dronesecurity.userapplication.domain.user.objects.Username;
import io.github.dronesecurity.userapplication.domain.user.repo.UserRepository;
import io.github.dronesecurity.userapplication.domain.user.services.AuthenticationService;
import io.github.dronesecurity.userapplication.events.DomainEvents;
import io.github.dronesecurity.userapplication.domain.user.exceptions.UserAlreadyLoggedException;
import io.github.dronesecurity.userapplication.domain.user.exceptions.UserNotLoggedException;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;
import java.util.function.Consumer;

/**
 * Implementation of {@link AuthenticationService}.
 */
public final class AuthenticationServiceImpl implements AuthenticationService {

    private final UserRepository userRepository;
    private final Consumer<LoggedIn> loggedInHandler;
    private final Consumer<LoggedOut> loggedOutHandler;

    /**
     * Build the service.
     * @param userRepository {@link UserRepository} to persist changes
     */
    public AuthenticationServiceImpl(final UserRepository userRepository) {
        this.userRepository = userRepository;
        this.loggedInHandler = this::loggedIn;
        this.loggedOutHandler = this::loggedOut;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean logIn(final Username username, final String password) {
        if (this.userRepository.checkLoggedUserRole().isEmpty()) {
            final boolean isAuthenticated = this.userRepository.isAuthenticated(username, password);
            if (isAuthenticated) {
                final User user = this.userRepository.retrieveUserByUsername(username).orElseThrow();
                DomainEvents.register(LoggedIn.class, this.loggedInHandler);
                user.logIn();
            }
            return isAuthenticated;
        } else
            throw new UserAlreadyLoggedException();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean logOut() {
        final Optional<Role> loggedUserRole = this.userRepository.checkLoggedUserRole();
        if (loggedUserRole.isEmpty())
            throw new UserNotLoggedException();
        else {
            DomainEvents.register(LoggedOut.class, this.loggedOutHandler);
            final User user;
            switch (loggedUserRole.get()) {
                case MAINTAINER:
                    user = this.userRepository.retrieveLoggedMaintainerIfPresent().orElseThrow();
                    break;
                case COURIER:
                    user = this.userRepository.retrieveLoggedCourierIfPresent().orElseThrow();
                    break;
                default:
                    return false;
            }
            user.logOut();
            return true;
        }
    }

    private void loggedIn(final @NotNull LoggedIn loggedInEvent) {
        this.userRepository.loggedIn(loggedInEvent.getUser());
        DomainEvents.unregister(LoggedIn.class, this.loggedInHandler);
    }

    private void loggedOut(final @NotNull LoggedOut loggedOutEvent) {
        this.userRepository.loggedOut(loggedOutEvent.getUser());
        DomainEvents.unregister(LoggedOut.class, this.loggedOutHandler);
    }
}
