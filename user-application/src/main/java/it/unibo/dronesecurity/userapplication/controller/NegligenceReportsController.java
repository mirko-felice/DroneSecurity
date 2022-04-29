package it.unibo.dronesecurity.userapplication.controller;

import io.vertx.core.impl.logging.LoggerFactory;
import it.unibo.dronesecurity.userapplication.auth.entities.Courier;
import it.unibo.dronesecurity.userapplication.auth.entities.Maintainer;
import it.unibo.dronesecurity.userapplication.negligence.report.NegligenceReport;
import it.unibo.dronesecurity.userapplication.negligence.report.NegligenceRepository;
import it.unibo.dronesecurity.userapplication.utilities.UserHelper;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.TextFieldTableCell;
import org.controlsfx.control.MasterDetailPane;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.Arrays;
import java.util.ResourceBundle;
import java.util.function.Consumer;

/**
 * Controller dedicated to list and control {@link NegligenceReport}.
 */
public class NegligenceReportsController implements Initializable {

    @FXML private MasterDetailPane masterDetailPane;
    private TableView<NegligenceReport> reportsTable;

    /**
     * {@inheritDoc}
     */
    @Override
    public void initialize(final URL location, final ResourceBundle resources) {
        NegligenceRepository.getInstance().retrieveReportsForCourier(UserHelper.getLoggedUser().getUsername())
                .onSuccess(reports -> this.reportsTable.setItems(FXCollections.observableList(reports)));

        this.reportsTable = new TableView<>();
        this.reportsTable.setPlaceholder(new Label("You have no negligence reports."));
        this.reportsTable.getSelectionModel().setCellSelectionEnabled(true);
        this.reportsTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        final TableColumn<NegligenceReport, Courier> negligentColumn =
                this.initializeColumn("Negligent", Courier.class);

        final TableColumn<NegligenceReport, Maintainer> assignerColumn =
                this.initializeColumn("Assigner", Maintainer.class);


        this.reportsTable.getColumns().addAll(Arrays.asList(negligentColumn, assignerColumn));

        this.masterDetailPane.setMasterNode(this.reportsTable);
    }

    private <T> @NotNull TableColumn<NegligenceReport, T> initializeColumn(final String header, final Class<T> type) {
        final TableColumn<NegligenceReport, T> column = new TableColumn<>(header);
        column.setCellFactory(ignored ->
                new NegligenceReportCell<>(value -> this.masterDetailPane.setDetailNode(new Label(value.toString()))));
        column.setCellValueFactory(cell -> {
            try {
                final Method[] methods = NegligenceReport.class.getMethods();
                int i;
                for (i = 0; i < methods.length; i++)
                    if (methods[i].getReturnType().equals(type))
                        break;
                final T object = type.cast(methods[i].invoke(cell.getValue()));
                return new SimpleObjectProperty<>(object);
            } catch (InvocationTargetException | IllegalAccessException | ArrayIndexOutOfBoundsException e) {
                LoggerFactory.getLogger(this.getClass())
                        .warn("Method with return type " + type.getSimpleName() + " does not exist.", e);
                return null;
            }
        });
        column.prefWidthProperty().bind(this.reportsTable.widthProperty().divide(2));
        return column;
    }

    /**
     * Simple class to create cell on a {@link TableView} from {@link NegligenceReport} to {@code <T>}.
     * @param <T> cell value type
     */
    private static final class NegligenceReportCell<T> extends TextFieldTableCell<NegligenceReport, T> {

        private final Consumer<T> consumer;

        private NegligenceReportCell(final Consumer<T> consumer) {
            this.consumer = consumer;
        }

        @Override
        public void updateItem(final T item, final boolean empty) {
            super.updateItem(item, empty);

            setOnMouseClicked(event -> {
                if (!empty)
                   this.consumer.accept(item);
            });
        }
    }
}
