/*
 * Copyright (C) The Spice Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */
package org.codehaus.spice.netevent.events;

import java.io.IOException;
import java.nio.channels.ServerSocketChannel;

/**
 * An Event indicating that ServerSocket failed to accept a socket.
 * 
 * @author Peter Donald
 * @version $Revision: 1.1 $ $Date: 2004-01-08 03:41:13 $
 */
public class AcceptErrorEvent
    extends ServerSocketEvent
{
    /** the exception. */
    private final IOException m_ioe;

    /**
     * Create event.
     * 
     * @param channel the channel.
     */
    public AcceptErrorEvent( final ServerSocketChannel channel,
                             final IOException ioe )
    {
        super( channel );
        if( null == ioe )
        {
            throw new NullPointerException( "ioe" );
        }
        m_ioe = ioe;
    }

    /**
     * Return the exception.
     * 
     * @return the exception.
     */
    public IOException getIoe()
    {
        return m_ioe;
    }
}
