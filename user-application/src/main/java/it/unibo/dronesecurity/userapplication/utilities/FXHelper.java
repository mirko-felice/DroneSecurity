package it.unibo.dronesecurity.userapplication.utilities;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.jetbrains.annotations.NotNull;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Optional;

/**
 * Helper providing methods related to JavaFX components.
 */
public final class FXHelper {

    private FXHelper() { }

    /**
     * Create basic window using parameters.
     * @param modality modality to initialize window
     * @param title title to set on the window
     * @param loader loader to use to load the scene from fxml
     * @return an {@link Optional} indicating if an error happened during the loading of resource file or not
     */
    public static Optional<Stage> createWindow(final Modality modality, final String title,
                                               final @NotNull FXMLLoader loader) {
        try {
            final Stage stage = new Stage();
            stage.setScene(new Scene(loader.load()));
            stage.initModality(modality);
            stage.setTitle(title);
            stage.setResizable(false);
            return Optional.of(stage);
        } catch (IOException e) {
            LoggerFactory.getLogger(FXHelper.class).error("Error creating the new window:", e);
            return Optional.empty();
        }
    }
}
