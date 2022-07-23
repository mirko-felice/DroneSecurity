/*
 * Copyright (c) 2021-2022, Mirko Felice & Maxim Derevyanchenko. All rights reserved.
 * Licensed under the MIT license. See LICENSE file in the project root for details.
 */

package io.github.dronesecurity.dronesystem.drone.domain.drone.order.objects;

import io.github.dronesecurity.lib.shared.ValueObject;

/**
 * Class representing the path that the drone needs to follow to perform the delivery.
 */
public final class Path implements ValueObject<Path> {

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isSameValueAs(final Path value) {
        return false;
    }
}
