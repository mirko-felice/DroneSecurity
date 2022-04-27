package it.unibo.dronesecurity.userapplication.negligence.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.vertx.core.impl.logging.LoggerFactory;
import it.unibo.dronesecurity.lib.Connection;
import it.unibo.dronesecurity.lib.MqttTopicConstants;
import it.unibo.dronesecurity.userapplication.events.DomainEvents;
import it.unibo.dronesecurity.userapplication.events.NewNegligence;
import it.unibo.dronesecurity.userapplication.negligence.report.NegligenceActionForm;
import it.unibo.dronesecurity.userapplication.negligence.report.NegligenceReport;
import it.unibo.dronesecurity.userapplication.negligence.report.NegligenceRepository;
import org.jetbrains.annotations.NotNull;

import java.nio.charset.StandardCharsets;

/**
 * Service providing methods both for the needs of the courier and for the maintainer.
 */
public class NegligenceReportService implements CourierNegligenceReportService, MaintainerNegligenceReportService {

    private static NegligenceReportService singleton;

    /**
     * Get the Singleton instance.
     * @return the singleton
     */
    public static NegligenceReportService getInstance() {
        synchronized (NegligenceReportService.class) {
            if (singleton == null)
                singleton = new NegligenceReportService();
            return singleton;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void subscribeToNegligenceReports(final @NotNull DomainEvents<NewNegligence> domainEvents) {
        domainEvents.register(this::onNewNegligence);
        Connection.getInstance().subscribe(MqttTopicConstants.NEGLIGENCE_REPORTS_TOPIC, msg -> {
            try {
                final NegligenceReport report = new ObjectMapper().readValue(
                        new String(msg.getPayload(), StandardCharsets.UTF_8),
                        NegligenceReport.class);
                domainEvents.raise(new NewNegligence(report));
            } catch (JsonProcessingException e) {
                LoggerFactory.getLogger(this.getClass()).error("Can NOT convert negligence report.", e);
            }
        });
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void takeAction(final NegligenceActionForm form) {
        NegligenceRepository.getInstance().takeAction(form);
    }

    private void onNewNegligence(final @NotNull NewNegligence newNegligence) {
        NegligenceRepository.getInstance().createReport(newNegligence.getReport());
    }
}
