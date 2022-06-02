/*
 * Copyright (c) 2021-2022, Mirko Felice & Maxim Derevyanchenko. All rights reserved.
 * Licensed under the MIT license. See LICENSE file in the project root for details.
 */

package io.github.dronesecurity.userapplication.reporting.negligence.entities;

/**
 * Implementation of {@link NegligenceActionForm}.
 */
public class NegligenceActionFormImpl implements NegligenceActionForm {

    private final NegligenceReportWithID report;
    private final String solution;

    /**
     * Build the form.
     * @param report related report
     * @param solution simple solution as text
     */
    public NegligenceActionFormImpl(final NegligenceReportWithID report, final String solution) {
        this.report = report;
        this.solution = solution;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public NegligenceReportWithID getReport() {
        return this.report;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getSolution() {
        return this.solution;
    }

}
