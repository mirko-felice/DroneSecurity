package it.unibo.dronesecurity.userapplication.utilities;

import it.unibo.dronesecurity.userapplication.negligence.entities.NegligenceReport;

/**
 * Exception thrown if {@link NegligenceReport} is building without providing data.
 */
public class ReportEmptyDataException extends IllegalArgumentException {

    private static final long serialVersionUID = -5999987812227600513L;

    /**
     * Build the exception.
     */
    public ReportEmptyDataException() {
        super("Can not close a report not providing data.");
    }
}
