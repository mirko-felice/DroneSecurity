/*
 * Copyright (c) 2021-2022, Mirko Felice & Maxim Derevyanchenko. All rights reserved.
 * Licensed under the MIT license. See LICENSE file in the project root for details.
 */

package io.github.dronesecurity.userapplication.domain.reporting.negligence.objects;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.github.dronesecurity.lib.Date;
import io.github.dronesecurity.lib.ValueObject;
import io.github.dronesecurity.userapplication.domain.reporting.negligence.entities.contracts.NegligenceReport;
import io.github.dronesecurity.userapplication.domain.reporting.negligence.exceptions.EmptySolutionException;
import io.github.dronesecurity.userapplication.infrastructure.reporting.negligence.serializers.NegligenceActionFormDeserializer;
import io.github.dronesecurity.userapplication.infrastructure.reporting.negligence.serializers.NegligenceActionFormSerializer;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

/**
 * {@link ValueObject} representing the solution of a {@link NegligenceReport}.
 */
@JsonSerialize(using = NegligenceActionFormSerializer.class)
@JsonDeserialize(using = NegligenceActionFormDeserializer.class)
public final class NegligenceActionForm implements ValueObject<NegligenceActionForm> {

    private final String solution;
    private final Date closingInstant;

    private NegligenceActionForm(final String solution, final Date closingInstant) {
        this.closingInstant = closingInstant;
        this.validate(solution);
        this.solution = solution;
    }

    /**
     * Creates a new action form using its solution.
     * @param solution solution to use
     * @return a new {@link NegligenceActionForm}
     * @throws EmptySolutionException if {@code solution} is null or empty
     */
    @Contract(value = "_ -> new", pure = true)
    public static @NotNull NegligenceActionForm create(final String solution) {
        return new NegligenceActionForm(solution, Date.now());
    }

    /**
     * Parses the parameters to build the action form.
     * @param solution solution to parse
     * @param closingInstant {@link Date} to parse
     * @return a new {@link NegligenceActionForm}
     * @throws EmptySolutionException if {@code solution} is null or empty
     */
    @Contract(value = "_, _ -> new", pure = true)
    public static @NotNull NegligenceActionForm parse(final String solution, final Date closingInstant) {
        return new NegligenceActionForm(solution, closingInstant);
    }

    /**
     * Gets the solution as a string.
     * @return the solution
     */
    public String getSolution() {
        return this.solution;
    }

    /**
     * Gets the instant when the action has been taken.
     * @return the closing instant as a {@link Date}
     */
    public Date getClosingInstant() {
        return this.closingInstant;
    }

    /**
     * {@inheritDoc}
     */
    @Contract(pure = true)
    @Override
    public @NotNull String toString() {
        return "Show Solution";
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isSameValueAs(final @NotNull NegligenceActionForm value) {
        return this.solution.equals(value.solution) && this.closingInstant.equals(value.closingInstant);
    }

    private void validate(final String value) {
        if (value == null || value.isBlank()) throw new EmptySolutionException();
    }
}
