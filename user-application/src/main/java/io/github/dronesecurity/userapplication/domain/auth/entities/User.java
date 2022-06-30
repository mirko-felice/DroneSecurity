/*
 * Copyright (c) 2021-2022, Mirko Felice & Maxim Derevyanchenko. All rights reserved.
 * Licensed under the MIT license. See LICENSE file in the project root for details.
 */

package io.github.dronesecurity.userapplication.domain.auth.entities;

/**
 * Entity representing any user of the system.
 */
public interface User {

    /**
     * Get the user's username.
     * @return his username
     */
    String getUsername();

}
