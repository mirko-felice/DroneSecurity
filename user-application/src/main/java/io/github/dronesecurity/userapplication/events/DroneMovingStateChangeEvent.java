/*
 * Copyright (c) 2021-2022, Mirko Felice & Maxim Derevyanchenko. All rights reserved.
 * Licensed under the MIT license. See LICENSE file in the project root for details.
 */

package io.github.dronesecurity.userapplication.events;

/**
 * Event representing Drone's notification about its moving state change.
 */
public class DroneMovingStateChangeEvent implements Event {

    private final String movingState;

    /**
     * Constructor for Moving state change event.
     * @param movingState the new state of the motion of the drone
     */
    public DroneMovingStateChangeEvent(final String movingState) {
        this.movingState = movingState;
    }

    /**
     * Gets the moving state sent by the drone.
     * @return the moving state of the drone
     */
    public String getMovingState() {
        return this.movingState;
    }
}
