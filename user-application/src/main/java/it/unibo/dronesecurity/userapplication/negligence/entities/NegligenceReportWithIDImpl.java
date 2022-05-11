package it.unibo.dronesecurity.userapplication.negligence.entities;

import org.jetbrains.annotations.NotNull;

/**
 * Implementation of {@link NegligenceReportWithID}.
 */
class NegligenceReportWithIDImpl extends NegligenceReportImpl implements NegligenceReportWithID {

    private final long id;

    NegligenceReportWithIDImpl(final long id, final @NotNull NegligenceReport report) {
        super(report.getNegligent(), report.assignedTo(), report.getData());
        this.id = id;
    }

    @Override
    public long getId() {
        return this.id;
    }
}
