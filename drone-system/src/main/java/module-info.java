open module it.unibo.dronesecurity.dronesystem {
    exports it.unibo.dronesecurity.dronesystem;

    requires it.unibo.dronesecurity.lib;
    requires org.apache.commons.exec;
    requires org.apache.commons.lang3;
    requires org.jetbrains.annotations;
    requires com.fasterxml.jackson.databind;
    requires aws.iot.device.sdk;
    requires aws.crt;
    requires org.slf4j;
}
