/*
 * Copyright (c) 2021-2022, Mirko Felice & Maxim Derevyanchenko. All rights reserved.
 * Licensed under the MIT license. See LICENSE file in the project root for details.
 */

package io.github.dronesecurity.userapplication.domain.reporting.negligence.objects;

import io.github.dronesecurity.lib.shared.ValueObject;
import io.github.dronesecurity.userapplication.domain.reporting.negligence.entities.contracts.NegligenceReport;
import io.github.dronesecurity.userapplication.domain.reporting.negligence.exceptions.EmptyAssigneeException;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

/**
 * {@link ValueObject} representing the assignee of the {@link NegligenceReport}.
 */
public final class Assignee implements ValueObject<Assignee> {

    private final String assigneeUsername;

    private Assignee(final String assigneeUsername) {
        this.validate(assigneeUsername);
        this.assigneeUsername = assigneeUsername;
    }

    /**
     * Parses a string into the assignee.
     * @param value {@link String} to parse
     * @return a new {@link Assignee}
     * @throws EmptyAssigneeException if {@code value} is null or empty
     */
    @Contract(value = "_ -> new", pure = true)
    public static @NotNull Assignee parse(final String value) {
        return new Assignee(value);
    }

    /**
     * Gets the assignee as a {@link String}.
     * @return the string
     */
    public String asString() {
        return this.assigneeUsername;
    }

    /**
     * {@inheritDoc}
     */
    @Contract(pure = true)
    @Override
    public boolean isSameValueAs(final @NotNull Assignee value) {
        return this.assigneeUsername.equals(value.assigneeUsername);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return this.assigneeUsername;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final Assignee assignee = (Assignee) o;
        return this.assigneeUsername.equals(assignee.assigneeUsername);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        return Objects.hash(this.assigneeUsername);
    }

    private void validate(final String value) {
        if (value == null || value.isBlank()) throw new EmptyAssigneeException();
    }
}
