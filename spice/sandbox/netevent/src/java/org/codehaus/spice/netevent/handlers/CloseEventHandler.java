/*
 * Copyright (C) The Spice Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */
package org.codehaus.spice.netevent.handlers;

import org.codehaus.spice.event.AbstractEventHandler;
import org.codehaus.spice.event.EventHandler;
import org.codehaus.spice.netevent.events.AbstractTransportEvent;
import org.codehaus.spice.netevent.transport.TcpTransport;

/**
 * Simple handler that closes underlying transport.
 * 
 * @author Peter Donald
 * @version $Revision: 1.1 $ $Date: 2004-01-07 05:06:31 $
 */
public class CloseEventHandler
    extends AbstractEventHandler
{
    /**
     * @see EventHandler#handleEvent(Object)
     */
    public void handleEvent( final Object event )
    {
        final AbstractTransportEvent ce = (AbstractTransportEvent)event;
        final TcpTransport transport = ce.getTransport();
        transport.close();
    }
}
