package org.codehaus.spice.event.impl;

import org.codehaus.spice.event.EventJoin;

class DummyEventSource
    extends AbstractEventSource
{
    private boolean m_refreshCall;
    private boolean m_openCall;
    private boolean m_closeCall;

    public DummyEventSource( final EventJoin join )
    {
        super( join );
    }

    public void open()
        throws Exception
    {
        super.open();
        m_openCall = true;
    }

    public void close()
        throws Exception
    {
        super.close();
        m_closeCall = true;
    }

    protected void refresh()
    {
        m_refreshCall = true;
    }

    public boolean isRefreshCall()
    {
        return m_refreshCall;
    }

    public boolean isOpenCall()
    {
        return m_openCall;
    }

    public boolean isCloseCall()
    {
        return m_closeCall;
    }
}
