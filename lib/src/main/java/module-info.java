open module it.unibo.dronesecurity.lib {
    exports it.unibo.dronesecurity.lib;

    requires transitive software.amazon.awssdk;
    requires transitive software.amazon.awssdk.iot;
    requires transitive java.logging;

    requires com.google.gson;
    requires org.jetbrains.annotations;
}