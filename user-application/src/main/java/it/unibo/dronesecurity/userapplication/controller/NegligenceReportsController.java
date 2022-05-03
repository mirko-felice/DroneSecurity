package it.unibo.dronesecurity.userapplication.controller;

import io.vertx.core.CompositeFuture;
import io.vertx.core.Future;
import it.unibo.dronesecurity.userapplication.auth.entities.User;
import it.unibo.dronesecurity.userapplication.negligence.entities.ClosedNegligenceReport;
import it.unibo.dronesecurity.userapplication.negligence.entities.NegligenceReport;
import it.unibo.dronesecurity.userapplication.negligence.entities.OpenNegligenceReport;
import it.unibo.dronesecurity.userapplication.negligence.repo.NegligenceRepository;
import it.unibo.dronesecurity.userapplication.utilities.FXHelper;
import it.unibo.dronesecurity.userapplication.utilities.UserHelper;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import org.controlsfx.control.MasterDetailPane;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Controller dedicated to list and control {@link NegligenceReport}.
 */
public class NegligenceReportsController implements Initializable {

    private static final ScheduledExecutorService SCHEDULER = Executors.newScheduledThreadPool(2);
    private static final long REPORT_LOADING_TIME = 500;
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
        openReportsFuture.onSuccess(reports -> {
            this.openReportsTable.setItems(FXCollections.observableList(reports));
            SCHEDULER.schedule(() -> this.openReportsTable.getColumns().forEach(col -> col.setResizable(false)),
                    REPORT_LOADING_TIME, TimeUnit.MILLISECONDS);
        });

        closedReportsFuture.onSuccess(reports -> {
            this.closedReportsTable.setItems(FXCollections.observableList(reports));
            SCHEDULER.schedule(() -> this.closedReportsTable.getColumns().forEach(col -> col.setResizable(false)),
                    REPORT_LOADING_TIME, TimeUnit.MILLISECONDS);
        });

        CompositeFuture.join(openReportsFuture, closedReportsFuture).onSuccess(ignored -> SCHEDULER.shutdown());
        this.openReportsTable = FXHelper.generateTable("openReportsTable",
                value -> this.openReportsPane.setDetailNode(new Label(value.toString())),
                value -> this.openReportsPane.setDetailNode(new Label(value.toString())));

        this.closedReportsTable = FXHelper.generateTable("closedReportsTable",
                value -> this.closedReportsPane.setDetailNode(new Label(value.toString())),
                value -> this.closedReportsPane.setDetailNode(new Label(value.toString())));

        this.openReportsPane.setMasterNode(this.openReportsTable);
        this.closedReportsPane.setMasterNode(this.closedReportsTable);
    }

}
