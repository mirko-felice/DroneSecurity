/*
 * Copyright (c) 2021-2022, Mirko Felice & Maxim Derevyanchenko. All rights reserved.
 * Licensed under the MIT license. See LICENSE file in the project root for details.
 */

package io.github.dronesecurity.userapplication.domain.reporting.negligence.entities.contracts;

import io.github.dronesecurity.userapplication.domain.reporting.negligence.objects.NegligenceActionForm;

/**
 * Represents CLOSED state of the {@link NegligenceReport}.
 */
public interface ClosedNegligenceReport extends NegligenceReport {

    /**
     * Gets the solution used to take action.
     * @return the {@link NegligenceActionForm}
     */
    NegligenceActionForm getActionForm();
}
