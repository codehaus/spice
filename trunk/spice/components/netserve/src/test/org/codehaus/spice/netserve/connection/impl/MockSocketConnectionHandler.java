/*
 * Copyright (C) The Spice Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */
package org.codehaus.spice.netserve.connection.impl;

import java.net.Socket;

import org.codehaus.spice.netserve.connection.handlers.AbstractRequestHandler;

/**
 *
 * @author Peter Donald
 * @version $Revision: 1.2 $ $Date: 2004-03-21 23:42:59 $
 */
class MockSocketConnectionHandler
   extends AbstractRequestHandler
{
   private Socket m_socket;

   protected void doPerformRequest( Socket socket )
      throws Exception
   {
      m_socket = socket;
   }

   Socket getSocket()
   {
      return m_socket;
   }
}
