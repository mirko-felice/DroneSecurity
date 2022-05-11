package it.unibo.dronesecurity.userapplication.negligence.services;

import io.vertx.core.Future;
import it.unibo.dronesecurity.userapplication.auth.entities.Courier;
import it.unibo.dronesecurity.userapplication.auth.entities.Maintainer;
import it.unibo.dronesecurity.userapplication.negligence.entities.NegligenceActionForm;

/**
 * Service dedicated to {@link Maintainer} needs, related to negligence reporting.
 */
public interface MaintainerNegligenceReportService extends CourierNegligenceReportService {

    /**
     * Takes action against a {@link Courier} using a {@link NegligenceActionForm}.
     * @param form the form to use
     * @return a {@link Future} to check when action is finished
     */
    Future<Void> takeAction(NegligenceActionForm form);

    /**
     * Get the instance of this service.
     * @return the instance
     */
    static MaintainerNegligenceReportService getInstance() {
        return NegligenceReportService.getInstance();
    }
}
