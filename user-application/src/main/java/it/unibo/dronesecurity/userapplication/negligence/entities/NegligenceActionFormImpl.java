package it.unibo.dronesecurity.userapplication.negligence.entities;

/**
 * Implementation of {@link NegligenceActionForm}.
 */
public class NegligenceActionFormImpl implements NegligenceActionForm {

    private final NegligenceReport report;
    private final String solution;

    /**
     * Build the form.
     * @param report related report
     * @param solution simple solution as text
     */
    public NegligenceActionFormImpl(final NegligenceReport report, final String solution) {
        this.report = report;
        this.solution = solution;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public NegligenceReport getReport() {
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
