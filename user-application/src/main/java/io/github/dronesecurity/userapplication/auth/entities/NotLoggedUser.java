/*
 * Copyright (c) 2021-2022, Mirko Felice & Maxim Derevyanchenko. All rights reserved.
 * Licensed under the MIT license. See LICENSE file in the project root for details.
 */

package io.github.dronesecurity.userapplication.auth.entities;

/**
 * Entity representing a {@link User} not already logged in.
 */
public interface NotLoggedUser extends User {

    /**
     * Get the user's password.
     * @return his password
     */
    String getPassword();
}
