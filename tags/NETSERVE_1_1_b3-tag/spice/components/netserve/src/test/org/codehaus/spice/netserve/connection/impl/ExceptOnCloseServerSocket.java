/*
 * Copyright (C) The Spice Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */
package org.codehaus.spice.netserve.connection.impl;

import java.io.IOException;
import java.net.ServerSocket;

/**
 *
 * @author Peter Donald
 * @version $Revision: 1.2 $ $Date: 2004-03-21 23:42:59 $
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
