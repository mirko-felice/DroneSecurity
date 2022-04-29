package it.unibo.dronesecurity.userapplication.negligence.report;

import io.vertx.core.Future;
import it.unibo.dronesecurity.userapplication.auth.entities.Courier;

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
     */
    void takeAction(NegligenceActionForm form);

    /**
     * Retrive all reports owned to a {@link Courier}.
     * @param username courier username
     * @return the future of the list of all reports
     */
    Future<List<NegligenceReport>> retrieveReportsForCourier(String username);

    /**
     * Get the instance of this repository.
     * @return the instance
     */
    static NegligenceRepository getInstance() {
        return NegligenceRepositoryImpl.getInstance();
    }
}
