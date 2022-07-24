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

    private final long issueId;

    /**
     * Builds the issue id with the specified identifier.
     * @param issueId the identifier of the issue
     */
    public IssueId(final long issueId) {
        this.issueId = issueId;
    }

    /**
     * Gets the identifier of the issue.
     * @return the identifier of the issue
     */
    public long getIssueId() {
        return this.issueId;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isSameValueAs(final @NotNull IssueId value) {
        return this.issueId == value.getIssueId();
    }
}
