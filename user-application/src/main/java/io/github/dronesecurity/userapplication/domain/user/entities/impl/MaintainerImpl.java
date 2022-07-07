/*
 * Copyright (c) 2021-2022, Mirko Felice & Maxim Derevyanchenko. All rights reserved.
 * Licensed under the MIT license. See LICENSE file in the project root for details.
 */

package io.github.dronesecurity.userapplication.domain.user.entities.impl;

import io.github.dronesecurity.userapplication.domain.user.entities.contracts.Maintainer;
import io.github.dronesecurity.userapplication.domain.user.entities.contracts.Role;
import io.github.dronesecurity.userapplication.domain.user.objects.Username;

/**
 * Implementation of {@link Maintainer}.
 */
public final class MaintainerImpl extends AbstractUser implements Maintainer {

    /**
     * Build the Maintainer.
     * @param username his {@link Username}
     */
    public MaintainerImpl(final Username username) {
        super(username, Role.MAINTAINER);
    }
}
