/*
 * Copyright (C) The Spice Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */
package org.jcomponent.netserve.sockets.impl;

import java.io.IOException;

/**
 *
 * @author <a href="mailto:peter at realityforge.org">Peter Donald</a>
 * @version $Revision: 1.1 $ $Date: 2003-10-10 02:53:14 $
 */
class RecordingNIOAcceptorMonitor
    extends NullNIOAcceptorMonitor
{
    private IOException m_errorAcceptingConnection;

    public void errorAcceptingConnection( String name, IOException ioe )
    {
        m_errorAcceptingConnection = ioe;
        super.errorAcceptingConnection( name, ioe );
    }

    IOException getErrorAcceptingConnection()
    {
        return m_errorAcceptingConnection;
    }
}
