package it.unibo.dronesecurity.userapplication.controller.auth;

import it.unibo.dronesecurity.userapplication.controller.auth.entities.BaseUser;

/**
 * Enum representing roles of the {@link BaseUser}.
 */
public enum Role {
    /**
     * Role representing the Courier user.
     */
    COURIER,
    /**
     * Role representing the Maintainer user.
     */
    MAINTAINER
}
