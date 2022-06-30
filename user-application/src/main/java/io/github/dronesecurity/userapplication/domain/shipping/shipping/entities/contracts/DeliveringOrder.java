/*
 * Copyright (c) 2021-2022, Mirko Felice & Maxim Derevyanchenko. All rights reserved.
 * Licensed under the MIT license. See LICENSE file in the project root for details.
 */

package io.github.dronesecurity.userapplication.domain.shipping.shipping.entities.contracts;

/**
 * Represents an {@link Order} that is currently being delivered.
 */
public interface DeliveringOrder extends Order {

    /**
     * Confirm the delivery.
     */
    void succeedDelivery();

    /**
     * Miss the delivery.
     */
    void failDelivery();
}
