package it.unibo.dronesecurity.userapplication.negligence.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import it.unibo.dronesecurity.lib.Connection;
import it.unibo.dronesecurity.lib.MqttTopicConstants;
import it.unibo.dronesecurity.userapplication.auth.repo.AuthenticationRepository;
import it.unibo.dronesecurity.userapplication.events.DomainEvents;
import it.unibo.dronesecurity.userapplication.events.NewNegligence;
import it.unibo.dronesecurity.userapplication.negligence.entities.NegligenceActionForm;
import it.unibo.dronesecurity.userapplication.negligence.entities.NegligenceReport;
import it.unibo.dronesecurity.userapplication.negligence.repo.NegligenceRepository;
import it.unibo.dronesecurity.userapplication.negligence.utilities.NegligenceConstants;
import org.jetbrains.annotations.NotNull;
import org.slf4j.LoggerFactory;

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
                final ObjectMapper mapper = new ObjectMapper();
                final JsonNode json = mapper.readTree(new String(msg.getPayload(), StandardCharsets.UTF_8));
                AuthenticationRepository.getInstance().retrieveCourier(json.get(NegligenceConstants.NEGLIGENT).asText())
                        .onSuccess(courier -> {
                            try {
                                final NegligenceReport report = mapper.reader()
                                        .forType(NegligenceReport.class)
                                        .withAttribute(NegligenceConstants.ASSIGNEE, courier.getSupervisor())
                                        .readValue(json.toString());
                                domainEvents.raise(new NewNegligence(report));
                            } catch (JsonProcessingException e) {
                                LoggerFactory.getLogger(this.getClass()).error("Can NOT convert negligence report.", e);
                            }
                        });
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
