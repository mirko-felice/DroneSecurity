open module io.github.dronesecurity.lib {
    exports io.github.dronesecurity.lib.shared;
    exports io.github.dronesecurity.lib.connection;
    exports io.github.dronesecurity.lib.utilities;

    requires aws.iot.device.sdk;
    requires aws.crt;
    requires com.fasterxml.jackson.databind;
    requires org.jetbrains.annotations;
    requires org.slf4j;
}
