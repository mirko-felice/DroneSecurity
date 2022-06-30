/*
 * Copyright (c) 2021-2022, Mirko Felice & Maxim Derevyanchenko. All rights reserved.
 * Licensed under the MIT license. See LICENSE file in the project root for details.
 */

package io.github.dronesecurity.userapplication.domain.auth.entities;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import io.github.dronesecurity.userapplication.domain.auth.serializers.LoggedUserDeserializer;

/**
 * Entity representing a {@link User} already logged in.
 */
@JsonDeserialize(using = LoggedUserDeserializer.class)
public interface LoggedUser extends User {

    /**
     * Get the user's role.
     * @return his role
     */
    Role getRole();
}
