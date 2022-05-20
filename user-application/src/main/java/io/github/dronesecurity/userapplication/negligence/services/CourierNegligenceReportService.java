/*
 * Copyright (c) 2021-2022, Mirko Felice & Maxim Derevyanchenko. All rights reserved.
 * Licensed under the MIT license. See LICENSE file in the project root for details.
 */

package io.github.dronesecurity.userapplication.negligence.services;

import io.github.dronesecurity.userapplication.auth.entities.Courier;
import io.github.dronesecurity.userapplication.events.NewNegligence;

import java.util.function.Consumer;

/**
 * Service dedicated to {@link Courier} needs, related to negligence reporting.
 */
public interface CourierNegligenceReportService {

    /**
     * Subscribe to {@link NewNegligence} events.
     * @param consumer {@link Consumer} to execute when a {@link NewNegligence} is raised
     */
    void subscribeToNegligenceReports(Consumer<NewNegligence> consumer);

    // TODO fare le chiamate al repo tramite il service

    /**
     * Get the instance of this service.
     * @return the instance
     */
    static CourierNegligenceReportService getInstance() {
        return NegligenceReportService.getInstance();
    }
}
