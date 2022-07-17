/*
 * Copyright (c) 2021-2022, Mirko Felice & Maxim Derevyanchenko. All rights reserved.
 * Licensed under the MIT license. See LICENSE file in the project root for details.
 */

package io.github.dronesecurity.dronesystem.drone.domain.drone.order.objects;

/**
 * Class representing data regarding the current order to deliver.
 */
public class OrderData {

    private final long orderId;
    private final String courier;
    private final Path path;

    /**
     * Builds the data of the delivered order.
     * @param orderId The identifier of the order currently delivered
     * @param courier The name of the courier currently delivering the order
     */
    public OrderData(final long orderId, final String courier) {
        this.orderId = orderId;
        this.courier = courier;
        this.path = new Path();
    }

    /**
     * Gets the id of the delivered order.
     * @return The id of the delivered order
     */
    public long getOrderId() {
        return this.orderId;
    }

    /**
     * Gets the courier in charge of the drone.
     * @return The name of the courier in charge
     */
    public String getCourier() {
        return this.courier;
    }

    /**
     * Gets the path the drone needs to follow to successfully deliver the order.
     * @return The path to the destination of the delivery
     */
    public Path getPath() {
        return this.path;
    }
}
