package shipping.courier;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

public final class OrderTest {

    private static final String EMPTY_PRODUCT = "";
    private static final String BASIC_PRODUCT = "basic";
    private transient Order order;

    @BeforeEach
    public void setOrder() {
        order = Order.placeToday(BASIC_PRODUCT);
    }

    @Test
    public void testValidation() {
        assertThrows(IllegalArgumentException.class, () -> Order.placeToday(null));
        assertThrows(IllegalArgumentException.class, () -> Order.placeToday(EMPTY_PRODUCT));
    }

    @Test
    public void testOrdersEquality() {
        assertEquals(order, order);
        assertNotEquals(order, Order.placeToday(BASIC_PRODUCT));
    }

    @Test
    public void testBasicLifeCycle() {
        testInitialLifecycle();
        testDelivery();
        order.confirmDelivery();
        assertTrue(order.hasBeenDelivered());
    }

        @Test
    public void testRescheduleLifecycle() {
        testInitialLifecycle();
        order.missDelivery();
        assertTrue(order.isFailed());
        order.rescheduleDelivery(new Date());
        assertTrue(order.isRescheduled());
    }

    private void testInitialLifecycle() {
        assertTrue(order.isPlaced());
        assertFalse(order.isBeingDelivered());
    }

    private void testDelivery() {
        order.deliver();
        assertTrue(order.isBeingDelivered());
    }
}
