/*
 * Copyright (C) The Spice Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */
package org.codehaus.spice.jervlet.impl.avalon;

import org.codehaus.spice.jervlet.impl.Pinger;
import org.apache.avalon.framework.logger.LogEnabled;
import org.apache.avalon.framework.logger.Logger;
import org.apache.avalon.framework.logger.ConsoleLogger;
import org.apache.avalon.framework.service.Serviceable;
import org.apache.avalon.framework.service.ServiceManager;
import org.apache.avalon.framework.service.ServiceException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * Avalon style servlet filter, for testing.
 *
 * @author Johan Sjoberg
 */
public class AvalonTestServlet extends HttpServlet
    implements LogEnabled, Serviceable
{
    /** Pinger */
    Pinger m_pinger;

    /** Logger */
    Logger m_logger = new ConsoleLogger();

    /**
     * Get the logger
     *
     * @param logger
     */
    public void enableLogging( Logger logger )
    {
        m_logger = logger;
    }

    /**
     * Get references, the pinger, and ping it.
     *
     * @param serviceManager a service manager with the needed references
     * @throws ServiceException if something goes wrong
     */
    public void service( ServiceManager serviceManager ) throws ServiceException
    {
        m_logger.info( "AvalonTestServlet service called." );
        m_pinger = (Pinger)serviceManager.lookup( "pinger" );
        m_pinger.ping( this.getClass().getName() + " "
          + m_pinger.getMessages().get( 0 ).toString() );
    }

    /**
     * Perform an HTTP GET, meaning write back whatever the pinger
     * was pinged with.
     *
     * @param request the request
     * @param response the response
     */
    public void doGet( HttpServletRequest request, HttpServletResponse response )
    {
        m_logger.info( "AvalonTestServlet doGet called." );
        try
        {
            response.getWriter().print( this.getClass().getName()
              + " " + m_pinger.getMessages().get( 0 ).toString() );
            response.getWriter().flush();
            response.getWriter().close();
        }
        catch( IOException ioe )
        {
            ioe.printStackTrace();
        }
    }
}