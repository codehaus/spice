package org.codehaus.spice.event.impl;

import org.codehaus.spice.event.EventSource;

class BlockingTestEventSource
    implements EventSource
{
    static final Object EVENT = new Object();
    static final Object[] EVENTS = new Object[] {EVENT};

    private final Object m_lock = new Object();
    private boolean m_unlocked;

    void unlock()
    {
        m_unlocked = true;
        synchronized( m_lock )
        {
            m_lock.notifyAll();
        }
    }

    public Object getEvent()
    {
        if( m_unlocked )
        {
            return EVENT;
        }
        return null;
    }

    public Object[] getEvents( int count )
    {
        if( m_unlocked )
        {
            return EVENTS;
        }
        return new Object[0];
    }

    public Object getSourceLock()
    {
        return m_lock;
    }
}
