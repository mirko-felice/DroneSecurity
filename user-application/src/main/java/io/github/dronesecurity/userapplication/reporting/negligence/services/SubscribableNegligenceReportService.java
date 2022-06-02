/*
 * Copyright (c) 2021-2022, Mirko Felice & Maxim Derevyanchenko. All rights reserved.
 * Licensed under the MIT license. See LICENSE file in the project root for details.
 */

package io.github.dronesecurity.userapplication.reporting.negligence.services;

import io.github.dronesecurity.userapplication.events.NewNegligence;

import java.util.function.Consumer;

/**
 * Service capable to subscribe to {@link NewNegligence} events.
 */
public interface SubscribableNegligenceReportService {

    /**
     * Subscribe to {@link NewNegligence} events.
     * @param consumer {@link Consumer} to execute when a {@link NewNegligence} is raised
     */
    void subscribeToNegligenceReports(Consumer<NewNegligence> consumer);
}
