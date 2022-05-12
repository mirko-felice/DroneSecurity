package it.unibo.dronesecurity.userapplication.negligence.services;

import it.unibo.dronesecurity.userapplication.auth.entities.Courier;
import it.unibo.dronesecurity.userapplication.events.DomainEvents;
import it.unibo.dronesecurity.userapplication.events.NewNegligence;

/**
 * Service dedicated to {@link Courier} needs, related to negligence reporting.
 */
public interface CourierNegligenceReportService {

    /**
     * Subscribe to {@link NewNegligence} events.
     * @param domainEvents domain to raise events on
     */
    void subscribeToNegligenceReports(DomainEvents<NewNegligence> domainEvents);

    /**
     * Get the instance of this service.
     * @return the instance
     */
    static CourierNegligenceReportService getInstance() {
        return NegligenceReportService.getInstance();
    }
}
