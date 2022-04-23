package it.unibo.dronesecurity.userapplication.negligence.report;

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
     * Get the instance of this repository.
     * @return the instance
     */
    static NegligenceRepository getInstance() {
        return NegligenceRepositoryImpl.getInstance();
    }
}
