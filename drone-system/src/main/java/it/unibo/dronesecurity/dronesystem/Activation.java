package it.unibo.dronesecurity.dronesystem;

import it.unibo.dronesecurity.dronesystem.drone.DroneService;

/**
 * Application that activates Drone Service.
 */
public final class Activation {

    /**
     * Main method for drone activation.
     *
     * @param args the args that are passed to the application
     */
    public static void main(final String[] args) {
        final DroneService droneService = new DroneService();
        droneService.activateDrone();
    }

    private Activation() { }
}
