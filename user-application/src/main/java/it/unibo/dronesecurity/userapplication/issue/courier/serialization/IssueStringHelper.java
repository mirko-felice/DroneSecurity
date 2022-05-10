package it.unibo.dronesecurity.userapplication.issue.courier.serialization;

/**
 * String helper for repository interaction purpose.
 */
public final class IssueStringHelper {

    /**
     * DB id parameter name for issue.
     */
    public static final String ID = "ID";

    /**
     * DB subject parameter name for issue.
     */
    public static final String SUBJECT = "subject";

    /**
     * DB details parameter name for issue.
     */
    public static final String DETAILS = "details";

    /**
     * DB courier parameter name for issue.
     */
    public static final String COURIER = "courier";

    /**
     * DB sending instant parameter name for issue.
     */
    public static final String SENDING_INSTANT = "sent";

    /**
     * DB status parameter name for issue.
     */
    public static final String STATUS = "status";

    /**
     * Status value identifying an open and not visioned issue.
     */
    public static final String STATUS_OPEN = "open";

    /**
     * Status value identifying an open issue which is currently visioned by a maintainer.
     */
    public static final String STATUS_VISIONED = "visioned";

    /**
     * Status value identifying a closed issue report.
     */
    public static final String STATUS_CLOSED = "closed";

    private IssueStringHelper() { }
}
