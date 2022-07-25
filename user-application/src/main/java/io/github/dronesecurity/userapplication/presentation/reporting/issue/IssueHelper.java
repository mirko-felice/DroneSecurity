/*
 * Copyright (c) 2021-2022, Mirko Felice & Maxim Derevyanchenko. All rights reserved.
 * Licensed under the MIT license. See LICENSE file in the project root for details.
 */

package io.github.dronesecurity.userapplication.presentation.reporting.issue;

import io.github.dronesecurity.userapplication.domain.reporting.issue.activeissue.entities.AbstractActiveIssue;
import io.github.dronesecurity.userapplication.domain.reporting.issue.createdissue.entities.AbstractCreatedIssue;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import org.jetbrains.annotations.NotNull;

import java.util.Comparator;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;

/**
 * Helper for {@link IssuesUIController}.
 */
public final class IssueHelper {

    private IssueHelper() { }

    /**
     * Initialize a {@link TableView} of class extending {@link AbstractCreatedIssue}.
     * @param table table to initialize
     * @param idColumn {@link TableColumn} of the identifier
     * @param subjectColumn {@link TableColumn} of the subject
     * @param courierColumn {@link TableColumn} of the courier
     * @param droneIdColumn {@link TableColumn} of the drone ide
     * @param onIssueSelected {@link Consumer} to accept when the issue is being selected
     * @param <T> type parameter of the table
     */
    public static <T extends AbstractCreatedIssue> void initTable(final @NotNull TableView<T> table,
                                                            final @NotNull TableColumn<T, String> idColumn,
                                                            final @NotNull TableColumn<T, String> subjectColumn,
                                                            final @NotNull TableColumn<T, String> courierColumn,
                                                            final @NotNull TableColumn<T, String> droneIdColumn,
                                                            final Consumer<T> onIssueSelected) {
        idColumn.setCellValueFactory(val -> new SimpleStringProperty("#" + val.getValue().getId().getId()));
        subjectColumn.setCellValueFactory(val -> new SimpleStringProperty(val.getValue().getSubject()));
        courierColumn.setCellValueFactory(val -> new SimpleStringProperty(val.getValue().getCourier()));
        droneIdColumn.setCellValueFactory(val -> new SimpleStringProperty(val.getValue().getDroneId()));

        final TableView.TableViewSelectionModel<T> selectionModel = table.getSelectionModel();
        selectionModel.setSelectionMode(SelectionMode.SINGLE);
        selectionModel.selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null)
                onIssueSelected.accept(newValue);
        });
    }

    /**
     * Refresh open issues table.
     * @param table table to refresh
     * @param issues issues to set
     */
    public static void refreshOpenIssues(final @NotNull TableView<AbstractActiveIssue> table,
                                         final List<AbstractActiveIssue> issues) {
        Platform.runLater(() -> table.setItems(
                FXCollections.observableList(issues.stream()
                        .sorted(Comparator.comparingLong(issue -> issue.getId().getId()))
                        .collect(Collectors.toList()))));
    }
}
