/*
 * Copyright (C) The Spice Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */
package org.jcomponent.netserve.sockets.impl;

import java.net.ServerSocket;
import java.io.IOException;

/**
 *
 * @author <a href="mailto:peter at realityforge.org">Peter Donald</a>
 * @version $Revision: 1.1 $ $Date: 2003-10-08 05:02:02 $
 */
public interface AcceptorMonitor
{
    void acceptorClosed( String name, ServerSocket serverSocket );

    void serverSocketListening( String name, ServerSocket serverSocket );

    void errorAcceptingConnection( String name, IOException ioe );

    void errorClosingServerSocket( String name, IOException ioe );
}
