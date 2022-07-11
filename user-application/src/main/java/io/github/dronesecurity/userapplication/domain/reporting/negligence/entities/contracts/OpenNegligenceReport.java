/*
 * Copyright (c) 2021-2022, Mirko Felice & Maxim Derevyanchenko. All rights reserved.
 * Licensed under the MIT license. See LICENSE file in the project root for details.
 */

package io.github.dronesecurity.userapplication.domain.reporting.negligence.entities.contracts;

import io.github.dronesecurity.userapplication.domain.reporting.negligence.objects.NegligenceActionForm;

/**
 * Represents OPEN state of the {@link NegligenceReport}.
 */
public interface OpenNegligenceReport extends NegligenceReport {

    /**
     * Takes action using the action form.
     * @param actionForm {@link NegligenceActionForm} used to take action
     */
    void takeAction(NegligenceActionForm actionForm);
}
