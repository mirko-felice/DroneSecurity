open module it.unibo.dronesecurity.lib {
    exports it.unibo.dronesecurity.lib;

    requires transitive java.logging;
    requires transitive javafx.controls;
    requires transitive javafx.fxml;

    requires software.amazon.awssdk;
    requires software.amazon.awssdk.iot;
    requires com.fasterxml.jackson.databind;
    requires org.jetbrains.annotations;
    requires org.slf4j;
}
