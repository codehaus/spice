package org.codehaus.spice.packet.handlers;

import org.codehaus.spice.packet.session.Session;

/**
 * @author Peter Donald
 * @version $Revision: 1.1 $ $Date: 2004-03-22 01:17:50 $
 */
class TimerEntry
{
    private final String _reason;
    private final Session _session;

    TimerEntry( final String reason, final Session session )
    {
        _reason = reason;
        _session = session;
    }

    public String getReason()
    {
        return _reason;
    }

    public Session getSession()
    {
        return _session;
    }

    public String toString()
    {
        return "Timer[Reason=" + _reason + ", Session=" + _session + "]";
    }
}
