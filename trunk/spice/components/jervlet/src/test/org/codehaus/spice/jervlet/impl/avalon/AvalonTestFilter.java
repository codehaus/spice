/*
 * Copyright (C) The Spice Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */
package org.codehaus.spice.jervlet.impl.avalon;

import org.codehaus.spice.jervlet.impl.Pinger;

import org.apache.avalon.framework.logger.Logger;
import org.apache.avalon.framework.logger.LogEnabled;
import org.apache.avalon.framework.logger.ConsoleLogger;
import org.apache.avalon.framework.service.ServiceManager;
import org.apache.avalon.framework.service.ServiceException;
import org.apache.avalon.framework.service.Serviceable;

import javax.servlet.Filter;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * Avalon style servlet filter, for testing.
 *
 * @author Johan Sjoberg
 */
public class AvalonTestFilter implements Filter, LogEnabled, Serviceable
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
     * @throws org.apache.avalon.framework.service.ServiceException if something goes wrong
     */
    public void service( ServiceManager serviceManager ) throws ServiceException
    {
        m_logger.info( "AvalonTestFilter service called." );
        m_pinger = (Pinger)serviceManager.lookup( "pinger" );
        m_pinger.ping( this.getClass().getName() + " "
          + m_pinger.getMessages().get( 0 ).toString() );
    }

    /**
     * Initialize the filter
     */
    public void init( FilterConfig filterConfig ) throws ServletException
    {
        m_logger.info( "AvalonTestFilter init called." );
    }

    /**
     * Filter a transaction, meaning write back whatever the pinger
     * was pinged with.
     *
     * @param request the request
     * @param response the response
     * @param chain the next step
     */
    public void doFilter( ServletRequest request,
                          ServletResponse response,
                          FilterChain chain ) throws IOException, ServletException
    {
        m_logger.info( "AvalonTestFilter doFilter called." );
        long before = System.currentTimeMillis();
        try
        {
            response.getWriter().print( this.getClass().getName()
              + " " + m_pinger.getMessages().get( 0 ).toString() + " " );
            response.getWriter().flush();
        }
        catch( IOException ioe )
        {
            ioe.printStackTrace();
        }
        chain.doFilter( request, response );
        long after = System.currentTimeMillis();
        String name = "";
        if( request instanceof HttpServletRequest )
        {
            name = ((HttpServletRequest)request).getRequestURI();
        }
        m_logger.info( "AvalonTestFilter processing for " + name + " took "
          + ( after - before ) + "ms." );
    }

    /**
     * Destroy the filter
     */
    public void destroy()
    {
        m_logger.info( "AvalonTestFilter destroy called." );
    }
}
