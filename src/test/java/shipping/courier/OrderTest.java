package shipping.courier;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

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

}
