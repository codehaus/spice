/*
 * Copyright (C) The Spice Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */
package org.codehaus.spice.netserve.connection.handlers;

import java.net.Socket;

/**
 *
 * @author <a href="mailto:peter at realityforge.org">Peter Donald</a>
 * @version $Revision: 1.1 $ $Date: 2003-11-20 00:25:05 $
 */
class MockRequestHandler
    extends AbstractRequestHandler
{
    private Socket m_performRequestSocket;

    private Socket m_errorClosingConnectionSocket;
    private Throwable m_errorClosingConnectionThrowable;

    private Socket m_errorHandlingConnectionSocket;
    private Throwable m_errorHandlingConnectionThrowable;

    protected void doPerformRequest( Socket socket )
        throws Exception
    {
        m_performRequestSocket = socket;
    }

    protected void errorClosingConnection( Socket socket,
                                           Throwable t )
    {
        super.errorClosingConnection( socket, t );
        m_errorClosingConnectionSocket = socket;
        m_errorClosingConnectionThrowable = t;
    }

    protected void errorHandlingConnection( Socket socket,
                                            Throwable t )
    {
        super.errorHandlingConnection( socket, t );
        m_errorHandlingConnectionSocket = socket;
        m_errorHandlingConnectionThrowable = t;
    }

    Socket getPerformRequestSocket()
    {
        return m_performRequestSocket;
    }

    Socket getErrorClosingConnectionSocket()
    {
        return m_errorClosingConnectionSocket;
    }

    Throwable getErrorClosingConnectionThrowable()
    {
        return m_errorClosingConnectionThrowable;
    }

    Socket getErrorHandlingConnectionSocket()
    {
        return m_errorHandlingConnectionSocket;
    }

    Throwable getErrorHandlingConnectionThrowable()
    {
        return m_errorHandlingConnectionThrowable;
    }
}

