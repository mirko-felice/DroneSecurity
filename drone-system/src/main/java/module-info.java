open module it.unibo.dronesecurity.dronesystem {
    exports it.unibo.dronesecurity.dronesystem;

    requires it.unibo.dronesecurity.lib;
    requires org.apache.commons.exec;
    requires org.jetbrains.annotations;
    requires com.fasterxml.jackson.databind;
    requires software.amazon.awssdk;
    requires org.slf4j;
}
