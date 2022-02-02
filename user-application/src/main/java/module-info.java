open module it.unibo.dronesecurity.userapplication {
    requires javafx.controls;
    requires javafx.fxml;
    requires io.vertx.web.client;
    requires org.jetbrains.annotations;
    requires io.vertx.core;
    requires java.logging;
    requires io.vertx.web.openapi;
    requires io.vertx.web.validation;
    requires io.vertx.web;
    requires com.fasterxml.jackson.databind;
    requires software.amazon.awssdk;
    requires software.amazon.awssdk.iot;
    requires io.vertx.client.mongo;
    requires org.controlsfx.controls;
    exports it.unibo.dronesecurity.userapplication;
}