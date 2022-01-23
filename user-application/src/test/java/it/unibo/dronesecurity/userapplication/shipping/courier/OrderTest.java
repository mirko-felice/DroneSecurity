package it.unibo.dronesecurity.userapplication.shipping.courier;

import it.unibo.dronesecurity.userapplication.shipping.courier.entities.*;
import it.unibo.dronesecurity.userapplication.utilities.CastHelper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Calendar;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests {@link Order} entity.
 */
final class OrderTest {

    private static final String EMPTY_PRODUCT = "";
    private static final String BASIC_PRODUCT = "basic";
    private static final int HOURS_OF_DAY = 24;
    private static final int MILLIS_CREATION_DELAY = 1000;
    private transient Order order;

    @BeforeEach
    void setUp() {
        order = Order.placeToday(BASIC_PRODUCT);
    }

    @Test
    void testValidation() {
        assertThrows(IllegalArgumentException.class,
                () -> Order.placeToday(null),
                "Passing null as product should throw IllegalArgumentException");
        assertThrows(IllegalArgumentException.class,
                () -> Order.placeToday(EMPTY_PRODUCT),
                "Passing empty string as product should throw IllegalArgumentException");
    }

    @Test
    void testOrdersEquality() throws InterruptedException {
        assertEquals(order, order, "Entity should be equal to itself.");
        Thread.sleep(MILLIS_CREATION_DELAY);
        assertNotEquals(order, Order.placeToday(BASIC_PRODUCT), "Entity must be unique even if build in the same way.");
    }

    @Test
    void testBasicLifeCycle() {
        final DeliveringOrder deliveringOrder = testDelivering();

        assertNotNull(deliveringOrder.confirmDelivery(), "Confirming delivery should not returning null.");
    }

    @Test
    void testRescheduleLifecycle() {
        final DeliveringOrder deliveringOrder = testDelivering();

        final FailedOrder failedOrder = deliveringOrder.failDelivery();
        assertNotNull(failedOrder, "Failing delivery should not returning null.");
        final Calendar today = Calendar.getInstance();
        today.add(Calendar.HOUR_OF_DAY, HOURS_OF_DAY);
        final RescheduledOrder rescheduledOrder = failedOrder.rescheduleDelivery(today.getTime());
        assertNotNull(rescheduledOrder, "Rescheduling delivery should not returning null.");
        final DeliveringOrder redeliveringOrder = rescheduledOrder.deliver();
        assertNotNull(redeliveringOrder, "Delivering should not returning null.");
        assertNotNull(deliveringOrder.confirmDelivery(), "Confirming delivery should not returning null.");
    }

    private DeliveringOrder testDelivering() {
        final DeliveringOrder deliveringOrder = CastHelper.safeCast(order, PlacedOrder.class).orElseThrow().deliver();
        assertNotNull(deliveringOrder, "Delivering should not returning null.");
        return deliveringOrder;
    }
}
