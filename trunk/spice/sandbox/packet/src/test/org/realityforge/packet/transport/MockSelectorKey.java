/*
 * Copyright (C) The Spice Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */
package org.realityforge.packet.transport;

import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SelectableChannel;

/**
 * @author <a href="mailto:peter at realityforge.org">Peter Donald</a>
 * @version $Revision: 1.1 $ $Date: 2003-11-26 04:34:02 $
 */
class MockSelectorKey
    extends SelectionKey
{
    public int interestOps()
    {
        return 0;
    }

    public SelectionKey interestOps( int ops )
    {
        return null;
    }

    public Selector selector()
    {
        return null;
    }

    public int readyOps()
    {
        return 0;
    }

    public boolean isValid()
    {
        return false;
    }

    public SelectableChannel channel()
    {
        return null;
    }

    public void cancel()
    {
    }
}
