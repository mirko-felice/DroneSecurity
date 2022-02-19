open module it.unibo.dronesecurity.userapplication {
    exports it.unibo.dronesecurity.userapplication;

    requires it.unibo.dronesecurity.lib;
    requires org.jetbrains.annotations;
    requires software.amazon.awssdk;

    requires javafx.controls;
    requires javafx.fxml;
    requires io.vertx.web.client;
    requires io.vertx.core;
    requires io.vertx.web.openapi;
    requires io.vertx.web.validation;
    requires io.vertx.web;
    requires com.fasterxml.jackson.databind;
    requires com.fasterxml.jackson.annotation;
    requires io.vertx.client.mongo;
    requires org.controlsfx.controls;
    requires org.slf4j;
    requires org.apache.commons.codec;
}
