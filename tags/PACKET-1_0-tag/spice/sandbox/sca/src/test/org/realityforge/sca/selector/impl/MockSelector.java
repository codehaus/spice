/*
 * Copyright (C) The Spice Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */
package org.realityforge.sca.selector.impl;

import java.io.IOException;
import java.nio.channels.Selector;
import java.nio.channels.spi.SelectorProvider;
import java.util.Set;

/**
 * @author Peter Donald
 * @version $Revision: 1.3 $ $Date: 2003-12-05 05:39:34 $
 */
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

    public int selectNow()
        throws IOException
    {
        return 0;
    }

    public int select( long timeout )
        throws IOException
    {
        return 0;
    }

    public int select()
        throws IOException
    {
        return 0;
    }

    public Selector wakeup()
    {
        return null;
    }

    public void close()
        throws IOException
    {
        EXCEPTION.fillInStackTrace();
        throw EXCEPTION;
    }
}
