open module io.github.dronesecurity.lib {
    exports io.github.dronesecurity.lib;

    requires transitive javafx.controls;
    requires transitive javafx.fxml;

    requires aws.iot.device.sdk;
    requires aws.crt;
    requires com.fasterxml.jackson.databind;
    requires org.jetbrains.annotations;
    requires org.slf4j;
}
