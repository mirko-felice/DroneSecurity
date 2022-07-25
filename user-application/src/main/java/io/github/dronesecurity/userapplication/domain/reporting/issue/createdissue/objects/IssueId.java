/*
 * Copyright (c) 2021-2022, Mirko Felice & Maxim Derevyanchenko. All rights reserved.
 * Licensed under the MIT license. See LICENSE file in the project root for details.
 */

package io.github.dronesecurity.userapplication.domain.reporting.issue.createdissue.objects;

import io.github.dronesecurity.lib.shared.ValueObject;
import org.jetbrains.annotations.NotNull;

/**
 * Value Object representing the identifier of an issue.
 */
public class IssueId implements ValueObject<IssueId> {

    private final long id;

    /**
     * Builds the issue id with the specified identifier.
     * @param id the identifier of the issue
     */
    public IssueId(final long id) {
        this.id = id;
    }

    /**
     * Gets the identifier of the issue.
     * @return the identifier of the issue
     */
    public long getId() {
        return this.id;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isSameValueAs(final @NotNull IssueId value) {
        return this.id == value.getId();
    }
}
