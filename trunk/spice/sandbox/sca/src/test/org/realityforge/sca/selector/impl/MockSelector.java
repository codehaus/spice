package org.realityforge.sca.selector.impl;

import java.io.IOException;
import java.nio.channels.Selector;
import java.nio.channels.spi.SelectorProvider;
import java.util.Set;

class MockSelector
    extends Selector
{
    static final IOException EXCEPTION = new IOException( "Blah!" );

    public boolean isOpen()
    {
        return false;
    }

    public SelectorProvider provider()
    {
        return null;
    }

    public Set keys()
    {
        return null;
    }

    public Set selectedKeys()
    {
        return null;
    }

    public int selectNow() throws IOException
    {
        return 0;
    }

    public int select( long timeout )
        throws IOException
    {
        return 0;
    }

    public int select() throws IOException
    {
        return 0;
    }

    public Selector wakeup()
    {
        return null;
    }

    public void close() throws IOException
    {
        EXCEPTION.fillInStackTrace();
        throw EXCEPTION;
    }
}
