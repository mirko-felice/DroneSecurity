/*
 * Copyright (c) 2021-2022, Mirko Felice & Maxim Derevyanchenko. All rights reserved.
 * Licensed under the MIT license. See LICENSE file in the project root for details.
 */

package io.github.dronesecurity.userapplication.controller;

import io.github.dronesecurity.userapplication.auth.entities.LoggedUser;
import io.github.dronesecurity.userapplication.negligence.entities.ClosedNegligenceReport;
import io.github.dronesecurity.userapplication.negligence.entities.NegligenceReport;
import io.github.dronesecurity.userapplication.negligence.entities.OpenNegligenceReport;
import io.github.dronesecurity.userapplication.negligence.repo.NegligenceRepository;
import io.github.dronesecurity.userapplication.utilities.FXHelper;
import io.github.dronesecurity.userapplication.utilities.UserHelper;
import io.vertx.core.Future;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Tab;
import javafx.scene.control.TableView;
import javafx.util.Pair;
import org.controlsfx.control.MasterDetailPane;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.function.Consumer;

/**
 * Controller dedicated to list and control {@link NegligenceReport}.
 */
public class NegligenceDataController implements Initializable {

    private static final String DETAIL_FILENAME = "detail.fxml";
    @FXML private Tab closedReportsTab;
    @FXML private MasterDetailPane openReportsPane;
    @FXML private MasterDetailPane closedReportsPane;
    private TableView<OpenNegligenceReport> openReportsTable;
    private TableView<ClosedNegligenceReport> closedReportsTable;
    private DetailController openController;
    private DetailController closedController;

    /**
     * {@inheritDoc}
     */
    @Override
    public void initialize(final URL location, final ResourceBundle resources) {
       this.updateReports();

        final URL fileUrl = NegligenceDataController.class.getResource(DETAIL_FILENAME);
        final Pair<TableView<OpenNegligenceReport>, DetailController> openPair =
                FXHelper.generateTableWithDetails(fileUrl, this.openReportsPane, "openReports");

        if (openPair != null) {
            this.openReportsTable = openPair.getKey();
            this.openController = openPair.getValue();
            this.openReportsPane.setMasterNode(this.openReportsTable);
            this.openReportsTable.itemsProperty().addListener(new NegligenceReportListener<>(() ->
                    this.openReportsTable.getColumns().forEach(col -> col.setResizable(false))));
        }

        final Pair<TableView<ClosedNegligenceReport>, DetailController> closedPair =
                FXHelper.generateTableWithDetails(fileUrl, this.closedReportsPane, "closedReports");

        if (closedPair != null) {
            this.closedReportsTable = closedPair.getKey();
            this.closedController = closedPair.getValue();
            this.closedReportsPane.setMasterNode(this.closedReportsTable);
            this.closedReportsTable.itemsProperty().addListener(new NegligenceReportListener<>(() ->
                    this.closedReportsTable.getColumns().forEach(col -> col.setResizable(false))));
        }
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
     * Empty the detail nodes.
     */
    public void emptyDetails() {
        this.openController.emptyDetails();
        this.closedController.emptyDetails();
    }

    /**
     * Retrieve the selected {@link OpenNegligenceReport}.
     * @return a {@link OpenNegligenceReport}
     */
    public @Nullable OpenNegligenceReport getSelectedOpenReport() {
        return this.openReportsTable.getSelectionModel().getSelectedItem();
    }

    /**
     * Update showed reports.
     */
    public void updateReports() {
        final LoggedUser user = UserHelper.getLoggedUser();
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
