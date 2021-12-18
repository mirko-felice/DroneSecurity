package shipping.courier;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import shipping.courier.entities.*;

import java.util.Calendar;

import static org.junit.jupiter.api.Assertions.*;

public final class OrderTest {

    private static final String EMPTY_PRODUCT = "";
    private static final String BASIC_PRODUCT = "basic";
    private static final int HOURS_OF_DAY = 24;
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
        DeliveringOrder deliveringOrder = testDelivering();

        assertNotNull(deliveringOrder.confirmDelivery());
    }

        @Test
    public void testRescheduleLifecycle() {
        DeliveringOrder deliveringOrder = testDelivering();

        FailedOrder failedOrder = deliveringOrder.failDelivery();
        assertNotNull(failedOrder);
        Calendar today = Calendar.getInstance();
        today.add(Calendar.HOUR_OF_DAY, HOURS_OF_DAY);
        RescheduledOrder rescheduledOrder = failedOrder.rescheduleDelivery(today.getTime());
        assertNotNull(rescheduledOrder);
        DeliveringOrder redeliveringOrder = rescheduledOrder.deliver();
        assertNotNull(redeliveringOrder);
        assertNotNull(deliveringOrder.confirmDelivery());
    }

    private DeliveringOrder testDelivering() {
        DeliveringOrder deliveringOrder = Utilities.safeCast(order, PlacedOrder.class).orElseThrow().deliver();
        assertNotNull(deliveringOrder);
        return deliveringOrder;
    }
}
