package it.unibo.dronesecurity.userapplication.negligence.services;

import it.unibo.dronesecurity.userapplication.auth.entities.Courier;
import it.unibo.dronesecurity.userapplication.auth.entities.Maintainer;
import it.unibo.dronesecurity.userapplication.negligence.report.NegligenceActionForm;

/**
 * Service dedicated to {@link Maintainer} needs, related to negligence reporting.
 */
public interface MaintainerNegligenceReportService extends CourierNegligenceReportService {

    /**
     * Takes action against a {@link Courier} using a {@link NegligenceActionForm}.
     * @param form the form to use
     */
    void takeAction(NegligenceActionForm form);

    /**
     * Get the instance of this service.
     * @return the instance
     */
    static MaintainerNegligenceReportService getInstance() {
        return NegligenceReportService.getInstance();
    }
}
