package drone;

import com.sun.tools.javac.Main;
import utilities.CustomLogger;

/**
 * Application that activates Drone Service.
 */
public final class Activation {

    private static final int TERMINATION_DELAY = 5000;

    /**
     * Main method for drone activation.
     *
     * @param args the args that are passed to the application
     */
    public static void main(final String[] args) {
        final DroneService droneService = new DroneService();
        droneService.startDrone();

        try {
            Thread.sleep(TERMINATION_DELAY);
            droneService.stopDrone();
        } catch (InterruptedException e) {
            CustomLogger.getLogger(Main.class.getName()).info(e.getMessage());
        }
    }

    private Activation() { }
}
