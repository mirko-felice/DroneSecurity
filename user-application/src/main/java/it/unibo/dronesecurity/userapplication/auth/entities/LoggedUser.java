package it.unibo.dronesecurity.userapplication.auth.entities;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import it.unibo.dronesecurity.userapplication.auth.serializers.LoggedUserDeserializer;

/**
 * Entity representing a {@link User} already logged in.
 */
@JsonDeserialize(using = LoggedUserDeserializer.class)
public interface LoggedUser extends User {

    /**
     * Get the user's role.
     * @return his role
     */
    Role getRole();
}
