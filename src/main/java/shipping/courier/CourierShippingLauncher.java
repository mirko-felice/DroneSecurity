package shipping.courier;

/**
 * {@link CourierShippingService} launcher.
 */
public final class CourierShippingLauncher {

    private CourierShippingLauncher() { }

    /**
     * Launch the corresponding Service.
     * @param args possibly arguments of main
     */
    public static void main(final String[] args) {
        new CourierShippingService().startListening();
    }
}
