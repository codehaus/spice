/*
 * Copyright (C) The Spice Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */
package org.codehaus.spice.netserve.connection.impl;

import java.io.IOException;
import java.io.InterruptedIOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 *
 * @author Peter Donald
 * @version $Revision: 1.2 $ $Date: 2004-03-21 23:42:59 $
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
