package it.unibo.dronesecurity.userapplication.negligence;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.vertx.core.impl.logging.LoggerFactory;
import it.unibo.dronesecurity.lib.Connection;
import it.unibo.dronesecurity.lib.MqttTopicConstants;
import it.unibo.dronesecurity.userapplication.events.DomainEvents;
import it.unibo.dronesecurity.userapplication.events.NewNegligence;
import it.unibo.dronesecurity.userapplication.negligence.report.NegligenceReport;
import it.unibo.dronesecurity.userapplication.negligence.report.NegligenceRepository;
import org.jetbrains.annotations.NotNull;

import java.nio.charset.StandardCharsets;

/**
 * Service providing method to subscribe to {@link NewNegligence} events.
 */
public class MaintainerNegligenceReportService {

    private final DomainEvents<NewNegligence> negligenceDomainEvents;

    /**
     * Build the Service.
     * @param negligenceDomainEvents {@link DomainEvents} used to register base behaviour known by this service
     */
    public MaintainerNegligenceReportService(final @NotNull DomainEvents<NewNegligence> negligenceDomainEvents) {
        this.negligenceDomainEvents = negligenceDomainEvents;
        this.negligenceDomainEvents.register(this::onNewNegligence);
    }

    /**
     * Subscribe to {@link NewNegligence} events.
     */
    public void subscribeToNegligenceReports() {
        Connection.getInstance().subscribe(MqttTopicConstants.NEGLIGENCE_REPORTS_TOPIC, msg -> {
            try {
                final NegligenceReport report = new ObjectMapper().readValue(
                        new String(msg.getPayload(), StandardCharsets.UTF_8),
                        NegligenceReport.class);
                this.negligenceDomainEvents.raise(new NewNegligence(report));
            } catch (JsonProcessingException e) {
                LoggerFactory.getLogger(this.getClass()).error("Can NOT convert negligence report.", e);
            }
        });
    }

    private void onNewNegligence(final @NotNull NewNegligence newNegligence) {
        NegligenceRepository.getInstance().createReport(newNegligence.getReport());
    }
}
