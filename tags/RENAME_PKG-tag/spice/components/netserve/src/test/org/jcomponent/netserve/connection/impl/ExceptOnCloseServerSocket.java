/*
 * Copyright (C) The Spice Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */
package org.jcomponent.netserve.connection.impl;

import java.io.IOException;
import java.net.ServerSocket;

/**
 *
 * @author <a href="mailto:peter at realityforge.org">Peter Donald</a>
 * @version $Revision: 1.1 $ $Date: 2003-10-27 05:26:54 $
 */
class ExceptOnCloseServerSocket
   extends ServerSocket
{
   static final IOException EXCEPTION = new IOException( "No Close - ha ha!" );

   public ExceptOnCloseServerSocket()
      throws IOException
   {
   }

   public void close() throws IOException
   {
      throw EXCEPTION;
   }
}
