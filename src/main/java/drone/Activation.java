package drone;

public final class Activation {

    /**
     * Main method for drone activation.
     *
     * @param args the args that are passed to the application
     */
    public static void main(final String[] args) {
        DroneService droneService = new DroneService();
        droneService.startDrone();
    }

    private Activation() { }
}
