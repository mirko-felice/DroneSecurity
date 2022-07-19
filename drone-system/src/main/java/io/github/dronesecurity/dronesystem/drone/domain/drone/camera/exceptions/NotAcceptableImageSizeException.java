/*
 * Copyright (c) 2021-2022, Mirko Felice & Maxim Derevyanchenko. All rights reserved.
 * Licensed under the MIT license. See LICENSE file in the project root for details.
 */

package io.github.dronesecurity.dronesystem.drone.domain.drone.camera.exceptions;

/**
 * Exception to be used when an invalid image size is passed as argument.
 */
public class NotAcceptableImageSizeException extends IllegalArgumentException {

    private static final long serialVersionUID = 1L;

    /**
     * Builds the exception.
     */
    public NotAcceptableImageSizeException() {
        super("Image size can NOT be a negative value.");
    }
}
