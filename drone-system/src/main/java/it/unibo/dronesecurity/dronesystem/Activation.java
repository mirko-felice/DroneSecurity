package it.unibo.dronesecurity.dronesystem;

import it.unibo.dronesecurity.dronesystem.drone.DroneService;
import it.unibo.dronesecurity.lib.CustomLogger;

/**
 * Application that activates Drone Service.
 */
public final class Activation {

    private static final int TERMINATION_DELAY = 25_000;

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
            CustomLogger.getLogger(Activation.class.getName()).info(e.getMessage());
        }
    }

    private Activation() { }
}
