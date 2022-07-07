/*
 * Copyright (c) 2021-2022, Mirko Felice & Maxim Derevyanchenko. All rights reserved.
 * Licensed under the MIT license. See LICENSE file in the project root for details.
 */

package io.github.dronesecurity.userapplication.domain.reporting.negligence.repo;

import io.github.dronesecurity.userapplication.domain.user.entities.impl.CourierImpl;
import io.github.dronesecurity.userapplication.domain.user.entities.impl.MaintainerImpl;
import io.github.dronesecurity.userapplication.domain.reporting.negligence.entities.ClosedNegligenceReport;
import io.github.dronesecurity.userapplication.domain.reporting.negligence.entities.NegligenceReport;
import io.github.dronesecurity.userapplication.domain.reporting.negligence.entities.NegligenceSolution;
import io.github.dronesecurity.userapplication.domain.reporting.negligence.entities.OpenNegligenceReport;
import io.vertx.core.Future;

import java.util.List;

/**
 * Repository to perform different actions on {@link NegligenceReport} entity.
 */
public interface NegligenceRepository {

    /**
     * Saves the report.
     * @param report the report to save
     */
    void createReport(NegligenceReport report);

    /**
     * Take action closing the report.
     * @param report report to close
     * @param solution {@link NegligenceSolution} used to close the report
     * @return a {@link Future} to check when action is finished
     */
    Future<Void> takeAction(OpenNegligenceReport report, NegligenceSolution solution);

    /**
     * Retrieve all {@link OpenNegligenceReport} owned to a {@link CourierImpl}.
     * @param username courier username
     * @return the future of the list of all reports
     */
    Future<List<OpenNegligenceReport>> retrieveOpenReportsForCourier(String username);

    /**
     * Retrieve all {@link ClosedNegligenceReport} owned to a {@link CourierImpl}.
     * @param username courier username
     * @return the future of the list of all reports
     */
    Future<List<ClosedNegligenceReport>> retrieveClosedReportsForCourier(String username);

    /**
     * Retrieve all {@link OpenNegligenceReport} assigned to a {@link MaintainerImpl}.
     * @param username maintainer username
     * @return the future of the list of all reports
     */
    Future<List<OpenNegligenceReport>> retrieveOpenReportsForMaintainer(String username);

    /**
     * Retrieve all {@link ClosedNegligenceReport} owned to a {@link MaintainerImpl}.
     * @param username maintainer username
     * @return the future of the list of all reports
     */
    Future<List<ClosedNegligenceReport>> retrieveClosedReportsForMaintainer(String username);

    /**
     * Get the instance of this repository.
     * @return the instance
     */
    static NegligenceRepository getInstance() {
        return NegligenceRepositoryImpl.getInstance();
    }

}
