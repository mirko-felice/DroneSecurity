open module it.unibo.dronesecurity.userapplication {
    exports it.unibo.dronesecurity.userapplication;

    requires it.unibo.dronesecurity.lib;
    requires org.jetbrains.annotations;
    requires aws.iot.device.sdk;
    requires aws.crt;

    requires io.vertx.web.client;
    requires io.vertx.core;
    requires io.vertx.web.openapi;
    requires io.vertx.web.validation;
    requires io.vertx.web;
    requires io.vertx.client.mongo;
    requires com.fasterxml.jackson.databind;
    requires com.fasterxml.jackson.annotation;
    requires org.controlsfx.controls;
    requires org.slf4j;
    requires org.apache.commons.codec;
}
