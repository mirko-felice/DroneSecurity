package it.unibo.dronesecurity.userapplication.utilities;

import it.unibo.dronesecurity.lib.Connection;
import it.unibo.dronesecurity.userapplication.auth.entities.Courier;
import it.unibo.dronesecurity.userapplication.auth.entities.Maintainer;
import it.unibo.dronesecurity.userapplication.negligence.entities.NegligenceReport;
import javafx.application.Platform;
import javafx.beans.property.SimpleObjectProperty;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import org.jetbrains.annotations.NotNull;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Optional;
import java.util.function.Consumer;

/**
 * Helper providing methods related to JavaFX components.
 */
public final class FXHelper {

    private static final EventHandler<WindowEvent> CLOSING_HANDLER = event -> {
        VertxHelper.WEB_CLIENT.close();
        VertxHelper.VERTX.close();
        Connection.getInstance().closeConnection();
        Platform.exit();
        System.exit(0);
    };

    private FXHelper() { }

    /**
     * Create basic window using parameters.
     * @param stage stage to initialize
     * @param modality modality to initialize window
     * @param title title to set on the window
     * @param loader loader to use to load the scene from fxml
     * @return an {@link Optional} indicating if an error happened during the loading of resource file or not
     */
    public static Optional<Stage> initializeWindow(final @NotNull Stage stage,
                                                   final Modality modality,
                                                   final String title,
                                                   final @NotNull FXMLLoader loader) {
        try {
            stage.setScene(new Scene(loader.load()));
            stage.initModality(modality);
            stage.setTitle(title);
            stage.setResizable(false);
            if (modality == Modality.NONE)
                stage.setOnCloseRequest(CLOSING_HANDLER);
            return Optional.of(stage);
        } catch (IOException e) {
            LoggerFactory.getLogger(FXHelper.class).error("Error creating the new window:", e);
            return Optional.empty();
        }
    }

    /**
     * Create basic window using parameters.
     * @param modality modality to initialize window
     * @param title title to set on the window
     * @param loader loader to use to load the scene from fxml
     * @return an {@link Optional} indicating if an error happened during the loading of resource file or not
     */
    public static Optional<Stage> initializeWindow(final Modality modality,
                                                   final String title,
                                                   final @NotNull FXMLLoader loader) {
        return initializeWindow(new Stage(), modality, title, loader);
    }

    /**
     * Generates a {@link TableView} of {@link NegligenceReport} or subclass.
     * @param id javafx:id associated to the table
     * @param courierClicked {@link Consumer} to use when a {@link Courier} is clicked
     * @param maintainerClicked {@link Consumer} to use when a {@link Maintainer} is clicked
     * @param <T> type parameter
     * @return the {@link TableView}
     */
    public static <T extends NegligenceReport> @NotNull TableView<T> generateTable(
            final String id,
            final Consumer<Courier> courierClicked,
            final Consumer<Maintainer> maintainerClicked) {
        final TableView<T> table = new TableView<>();
        table.setId(id);
        table.setPlaceholder(new Label("No reports found."));
        table.getSelectionModel().setCellSelectionEnabled(true);
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        final TableColumn<T, Courier> negligentColumn =
                initializeColumn("Negligent", Courier.class, courierClicked);

        final TableColumn<T, Maintainer> assignedToColumn =
                initializeColumn("Assigned To", Maintainer.class, maintainerClicked);

        table.getColumns().addAll(Arrays.asList(negligentColumn, assignedToColumn));
        return table;
    }

    private static <T extends NegligenceReport, S> @NotNull TableColumn<T, S> initializeColumn(
            final String header,
            final Class<S> type,
            final Consumer<S> mouseClickedListener) {
        final TableColumn<T, S> column = new TableColumn<>(header);
        column.setCellFactory(ignored -> new NegligenceReportCell<>(mouseClickedListener));
        column.setCellValueFactory(cell -> {
            try {
                final Method[] methods = NegligenceReport.class.getMethods();
                int i;
                for (i = 0; i < methods.length; i++)
                    if (methods[i].getReturnType().equals(type))
                        break;
                final S object = type.cast(methods[i].invoke(cell.getValue()));
                return new SimpleObjectProperty<>(object);
            } catch (InvocationTargetException | IllegalAccessException | ArrayIndexOutOfBoundsException e) {
                LoggerFactory.getLogger(FXHelper.class)
                        .warn("Method with return type " + type.getSimpleName() + " does not exist.", e);
                return null;
            }
        });
        return column;
    }

    /**
     * Class to create default {@link TextFieldTableCell} adding a {@link Consumer} to trigger code on mouse clicked
     * on a non-empty cell.
     * @param <T> table type value extending {@link NegligenceReport}
     * @param <S> cell type value
     */
    private static final class NegligenceReportCell<T extends NegligenceReport, S> extends TextFieldTableCell<T, S> {

        private final Consumer<S> consumer;

        private NegligenceReportCell(final Consumer<S> consumer) {
            this.consumer = consumer;
        }

        @Override
        public void updateItem(final S item, final boolean empty) {
            super.updateItem(item, empty);

            setOnMouseClicked(event -> {
                if (!empty)
                    this.consumer.accept(item);
            });
        }
    }
}
