/*
 * Copyright (c) 2021-2022, Mirko Felice & Maxim Derevyanchenko. All rights reserved.
 * Licensed under the MIT license. See LICENSE file in the project root for details.
 */

package io.github.dronesecurity.userapplication.negligence.services;

import io.github.dronesecurity.userapplication.auth.entities.Courier;
import io.github.dronesecurity.userapplication.auth.entities.Maintainer;
import io.vertx.core.Future;
import io.github.dronesecurity.userapplication.negligence.entities.NegligenceActionForm;

/**
 * Service dedicated to {@link Maintainer} needs, related to negligence reporting.
 */
public interface MaintainerNegligenceReportService extends CourierNegligenceReportService {

    /**
     * Takes action against a {@link Courier} using a {@link NegligenceActionForm}.
     * @param form the form to use
     * @return a {@link Future} to check when action is finished
     */
    Future<Void> takeAction(NegligenceActionForm form);

    /**
     * Get the instance of this service.
     * @return the instance
     */
    static MaintainerNegligenceReportService getInstance() {
        return NegligenceReportService.getInstance();
    }
}
