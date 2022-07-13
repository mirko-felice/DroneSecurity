/*
 * Copyright (c) 2021-2022, Mirko Felice & Maxim Derevyanchenko. All rights reserved.
 * Licensed under the MIT license. See LICENSE file in the project root for details.
 */

package io.github.dronesecurity.userapplication.domain.shipping;

import io.github.dronesecurity.userapplication.application.shipping.DeliveryServiceImpl;
import io.github.dronesecurity.userapplication.application.shipping.OrderManagerImpl;
import io.github.dronesecurity.userapplication.domain.shipping.client.Client;
import io.github.dronesecurity.userapplication.domain.shipping.shipping.entities.contracts.*;
import io.github.dronesecurity.userapplication.domain.shipping.shipping.exceptions.EstimatedArrivalCannotBeBeforeTodayException;
import io.github.dronesecurity.userapplication.domain.shipping.shipping.objects.OrderDate;
import io.github.dronesecurity.userapplication.domain.shipping.shipping.objects.OrderIdentifier;
import io.github.dronesecurity.userapplication.domain.shipping.shipping.objects.Product;
import io.github.dronesecurity.userapplication.domain.shipping.shipping.repo.OrderRepository;
import io.github.dronesecurity.userapplication.domain.shipping.shipping.service.DeliveryService;
import io.github.dronesecurity.userapplication.domain.shipping.shipping.service.OrderManager;
import io.github.dronesecurity.userapplication.infrastructure.shipping.repo.InMemoryOrderRepository;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test business logic related to {@link Order}.
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
final class BusinessTest {

    private static final int FIRST = 1;
    private static final int SECOND = 2;
    private static final int THIRD = 3;
    private static final int FOURTH = 4;
    private static final int FIFTH = 5;
    private static final int SIXTH = 6;
    private static final int SEVENTH = 7;
    private static final OrderRepository ORDER_REPOSITORY = new InMemoryOrderRepository();
    private static final OrderManager ORDER_MANAGER = new OrderManagerImpl(ORDER_REPOSITORY);
    private static final DeliveryService DELIVERY_SERVICE = new DeliveryServiceImpl(ORDER_REPOSITORY);
    private static final OrderIdentifier ORDER_IDENTIFIER = OrderIdentifier.first();
    private static final Client CLIENT = Client.mock();
    private static final Product PRODUCT = Product.fromName("test");
    private static final String DRONE_IDENTIFIER = "drone identifier";
    private static final String COURIER = "courier";

    @Test
    @org.junit.jupiter.api.Order(FIRST)
    void testInitialValidation() {
        assertTrue(ORDER_MANAGER.listOrders().isEmpty(),
                "At first orders should be empty.");
        assertNull(ORDER_MANAGER.retrieveOrderById(ORDER_IDENTIFIER),
                "At first retrieving an order should return null.");
    }

    @Test
    @org.junit.jupiter.api.Order(SECOND)
     void testPlacing() {
        assertThrowsExactly(EstimatedArrivalCannotBeBeforeTodayException.class,
                () -> ORDER_MANAGER.placeOrder(CLIENT, PRODUCT, OrderDate.TODAY),
                "Placing an order using today as estimated arrival should not be possible.");

        ORDER_MANAGER.placeOrder(CLIENT, PRODUCT, OrderDate.TOMORROW);
        assertEquals(1, ORDER_MANAGER.listOrders().size(),
                "After placing the first order, orders list size should be 1.");
        final Order placed = ORDER_MANAGER.retrieveOrderById(ORDER_IDENTIFIER);
        assertNotNull(placed,
                "After placing the first order it should be retrievable by using the first order identifier.");
        assertEquals(OrderState.PLACED, placed.getCurrentState(),
                "After placing it, the order should have PLACED state.");
        assertInstanceOf(PlacedOrder.class, placed,
                "Order should be instance of Placed Order.");
    }

    @Test
    @org.junit.jupiter.api.Order(THIRD)
    void testDelivering() {
        DELIVERY_SERVICE.performDelivery((PlacedOrder) ORDER_MANAGER.retrieveOrderById(ORDER_IDENTIFIER),
                DRONE_IDENTIFIER, COURIER);
        final Order delivering = ORDER_MANAGER.retrieveOrderById(ORDER_IDENTIFIER);
        assertNotNull(delivering,
                "Even after performing delivery, same order should be retrievable.");
        assertEquals(OrderState.DELIVERING, delivering.getCurrentState(),
                "After performing delivery, the order should have DELIVERING state.");
        assertInstanceOf(DeliveringOrder.class, delivering,
                "Order should be instance of Delivering Order.");
    }

    @Test
    @org.junit.jupiter.api.Order(FOURTH)
    void testFailed() {
        DELIVERY_SERVICE.failDelivery((DeliveringOrder) ORDER_MANAGER.retrieveOrderById(ORDER_IDENTIFIER));
        final Order failed = ORDER_MANAGER.retrieveOrderById(ORDER_IDENTIFIER);
        assertNotNull(failed,
                "Even after failing delivery, same order should be retrievable.");
        assertEquals(OrderState.FAILED, failed.getCurrentState(),
                "After failing delivery, the order should have FAILED state.");
        assertInstanceOf(FailedOrder.class, failed,
                "Order should be instance of Failed Order.");
    }

    @Test
    @org.junit.jupiter.api.Order(FIFTH)
    void testRescheduled() {
        DELIVERY_SERVICE.rescheduleDelivery((FailedOrder) ORDER_MANAGER.retrieveOrderById(ORDER_IDENTIFIER),
                OrderDate.TOMORROW);
        final Order rescheduled = ORDER_MANAGER.retrieveOrderById(ORDER_IDENTIFIER);
        assertNotNull(rescheduled,
                "Even after rescheduling delivery, same order should be retrievable.");
        assertEquals(OrderState.RESCHEDULED, rescheduled.getCurrentState(),
                "After rescheduling delivery, the order should have RESCHEDULED state.");
        assertInstanceOf(RescheduledOrder.class, rescheduled,
                "Order should be instance of Rescheduled Order.");
    }

    @Test
    @org.junit.jupiter.api.Order(SIXTH)
    void testSucceeded() {
        DELIVERY_SERVICE.performDelivery((RescheduledOrder) ORDER_MANAGER.retrieveOrderById(ORDER_IDENTIFIER),
                DRONE_IDENTIFIER, COURIER);
        DELIVERY_SERVICE.succeedDelivery((DeliveringOrder) ORDER_MANAGER.retrieveOrderById(ORDER_IDENTIFIER));
        final Order succeeded = ORDER_MANAGER.retrieveOrderById(ORDER_IDENTIFIER);
        assertNotNull(succeeded,
                "Even after succeeding delivery, same order should be retrievable.");
        assertEquals(OrderState.SUCCEEDED, succeeded.getCurrentState(),
                "After succeeding delivery, the order should have SUCCEEDED state.");
        assertInstanceOf(SucceededOrder.class, succeeded,
                "Order should be instance of Succeeded Order.");
    }

    @Test
    @org.junit.jupiter.api.Order(SEVENTH)
    void testMultiple() {
        ORDER_MANAGER.placeOrder(CLIENT, PRODUCT, OrderDate.TOMORROW);
        assertEquals(2, ORDER_MANAGER.listOrders().size(),
                "After placing the second order, orders list size should be 2.");

        final int expectedSize = 3;
        ORDER_MANAGER.placeOrder(CLIENT, PRODUCT, OrderDate.TOMORROW);
        assertEquals(expectedSize, ORDER_MANAGER.listOrders().size(),
                "After placing the third order, orders list size should be 3.");

        DELIVERY_SERVICE.performDelivery((PlacedOrder) ORDER_MANAGER.retrieveOrderById(OrderIdentifier.fromLong(2)),
                DRONE_IDENTIFIER, COURIER);
    }
}
