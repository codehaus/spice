/*
 * Copyright (C) The Spice Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */
package org.jcomponent.netserve.connection.impl;

import java.io.IOException;
import java.io.InterruptedIOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 *
 * @author <a href="mailto:peter at realityforge.org">Peter Donald</a>
 * @version $Revision: 1.1 $ $Date: 2003-10-27 05:26:54 $
 */
class ExceptOnAcceptServerSocket
   extends ServerSocket
{
   static final IOException ERROR_EXCEPTION = new IOException( "No Accept - ha ha!" );
   static final IOException INTERRUPTED_EXCEPTION = new InterruptedIOException( "No Interuptions!" );

   private boolean m_interupt;

   public ExceptOnAcceptServerSocket( final boolean interupt )
      throws IOException
   {
      m_interupt = interupt;
   }

   public Socket accept() throws IOException
   {
      if ( m_interupt )
      {
         throw INTERRUPTED_EXCEPTION;
      }
      else
      {
         throw ERROR_EXCEPTION;
      }
   }
}
