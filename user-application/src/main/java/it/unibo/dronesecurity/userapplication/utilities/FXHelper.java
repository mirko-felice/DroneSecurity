package it.unibo.dronesecurity.userapplication.utilities;

import it.unibo.dronesecurity.lib.Connection;
import it.unibo.dronesecurity.userapplication.auth.entities.LoggedUser;
import it.unibo.dronesecurity.userapplication.auth.repo.AuthenticationRepository;
import it.unibo.dronesecurity.userapplication.controller.DetailController;
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
import org.controlsfx.control.MasterDetailPane;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
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
     * @param detailFile name of the detail fxml file
     * @param masterDetailPane {@link MasterDetailPane} on which setting a detail node
     * @param <T> type parameter
     * @return the {@link TableView}
     */
    public static <T extends NegligenceReport> @Nullable TableView<T> generateTableWithDetails(
            final URL detailFile,
            final @NotNull MasterDetailPane masterDetailPane) {
        try {
            final FXMLLoader loader = new FXMLLoader(detailFile);
            masterDetailPane.setDetailNode(loader.load());
            final DetailController controller = loader.getController();

            final Consumer<LoggedUser> userConsumer = loggedUser ->
                    Platform.runLater(() -> controller.updateDetails(loggedUser));

            final Consumer<String> courierConsumer = value ->
                    AuthenticationRepository.getInstance().retrieveCourier(value).onSuccess(userConsumer::accept);
            final Consumer<String> maintainerConsumer = value ->
                    AuthenticationRepository.getInstance().retrieveMaintainer(value).onSuccess(userConsumer::accept);
            return generateTable(courierConsumer, maintainerConsumer);
        } catch (IOException e) {
            LoggerFactory.getLogger(FXHelper.class).error("Can NOT load the detail fxml file.", e);
            return null;
        }
    }

    private static <T extends NegligenceReport> @NotNull TableView<T> generateTable(
            final Consumer<String> courierClicked,
            final Consumer<String> maintainerClicked) {
        final TableView<T> table = new TableView<>();
        table.setPlaceholder(new Label("No reports found."));
        table.getSelectionModel().setCellSelectionEnabled(true);
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        final TableColumn<T, String> negligentColumn =
                initializeColumn("Negligent", String.class, "getNegligent", courierClicked);

        final TableColumn<T, String> assignedToColumn =
                initializeColumn("Assigned To", String.class, "assignedTo", maintainerClicked);

        table.getColumns().addAll(Arrays.asList(negligentColumn, assignedToColumn));
        return table;
    }

    private static <T extends NegligenceReport, S> @NotNull TableColumn<T, S> initializeColumn(
            final String header,
            final Class<S> type,
            final S value,
            final Consumer<S> mouseClickedListener) {
        final TableColumn<T, S> column = new TableColumn<>(header);
        column.setCellFactory(ignored -> new NegligenceReportCell<>(mouseClickedListener));
        column.setCellValueFactory(cell -> {
            try {
                final Method[] methods = NegligenceReport.class.getMethods();
                final S object = type.cast(Arrays.stream(methods)
                        .filter(m -> m.getName().equals(value))
                        .findFirst()
                        .orElseThrow()
                        .invoke(cell.getValue()));
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
