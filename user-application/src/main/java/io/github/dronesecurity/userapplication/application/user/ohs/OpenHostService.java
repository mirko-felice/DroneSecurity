/*
 * Copyright (c) 2021-2022, Mirko Felice & Maxim Derevyanchenko. All rights reserved.
 * Licensed under the MIT license. See LICENSE file in the project root for details.
 */

package io.github.dronesecurity.userapplication.application.user.ohs;

import io.github.dronesecurity.userapplication.application.user.ohs.pl.Courier;
import io.github.dronesecurity.userapplication.application.user.ohs.pl.GenericUser;
import io.github.dronesecurity.userapplication.application.user.ohs.pl.UserRole;
import io.github.dronesecurity.userapplication.domain.user.entities.contracts.Role;
import io.github.dronesecurity.userapplication.domain.user.entities.contracts.User;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

/**
 * OHS to conform internal implementations to external usages.
 */
public final class OpenHostService {

    private OpenHostService() { }

    /**
     * Convert domain user into the generic user.
     * @param user {@link User} to convert
     * @return the {@link GenericUser}
     */
    @Contract("_ -> new")
    public static @NotNull GenericUser convertToGenericUser(final @NotNull User user) {
        return new GenericUser(user.getUsername().asString(), convertToUserRole(user.getRole()));
    }

    /**
     * Convert domain role into the user role.
     * @param role {@link Role} to parse
     * @return the {@link UserRole}
     */
    public static UserRole convertToUserRole(final @NotNull Role role) {
        return UserRole.valueOf(role.name());
    }

    /**
     * Convert domain courier into external courier.
     * @param courier {@link io.github.dronesecurity.userapplication.domain.user.entities.contracts.Courier} to parse}
     * @return the {@link Courier}
     */
    @Contract("_ -> new")
    public static @NotNull Courier convertToCourier(
            final io.github.dronesecurity.userapplication.domain.user.entities.contracts.@NotNull Courier courier) {
        return new Courier(
                courier.getUsername().asString(),
                courier.supervisorUsername().asString(),
                courier.assignedDrones());
    }
}
