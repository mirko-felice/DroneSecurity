open module it.unibo.dronesecurity.lib {
    exports it.unibo.dronesecurity.lib;

    requires transitive software.amazon.awssdk;
    requires transitive software.amazon.awssdk.iot;
    requires transitive java.logging;
    requires transitive javafx.controls;
    requires transitive javafx.fxml;

    requires com.fasterxml.jackson.databind;
    requires org.jetbrains.annotations;
}
