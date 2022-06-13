/*
 * Copyright (c) 2021-2022, Mirko Felice & Maxim Derevyanchenko. All rights reserved.
 * Licensed under the MIT license. See LICENSE file in the project root for details.
 */

package io.github.dronesecurity.userapplication.reporting.negligence.repo;

import io.github.dronesecurity.userapplication.auth.entities.Courier;
import io.github.dronesecurity.userapplication.auth.entities.Maintainer;
import io.vertx.core.Future;
import io.github.dronesecurity.userapplication.reporting.negligence.entities.ClosedNegligenceReport;
import io.github.dronesecurity.userapplication.reporting.negligence.entities.NegligenceActionForm;
import io.github.dronesecurity.userapplication.reporting.negligence.entities.NegligenceReport;
import io.github.dronesecurity.userapplication.reporting.negligence.entities.OpenNegligenceReport;

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
     * Take action saving the {@link NegligenceActionForm}.
     * @param form form to be saved
     * @return a {@link Future} to check when action is finished
     */
    Future<Void> takeAction(NegligenceActionForm form);

    /**
     * Retrieve all {@link OpenNegligenceReport} owned to a {@link Courier}.
     * @param username courier username
     * @return the future of the list of all reports
     */
    Future<List<OpenNegligenceReport>> retrieveOpenReportsForCourier(String username);

    /**
     * Retrieve all {@link ClosedNegligenceReport} owned to a {@link Courier}.
     * @param username courier username
     * @return the future of the list of all reports
     */
    Future<List<ClosedNegligenceReport>> retrieveClosedReportsForCourier(String username);

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
     * Get the instance of this repository.
     * @return the instance
     */
    static NegligenceRepository getInstance() {
        return NegligenceRepositoryImpl.getInstance();
    }

}