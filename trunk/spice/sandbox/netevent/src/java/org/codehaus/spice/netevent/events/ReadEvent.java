/*
 * Copyright (C) The Spice Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */
package org.codehaus.spice.netevent.events;

import java.nio.ByteBuffer;
import org.codehaus.spice.netevent.transport.ChannelTransport;

/**
 * Event indicating events were read.
 * 
 * @author Peter Donald
 * @version $Revision: 1.4 $ $Date: 2004-01-20 01:08:30 $
 */
public class ReadEvent
    extends AbstractTransportEvent
{
    /** The Buffer. */
    private final ByteBuffer m_buffer;

    /**
     * Create event.
     * 
     * @param transport the transport
     * @param buffer the ByteBuffer
     */
    public ReadEvent( final ChannelTransport transport,
                      final ByteBuffer buffer )
    {
        super( transport );
        if( null == buffer )
        {
            throw new NullPointerException( "buffer" );
        }
        m_buffer = buffer;
    }

    /**
     * Return the ByteBuffer.
     * 
     * @return the ByteBuffer.
     */
    public ByteBuffer getBuffer()
    {
        return m_buffer;
    }

    /**
     * @see AbstractTransportEvent#getEventDescription()
     */
    protected String getEventDescription()
    {
        return
            getBuffer().limit() +
            " bytes to " +
            super.getEventDescription();
    }
}
