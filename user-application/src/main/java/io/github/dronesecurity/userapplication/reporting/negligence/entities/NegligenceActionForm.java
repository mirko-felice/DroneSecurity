/*
 * Copyright (c) 2021-2022, Mirko Felice & Maxim Derevyanchenko. All rights reserved.
 * Licensed under the MIT license. See LICENSE file in the project root for details.
 */

package io.github.dronesecurity.userapplication.reporting.negligence.entities;

import io.github.dronesecurity.userapplication.auth.entities.Courier;

/**
 * Action form to be used to take action against a {@link Courier}.
 */
public interface NegligenceActionForm {

    /**
     * Gets the report to update.
     * @return the {@link NegligenceReportWithID}
     */
    NegligenceReportWithID getReport();

    /**
     * Simple textual representation of assignee solution.
     * @return the solution as a {@link String}
     */
    String getSolution();
    // TODO think about real solutions
}
