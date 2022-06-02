/*
 * Copyright (c) 2021-2022, Mirko Felice & Maxim Derevyanchenko. All rights reserved.
 * Licensed under the MIT license. See LICENSE file in the project root for details.
 */

package io.github.dronesecurity.userapplication.shipping.courier;

import io.github.dronesecurity.userapplication.exceptions.EmptyClientException;
import io.github.dronesecurity.userapplication.exceptions.EmptyProductException;
import io.github.dronesecurity.userapplication.shipping.courier.entities.*;
import io.github.dronesecurity.userapplication.utilities.CastHelper;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Calendar;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests {@link Order} entity.
 */
final class OrderTest {

    private static final String EMPTY_STRING = "";
    private static final String SIMPLE_PRODUCT = "example";
    private static final String SIMPLE_CLIENT = "John";
    private static final int HOURS_OF_DAY = 24;
    private static final int MILLIS_CREATION_DELAY = 1000;
    private Order order;

    @BeforeEach
    void setup() {
        this.order = Order.placeToday(SIMPLE_PRODUCT, SIMPLE_CLIENT);
    }

    @Test
    void testValidation() {
        assertThrows(EmptyProductException.class,
                () -> Order.placeToday(null, SIMPLE_CLIENT),
                "Passing null as product should throw EmptyProductException");
        assertThrows(EmptyClientException.class,
                () -> Order.placeToday(SIMPLE_PRODUCT, null),
                "Passing null as client should throw EmptyClientException");
        assertThrows(EmptyProductException.class,
                () -> Order.placeToday(EMPTY_STRING, SIMPLE_CLIENT),
                "Passing empty string as product should throw EmptyProductException");
        assertThrows(EmptyClientException.class,
                () -> Order.placeToday(SIMPLE_PRODUCT, EMPTY_STRING),
                "Passing empty string as client should throw EmptyClientException");
    }

    @Test
    void testOrdersEquality() throws InterruptedException {
        assertEquals(this.order, this.order, "Entity should be equal to itself.");
        final ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();
        executor.schedule(() -> assertNotEquals(this.order, Order.placeToday(SIMPLE_PRODUCT, SIMPLE_CLIENT),
                "Entity must be unique even if build in the same way."),
                MILLIS_CREATION_DELAY, TimeUnit.MILLISECONDS);
        executor.shutdown();
        assertTrue(executor.awaitTermination(2, TimeUnit.SECONDS),
                "Executor does not terminate correctly.");
    }

    @Test
    void testBasicLifeCycle() {
        final DeliveringOrder deliveringOrder = this.testDelivering();

        assertNotNull(deliveringOrder.confirmDelivery(), "Confirming delivery should not returning null.");
    }

    @Test
    void testRescheduleLifecycle() {
        final DeliveringOrder deliveringOrder = this.testDelivering();

        final FailedOrder failedOrder = deliveringOrder.failDelivery();
        assertNotNull(failedOrder, "Failing delivery should not returning null.");
        final Calendar today = Calendar.getInstance();
        today.add(Calendar.HOUR_OF_DAY, HOURS_OF_DAY);
        final RescheduledOrder rescheduledOrder = failedOrder.rescheduleDelivery(today.toInstant());
        assertNotNull(rescheduledOrder, "Rescheduling delivery should not returning null.");
        final DeliveringOrder redeliveringOrder = rescheduledOrder.deliver();
        assertNotNull(redeliveringOrder, "Delivering should not returning null.");
        assertNotNull(deliveringOrder.confirmDelivery(), "Confirming delivery should not returning null.");
    }

    private @NotNull DeliveringOrder testDelivering() {
        final DeliveringOrder deliveringOrder = CastHelper.safeCast(this.order, PlacedOrder.class)
                .orElseThrow().deliver();
        assertNotNull(deliveringOrder, "Delivering should not returning null.");
        return deliveringOrder;
    }
}
