/*
 * Copyright (c) 2021-2022, Mirko Felice & Maxim Derevyanchenko. All rights reserved.
 * Licensed under the MIT license. See LICENSE file in the project root for details.
 */

package io.github.dronesecurity.userapplication.domain.shipping;

import io.github.dronesecurity.lib.utilities.DateHelper;
import io.github.dronesecurity.userapplication.domain.shipping.client.Client;
import io.github.dronesecurity.userapplication.domain.shipping.shipping.entities.contracts.Order;
import io.github.dronesecurity.userapplication.domain.shipping.shipping.entities.impl.PlacedOrderImpl;
import io.github.dronesecurity.userapplication.domain.shipping.shipping.entities.impl.RescheduledOrderImpl;
import io.github.dronesecurity.userapplication.domain.shipping.shipping.exceptions.ArrivalDateBeforePlacingDateException;
import io.github.dronesecurity.userapplication.domain.shipping.shipping.exceptions.EmptyProductNameException;
import io.github.dronesecurity.userapplication.domain.shipping.shipping.exceptions.OrderIdentifierCannotHaveNegativeValueException;
import io.github.dronesecurity.userapplication.domain.shipping.shipping.exceptions.RescheduledDateBeforeFirstArrivalDateException;
import io.github.dronesecurity.userapplication.domain.shipping.shipping.objects.OrderDate;
import io.github.dronesecurity.userapplication.domain.shipping.shipping.objects.OrderIdentifier;
import io.github.dronesecurity.userapplication.domain.shipping.shipping.objects.Product;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests {@link Order} entity.
 */
final class OrderAggregateTest {

    private static final long NEGATIVE_ID = -10L;
    private static final long SIMPLE_ID = 3L;
    private static final OrderIdentifier ORDER_IDENTIFIER = OrderIdentifier.first();
    private static final Product PRODUCT = Product.fromName("testProduct");
    private static final Client CLIENT = Client.mock();
    private static final OrderDate PLACING_DATE = OrderDate.TODAY;
    private static final OrderDate ESTIMATED_ARRIVAL = OrderDate.TOMORROW;

    @Test
    void testOrderIdentifier() {
        assertThrowsExactly(OrderIdentifierCannotHaveNegativeValueException.class,
                () -> OrderIdentifier.fromLong(NEGATIVE_ID),
                "Order Identifier should not have negative value.");

        final OrderIdentifier first = OrderIdentifier.first();
        assertEquals(1L, first.asLong(), "First Order Identifier should be 1.");
        assertTrue(first.isSameValueAs(OrderIdentifier.first()),
                "Order Identifiers should be equals if both first.");

        final OrderIdentifier simpleIdentifier = OrderIdentifier.fromLong(SIMPLE_ID);
        assertTrue(simpleIdentifier.isSameValueAs(simpleIdentifier),
                "Order Identifiers should be equals if both built using same value.");

        assertFalse(first.isSameValueAs(simpleIdentifier),
                "Order Identifiers should be different if built using different values.");
    }

    @Test
    void testProduct() {
        final String productName = "example";
        assertThrowsExactly(EmptyProductNameException.class,
                () -> Product.fromName(null),
                "Product name should not be null.");
        assertThrowsExactly(EmptyProductNameException.class,
                () -> Product.fromName(""),
                "Product name should not be empty.");

        final Product simpleProduct = Product.fromName(productName);
        assertEquals(productName, simpleProduct.name(),
                "Product name should be the same used to build the Value Object.");
        assertTrue(simpleProduct.isSameValueAs(simpleProduct),
                "Products should be equals if their name is the same.");

        final Product anotherProduct = Product.fromName("another");
        assertFalse(simpleProduct.isSameValueAs(anotherProduct),
                "Products should be different if their names are different.");
    }

    @Test
    void testOrderDate() {
        final Instant today = Instant.from(Instant.now().atZone(ZoneId.systemDefault()).truncatedTo(ChronoUnit.DAYS));
        assertEquals(DateHelper.toString(today), PLACING_DATE.asString(),
                "Placing date as String should be the same as today string.");
        assertTrue(OrderDate.TODAY.isSameValueAs(PLACING_DATE),
                "Placing date with same date should be considered the same value.");
        assertDoesNotThrow(() -> OrderDate.parseString("January 01 2023 00:00:00"),
                "Parsing date string formatted in this way (MMMM dd yyyy HH:mm:ss) should not throw exceptions.");
    }

    @Test
    void testOrderValidation() {
        final OrderDate yesterday = OrderDate.parseInstant(Instant.now().minus(2, ChronoUnit.DAYS));
        assertThrowsExactly(ArrivalDateBeforePlacingDateException.class,
                () -> new PlacedOrderImpl(ORDER_IDENTIFIER, PRODUCT, CLIENT, PLACING_DATE, yesterday),
                "Estimated arrival date should never be before placing date.");

        assertThrowsExactly(RescheduledDateBeforeFirstArrivalDateException.class,
                () -> new RescheduledOrderImpl(
                        ORDER_IDENTIFIER, PRODUCT, CLIENT, PLACING_DATE, ESTIMATED_ARRIVAL, yesterday),
                "Rescheduled date should never be before first estimated arrival.");
    }

    @Test
    void testOrderIdentity() {
        final Order order = new PlacedOrderImpl(ORDER_IDENTIFIER, PRODUCT, CLIENT, PLACING_DATE, ESTIMATED_ARRIVAL);
        assertTrue(order.hasSameIdentityAs(order), "Entity should have same identity as itself.");
        final Order anotherOrder =
                new PlacedOrderImpl(OrderIdentifier.fromLong(2L), PRODUCT, CLIENT, PLACING_DATE, ESTIMATED_ARRIVAL);
        assertFalse(order.hasSameIdentityAs(anotherOrder),
                "Entities with same attributes but different identifiers should not have same identity.");
    }

}
