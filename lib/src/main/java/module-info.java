open module it.unibo.dronesecurity.lib {
    exports it.unibo.dronesecurity.lib;

    requires transitive javafx.controls;
    requires transitive javafx.fxml;

    requires aws.iot.device.sdk;
    requires aws.crt;
    requires com.fasterxml.jackson.databind;
    requires org.jetbrains.annotations;
    requires org.slf4j;
}
