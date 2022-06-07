/*
 * Copyright (c) 2021-2022, Mirko Felice & Maxim Derevyanchenko. All rights reserved.
 * Licensed under the MIT license. See LICENSE file in the project root for details.
 */

package io.github.dronesecurity.userapplication.reporting.negligence.services;

import io.github.dronesecurity.userapplication.auth.entities.Courier;
import io.github.dronesecurity.userapplication.auth.entities.Maintainer;
import io.github.dronesecurity.userapplication.events.NewNegligence;
import io.github.dronesecurity.userapplication.reporting.negligence.entities.ClosedNegligenceReport;
import io.github.dronesecurity.userapplication.reporting.negligence.entities.OpenNegligenceReport;
import io.vertx.core.Future;
import io.github.dronesecurity.userapplication.reporting.negligence.entities.NegligenceActionForm;

import java.util.List;
import java.util.function.Consumer;

/**
 * Service dedicated to {@link Maintainer} needs, related to negligence reporting.
 */
public interface MaintainerNegligenceReportService {

    /**
     * Retrieve all {@link OpenNegligenceReport} assigned to a {@link Maintainer}.
     * @param username maintainer username
     * @return the future of the list of all reports
     */
    Future<List<OpenNegligenceReport>> retrieveOpenReportsForMaintainer(String username);

    /**
     * Retrieve all {@link ClosedNegligenceReport} owned to a {@link Maintainer}.
     * @param username maintainer username
     * @return the future of the list of all reports
     */
    Future<List<ClosedNegligenceReport>> retrieveClosedReportsForMaintainer(String username);

    /**
     * Takes action against a {@link Courier} using a {@link NegligenceActionForm}.
     * @param form the form to use
     * @return a {@link Future} to check when action is finished
     */
    Future<Void> takeAction(NegligenceActionForm form);

    /**
     * Subscribes to {@link NewNegligence} events.
     * @param courier {@link Courier} username to receive {@link NewNegligence} from
     * @param consumer {@link Consumer} to execute when a {@link NewNegligence} is raised
     */
    void subscribeToCourierNegligence(String courier, Consumer<NewNegligence> consumer);

    /**
     * Get the instance of this service.
     * @return the instance
     */
    static MaintainerNegligenceReportService getInstance() {
        return NegligenceReportService.getInstance();
    }
}
