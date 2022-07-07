/*
 * Copyright (c) 2021-2022, Mirko Felice & Maxim Derevyanchenko. All rights reserved.
 * Licensed under the MIT license. See LICENSE file in the project root for details.
 */

package io.github.dronesecurity.userapplication.application.user.ohs.pl;

/**
 * Enum to represent possible roles of the {@link GenericUser}.
 */
public enum UserRole {

    /**
     * Indicates user is a courier.
     */
    COURIER,

    /**
     * Indicated user is a maintainer.
     */
    MAINTAINER,

    /**
     * Indicates user is not logged.
     */
    NOT_LOGGED
}
