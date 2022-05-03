package it.unibo.dronesecurity.userapplication.controller;

import io.vertx.core.Future;
import it.unibo.dronesecurity.userapplication.auth.entities.User;
import it.unibo.dronesecurity.userapplication.negligence.entities.ClosedNegligenceReport;
import it.unibo.dronesecurity.userapplication.negligence.entities.NegligenceReport;
import it.unibo.dronesecurity.userapplication.negligence.entities.OpenNegligenceReport;
import it.unibo.dronesecurity.userapplication.negligence.repo.NegligenceRepository;
import it.unibo.dronesecurity.userapplication.utilities.FXHelper;
import it.unibo.dronesecurity.userapplication.utilities.UserHelper;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TableView;
import org.controlsfx.control.MasterDetailPane;
import org.jetbrains.annotations.NotNull;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.function.Consumer;

/**
 * Controller dedicated to list and control {@link NegligenceReport}.
 */
public class NegligenceReportsController implements Initializable {

    @FXML private Tab closedReportsTab;
    @FXML private MasterDetailPane openReportsPane;
    @FXML private MasterDetailPane closedReportsPane;
    private TableView<OpenNegligenceReport> openReportsTable;
    private TableView<ClosedNegligenceReport> closedReportsTable;

    /**
     * {@inheritDoc}
     */
    @Override
    public void initialize(final URL location, final ResourceBundle resources) {
        final User user = UserHelper.getLoggedUser();
        final NegligenceRepository repository = NegligenceRepository.getInstance();
        final String username = user.getUsername();
        final Future<List<OpenNegligenceReport>> openReportsFuture;
        final Future<List<ClosedNegligenceReport>> closedReportsFuture;
        switch (user.getRole()) {
            case COURIER:
                openReportsFuture = repository.retrieveOpenReportsForCourier(username);
                closedReportsFuture = repository.retrieveClosedReportsForCourier(username);
                break;
            case MAINTAINER:
                openReportsFuture = repository.retrieveOpenReportsForMaintainer(username);
                closedReportsFuture = repository.retrieveClosedReportsForMaintainer(username);
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + user.getRole());
        }
        openReportsFuture.onSuccess(reports -> Platform.runLater(() ->
                this.openReportsTable.setItems(FXCollections.observableList(reports))));

        closedReportsFuture.onSuccess(reports -> Platform.runLater(() ->
                this.closedReportsTable.setItems(FXCollections.observableList(reports))));

        this.openReportsTable = FXHelper.generateTable("openReportsTable",
                value -> this.openReportsPane.setDetailNode(new Label(value.toString())),
                value -> this.openReportsPane.setDetailNode(new Label(value.toString())));

        this.closedReportsTable = FXHelper.generateTable("closedReportsTable",
                value -> this.closedReportsPane.setDetailNode(new Label(value.toString())),
                value -> this.closedReportsPane.setDetailNode(new Label(value.toString())));

        this.openReportsPane.setMasterNode(this.openReportsTable);
        this.closedReportsPane.setMasterNode(this.closedReportsTable);
        this.openReportsTable.itemsProperty().addListener(new NegligenceReportListener<>(() ->
                this.openReportsTable.getColumns().forEach(col -> col.setResizable(false))));
        this.closedReportsTable.itemsProperty().addListener(new NegligenceReportListener<>(() ->
                this.closedReportsTable.getColumns().forEach(col -> col.setResizable(false))));
    }


    /**
     * Sets a {@link javafx.event.EventHandler} on closed reports tab selection changed.
     * @param tabSelectedConsumer {@link Consumer} to use on tab selection
     */
    public void setOnClosedTabSelectionChanged(final Consumer<Boolean> tabSelectedConsumer) {
        this.closedReportsTab.setOnSelectionChanged(ignored ->
                tabSelectedConsumer.accept(this.closedReportsTab.isSelected()));
    }

    /**
     * {@link ChangeListener} that on changed event it runs a {@link Runnable} and remove itself from listening over.
     * @param <T> type parameter
     */
    private static final class NegligenceReportListener<T> implements ChangeListener<ObservableList<T>> {

        private final Runnable changedRunnable;

        private NegligenceReportListener(final Runnable changedRunnable) {
            this.changedRunnable = changedRunnable;
        }

        @Override
        public void changed(final @NotNull ObservableValue<? extends ObservableList<T>> observable,
                            final ObservableList<T> oldValue,
                            final ObservableList<T> newValue) {
            this.changedRunnable.run();
            observable.removeListener(this);
        }
    }

}
