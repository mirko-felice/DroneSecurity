open module io.github.dronesecurity.dronesystem {
    exports io.github.dronesecurity.dronesystem.drone;

    requires io.github.dronesecurity.lib;
    requires org.apache.commons.exec;
    requires org.apache.commons.lang3;
    requires org.jetbrains.annotations;
    requires com.fasterxml.jackson.databind;
    requires aws.iot.device.sdk;
    requires aws.crt;
    requires org.slf4j;
}
