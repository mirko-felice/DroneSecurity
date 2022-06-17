/*
 * Copyright (c) 2021-2022, Mirko Felice & Maxim Derevyanchenko. All rights reserved.
 * Licensed under the MIT license. See LICENSE file in the project root for details.
 */

package io.github.dronesecurity.userapplication.reporting.negligence.entities;

/**
 * Represents the solution of a {@link NegligenceReport}.
 */
public interface NegligenceSolution {

    /**
     * Gets the solution as a string.
     * @return the solution
     */
    String getSolution();
}
