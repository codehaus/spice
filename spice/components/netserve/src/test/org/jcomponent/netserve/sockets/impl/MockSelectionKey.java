/*
 * Copyright (C) The Spice Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */
package org.jcomponent.netserve.sockets.impl;

import java.nio.channels.SelectionKey;
import java.nio.channels.SelectableChannel;
import java.nio.channels.Selector;

/**
 *
 * @author <a href="mailto:peter at realityforge.org">Peter Donald</a>
 * @version $Revision: 1.1 $ $Date: 2003-10-09 06:45:48 $
 */
class MockSelectionKey
    extends SelectionKey
{
    public SelectableChannel channel()
    {
        return null;
    }

    public Selector selector()
    {
        return null;
    }

    public boolean isValid()
    {
        return false;
    }

    public void cancel()
    {
    }

    public int interestOps()
    {
        return 0;
    }

    public SelectionKey interestOps( int ops )
    {
        return null;
    }

    public int readyOps()
    {
        return 0;
    }
}
