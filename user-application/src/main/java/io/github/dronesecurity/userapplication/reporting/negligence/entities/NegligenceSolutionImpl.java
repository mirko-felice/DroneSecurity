/*
 * Copyright (c) 2021-2022, Mirko Felice & Maxim Derevyanchenko. All rights reserved.
 * Licensed under the MIT license. See LICENSE file in the project root for details.
 */

package io.github.dronesecurity.userapplication.reporting.negligence.entities;

import java.util.Objects;

/**
 * Implementation of {@link NegligenceSolution}.
 */
public final class NegligenceSolutionImpl implements NegligenceSolution {

    private final String solution;

    /**
     * Build the solution.
     * @param solution solution text
     */
    public NegligenceSolutionImpl(final String solution) {
        this.solution = solution;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getSolution() {
        return this.solution;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return "Show Solution";
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || this.getClass() != o.getClass()) return false;
        final NegligenceSolutionImpl that = (NegligenceSolutionImpl) o;
        return this.getSolution().equals(that.getSolution());
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.getSolution());
    }
}
