/*
 * Copyright (C) The Spice Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */
package org.jcomponent.netserve.sockets.impl;

import java.net.ServerSocket;
import java.net.Socket;
import java.io.IOException;

import org.jcomponent.netserve.selector.impl.NullSelectorMonitor;

/**
 * A noop monitor.
 *
 * @author <a href="mailto:peter at realityforge.org">Peter Donald</a>
 * @version $Revision: 1.5 $ $Date: 2003-10-24 03:18:44 $
 */
public class NullNIOAcceptorMonitor
   extends NullSelectorMonitor
   implements NIOAcceptorMonitor
{
   /**
    * Add constant for instance of Null Monitor.
    */
   public static final NullNIOAcceptorMonitor MONITOR = new NullNIOAcceptorMonitor();

   /**
    * @see NIOAcceptorMonitor#acceptorCreated
    */
   public void acceptorCreated( final String name,
                                final ServerSocket serverSocket )
   {
   }

   /**
    * @see NIOAcceptorMonitor#acceptorClosing
    */
   public void acceptorClosing( final String name )
   {
   }

   /**
    * @see NIOAcceptorMonitor#errorAcceptingConnection
    */
   public void errorAcceptingConnection( final String name,
                                         final IOException ioe )
   {
   }

   /**
    * @see NIOAcceptorMonitor#handlingConnection
    */
   public void handlingConnection( final String name,
                                   final ServerSocket serverSocket,
                                   final Socket socket )
   {
   }
}
