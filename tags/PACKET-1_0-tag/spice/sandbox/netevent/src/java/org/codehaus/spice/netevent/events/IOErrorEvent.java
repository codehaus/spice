/*
 * Copyright (C) The Spice Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */
package org.codehaus.spice.netevent.events;

import java.io.IOException;
import org.codehaus.spice.netevent.transport.ChannelTransport;

/**
 * Abstract event for IO errors.
 * 
 * @author Peter Donald
 * @version $Revision: 1.4 $ $Date: 2004-01-08 03:41:13 $
 */
public abstract class IOErrorEvent
    extends AbstractTransportEvent
{
    /** The Error. */
    private final IOException m_ioe;

    /**
     * Create event for transport with error.
     * 
     * @param transport the transport
     * @param ioe the error
     */
    public IOErrorEvent( final ChannelTransport transport,
                         final IOException ioe )
    {
        super( transport );
        if( null == ioe )
        {
            throw new NullPointerException( "ioe" );
        }
        m_ioe = ioe;
    }

    /**
     * Return the error.
     * 
     * @return the error.
     */
    public IOException getIoe()
    {
        return m_ioe;
    }

    /**
     * @see AbstractTransportEvent#getEventDescription()
     */
    protected String getEventDescription()
    {
        return getIoe() + " error connected to " + super.getEventDescription();
    }
}
