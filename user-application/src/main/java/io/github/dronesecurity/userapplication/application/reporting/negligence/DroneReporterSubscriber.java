/*
 * Copyright (c) 2021-2022, Mirko Felice & Maxim Derevyanchenko. All rights reserved.
 * Licensed under the MIT license. See LICENSE file in the project root for details.
 */

package io.github.dronesecurity.userapplication.application.reporting.negligence;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.dronesecurity.lib.connection.Connection;
import io.github.dronesecurity.lib.connection.MqttTopicConstants;
import io.github.dronesecurity.userapplication.application.user.ohs.pl.Courier;
import io.github.dronesecurity.userapplication.application.user.ohs.pl.Maintainer;
import io.github.dronesecurity.userapplication.domain.reporting.negligence.objects.Assignee;
import io.github.dronesecurity.userapplication.domain.reporting.negligence.objects.DroneData;
import io.github.dronesecurity.userapplication.domain.reporting.negligence.objects.Negligent;
import io.github.dronesecurity.userapplication.domain.reporting.negligence.services.DroneReporter;
import io.github.dronesecurity.userapplication.infrastructure.reporting.negligence.NegligenceConstants;
import io.github.dronesecurity.userapplication.infrastructure.reporting.negligence.repo.MongoNegligenceRepository;
import io.github.dronesecurity.userapplication.utilities.user.UserAPIHelper;
import io.vertx.ext.web.codec.BodyCodec;
import org.slf4j.LoggerFactory;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;

/**
 * Helper class to start and stop receiving negligence reports.
 */
public final class DroneReporterSubscriber {

    private static final DroneReporter DRONE_REPORTER = new DroneReporterImpl(new MongoNegligenceRepository());

    private DroneReporterSubscriber() { }

    /**
     * Starts receiving negligence reports for the current possible assignee.
     */
    public static void startReceivingAssigneeReports() {
        UserAPIHelper.get(UserAPIHelper.Operation.RETRIEVE_LOGGED_MAINTAINER_IF_PRESENT,
                        BodyCodec.json(Maintainer.class))
                .compose(maintainer ->
                        UserAPIHelper.get(UserAPIHelper.Operation.RETRIEVE_COURIERS_SUPERVISED_BY_LOGGED_MAINTAINER,
                                        BodyCodec.json(String[].class))
                                .map(res -> {
                                    final List<String> couriers = Arrays.asList(res.body());
                                    couriers.add(0, maintainer.body().getUsername());
                                    return couriers;
                                }))
                .onSuccess(array -> {
                    final String maintainer = array.get(0);
                    array.stream()
                            .filter(s -> array.indexOf(s) != 0)
                            .forEach(courier -> subscribeToNegligence(courier, maintainer));
                });
    }

    /**
     * Starts receiving negligence reports for the current possible negligent.
     */
    public static void startReceivingNegligentReports() {
        UserAPIHelper.get(UserAPIHelper.Operation.RETRIEVE_LOGGED_COURIER_IF_PRESENT,
                        BodyCodec.json(Courier.class))
                .onSuccess(res -> {
                    final Courier courier = res.body();
                    subscribeToNegligence(courier.getUsername(), courier.getSupervisorUsername());
                });
    }

    /**
     * Stops receiving negligence reports for the current possible assignee.
     */
    public static void stopReceivingAssigneeReports() {
        UserAPIHelper.get(UserAPIHelper.Operation.RETRIEVE_COURIERS_SUPERVISED_BY_LOGGED_MAINTAINER,
                        BodyCodec.json(String[].class))
                .onSuccess(res -> {
                    final List<String> couriers = Arrays.asList(res.body());
                    couriers.forEach(DroneReporterSubscriber::unsubscribeToNegligence);
                });
    }

    /**
     * Stops receiving negligence reports for the current possible negligent.
     */
    public static void stopReceivingNegligentReports() {
        UserAPIHelper.get(UserAPIHelper.Operation.RETRIEVE_LOGGED_COURIER_IF_PRESENT,
                        BodyCodec.json(Courier.class))
                .onSuccess(res -> unsubscribeToNegligence(res.body().getUsername()));
    }

    private static void subscribeToNegligence(final String courier, final String maintainer) {
        Connection.getInstance().subscribe(MqttTopicConstants.NEGLIGENCE_REPORTS_TOPIC + courier, msg -> {
            try {
                final ObjectMapper mapper = new ObjectMapper();
                final JsonNode json = mapper.readTree(new String(msg.getPayload(), StandardCharsets.UTF_8));
                final Negligent negligent = Negligent.parse(json.get(NegligenceConstants.NEGLIGENT).asText());
                final Assignee assignee = Assignee.parse(maintainer);
                final DroneData droneData =
                        mapper.readValue(json.get(NegligenceConstants.DATA).toString(), DroneData.class);

                DRONE_REPORTER.reportsNegligence(negligent, assignee, droneData);
            } catch (JsonProcessingException e) {
                LoggerFactory.getLogger(DroneReporterSubscriber.class).error("Can NOT convert negligence report.", e);
            }
        });
    }

    private static void unsubscribeToNegligence(final String username) {
        Connection.getInstance().unsubscribe(MqttTopicConstants.NEGLIGENCE_REPORTS_TOPIC + username);
    }
}
