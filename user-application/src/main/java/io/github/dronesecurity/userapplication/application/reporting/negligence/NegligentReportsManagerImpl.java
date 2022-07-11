/*
 * Copyright (c) 2021-2022, Mirko Felice & Maxim Derevyanchenko. All rights reserved.
 * Licensed under the MIT license. See LICENSE file in the project root for details.
 */

package io.github.dronesecurity.userapplication.application.reporting.negligence;

import io.github.dronesecurity.userapplication.domain.reporting.negligence.entities.contracts.ClosedNegligenceReport;
import io.github.dronesecurity.userapplication.domain.reporting.negligence.entities.contracts.OpenNegligenceReport;
import io.github.dronesecurity.userapplication.domain.reporting.negligence.objects.Negligent;
import io.github.dronesecurity.userapplication.domain.reporting.negligence.repo.NegligenceRepository;
import io.github.dronesecurity.userapplication.domain.reporting.negligence.services.NegligentReportsManager;

import java.util.List;

/**
 * Implementation of {@link NegligentReportsManager}.
 */
public final class NegligentReportsManagerImpl implements NegligentReportsManager {

    private final NegligenceRepository negligenceRepository;

    /**
     * Build the negligent reports' manager.
     * @param negligenceRepository {@link NegligenceRepository} to retrieve entities
     */
    public NegligentReportsManagerImpl(final NegligenceRepository negligenceRepository) {
        this.negligenceRepository = negligenceRepository;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<OpenNegligenceReport> retrieveOpenReportsForNegligent(final Negligent negligent) {
        return this.negligenceRepository.retrieveOpenReportsForNegligent(negligent);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<ClosedNegligenceReport> retrieveClosedReportsForNegligent(final Negligent negligent) {
        return this.negligenceRepository.retrieveClosedReportsForNegligent(negligent);
    }
}
