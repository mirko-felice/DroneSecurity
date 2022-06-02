/*
 * Copyright (c) 2021-2022, Mirko Felice & Maxim Derevyanchenko. All rights reserved.
 * Licensed under the MIT license. See LICENSE file in the project root for details.
 */

package io.github.dronesecurity.userapplication.shipping.courier.utilities;

/**
 *  Order related strings.
 */
public final class OrderConstants {

    /**
     * Key for the order identifier.
     */
    public static final String ID = "id";

    /**
     * Key for the events.
     */
    public static final String EVENTS = "events";

    /**
     * Key for the product.
     */
    public static final String PRODUCT = "product";

    /**
     * Key for the client name.
     */
    public static final String CLIENT = "client";

    /**
     * Key for the placing date.
     */
    public static final String PLACING_DATE = "placingDate";

    /**
     * Key for the estimated arrival.
     */
    public static final String ESTIMATED_ARRIVAL = "estimatedArrival";

    /**
     * Key for the new estimated arrival.
     */
    public static final String NEW_ESTIMATED_ARRIVAL = "newEstimatedArrival";

    /**
     * {@link io.github.dronesecurity.userapplication.shipping.courier.entities.PlacedOrder} state.
     */
    public static final String PLACED_ORDER_STATE = "Order is placed.";

    /**
     * {@link io.github.dronesecurity.userapplication.shipping.courier.entities.FailedOrder} state.
     */
    public static final String FAILED_ORDER_STATE = "Order fails delivery.";

    /**
     * {@link io.github.dronesecurity.userapplication.shipping.courier.entities.DeliveredOrder} state.
     */
    public static final String DELIVERED_ORDER_STATE = "Order is delivered.";

    /**
     * {@link io.github.dronesecurity.userapplication.shipping.courier.entities.DeliveringOrder} state.
     */
    public static final String DELIVERING_ORDER_STATE = "Order is delivering.";

    /**
     * {@link io.github.dronesecurity.userapplication.shipping.courier.entities.RescheduledOrder} state.
     */
    public static final String RESCHEDULED_ORDER_STATE = "Order is rescheduled.";

    private OrderConstants() { }
}
