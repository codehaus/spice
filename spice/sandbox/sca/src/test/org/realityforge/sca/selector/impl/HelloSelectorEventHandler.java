/*
 * Copyright (C) The Spice Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */
package org.realityforge.sca.selector.impl;

import java.io.IOException;
import java.nio.channels.SelectionKey;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import org.realityforge.sca.selector.SelectorEventHandler;

/**
 * @author Peter Donald
 * @version $Revision: 1.3 $ $Date: 2003-12-05 05:39:34 $
 */
class HelloSelectorEventHandler
    implements SelectorEventHandler
{
    static final byte[] MESSAGE = "HELLO!".getBytes();

    public void handleSelectorEvent( SelectionKey key, Object userData )
    {
        final ServerSocketChannel channel = (ServerSocketChannel)key.channel();
        try
        {
            final SocketChannel socket = channel.accept();
            socket.socket().getOutputStream().write( MESSAGE );
            socket.socket().close();
        }
        catch( IOException e )
        {
            e.printStackTrace();
        }
    }
}
