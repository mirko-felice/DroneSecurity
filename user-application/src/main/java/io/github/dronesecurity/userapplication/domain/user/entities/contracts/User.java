/*
 * Copyright (c) 2021-2022, Mirko Felice & Maxim Derevyanchenko. All rights reserved.
 * Licensed under the MIT license. See LICENSE file in the project root for details.
 */

package io.github.dronesecurity.userapplication.domain.user.entities.contracts;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import io.github.dronesecurity.lib.shared.Entity;
import io.github.dronesecurity.userapplication.domain.user.objects.Username;
import io.github.dronesecurity.userapplication.infrastructure.user.serializers.UserDeserializer;

/**
 * {@link Entity} representing any user of the system.
 */
@JsonDeserialize(using = UserDeserializer.class)
public interface User extends Entity<User> {

    /**
     * Gets the username.
     * @return his {@link Username}
     */
    Username getUsername();

    /**
     * Get the user's role.
     * @return his {@link Role}
     */
    Role getRole();

    /**
     * Checks if the user is logged into the system or not.
     * @return true if the user is logged, false otherwise
     */
    boolean isLogged();

    /**
     * Log in the user.
     */
    void logIn();

    /**
     * Log out the user.
     */
    void logOut();
}
