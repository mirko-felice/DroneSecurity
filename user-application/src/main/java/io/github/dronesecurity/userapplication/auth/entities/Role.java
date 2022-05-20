/*
 * Copyright (c) 2021-2022, Mirko Felice & Maxim Derevyanchenko. All rights reserved.
 * Licensed under the MIT license. See LICENSE file in the project root for details.
 */

package io.github.dronesecurity.userapplication.auth.entities;

/**
 * Enum representing roles of the {@link BaseLoggedUser}.
 */
public enum Role {
    /**
     * Role representing the Courier user.
     */
    COURIER,
    /**
     * Role representing the Maintainer user.
     */
    MAINTAINER
}
