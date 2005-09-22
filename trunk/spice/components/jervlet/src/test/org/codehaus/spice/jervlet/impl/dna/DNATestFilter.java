/*
 * Copyright (C) The Spice Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */
package org.codehaus.spice.jervlet.impl.dna;

import org.codehaus.spice.jervlet.impl.Pinger;
import org.codehaus.dna.Composable;
import org.codehaus.dna.Logger;
import org.codehaus.dna.ResourceLocator;
import org.codehaus.dna.MissingResourceException;
import org.codehaus.dna.LogEnabled;
import org.codehaus.dna.impl.ConsoleLogger;

import javax.servlet.Filter;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * DNA style servlet filter, for testing.
 *
 * @author Johan Sjoberg
 */
public class DNATestFilter implements Filter, LogEnabled, Composable
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
     * @param locator a service manager with the needed references
     * @throws org.codehaus.dna.MissingResourceException if something goes wrong
     */
    public void compose( ResourceLocator locator ) throws MissingResourceException
    {
        m_logger.info( "DNATestFilter service called." );
        m_pinger = (Pinger)locator.lookup( "pinger" );
        m_pinger.ping( this.getClass().getName() + " "
          + m_pinger.getMessages().get( 0 ).toString() );
    }

    /**
     * Initialize the filter
     */
    public void init( FilterConfig filterConfig ) throws ServletException
    {
        m_logger.info( "DNATestFilter init called." );
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
        m_logger.info( "DNATestFilter doFilter called." );
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
        m_logger.info( "DNATestFilter processing for " + name + " took "
          + ( after - before ) + "ms." );
    }

    /**
     * Destroy the filter
     */
    public void destroy()
    {
        m_logger.info( "DNATestFilter destroy called." );
    }
}
