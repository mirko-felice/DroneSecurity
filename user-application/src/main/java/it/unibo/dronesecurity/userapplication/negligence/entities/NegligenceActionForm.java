package it.unibo.dronesecurity.userapplication.negligence.entities;

import it.unibo.dronesecurity.userapplication.auth.entities.Courier;

/**
 * Action form to be used to take action against a {@link Courier}.
 */
public interface NegligenceActionForm {

    /**
     * Gets the report to update.
     * @return the {@link NegligenceReport}
     */
    NegligenceReport getReport();

    /**
     * Simple textual representation of assigner solution.
     * @return the solution as a {@link String}
     */
    String getSolution();
    // TODO think about real solutions
}