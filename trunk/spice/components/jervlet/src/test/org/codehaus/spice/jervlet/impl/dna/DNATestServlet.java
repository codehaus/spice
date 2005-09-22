/*
 * Copyright (C) The Spice Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */
package org.codehaus.spice.jervlet.impl.dna;

import org.codehaus.spice.jervlet.impl.Pinger;
import org.codehaus.dna.LogEnabled;
import org.codehaus.dna.Composable;
import org.codehaus.dna.Logger;
import org.codehaus.dna.MissingResourceException;
import org.codehaus.dna.ResourceLocator;
import org.codehaus.dna.impl.ConsoleLogger;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * DNA style servlet filter, for testing.
 *
 * @author Johan Sjoberg
 */
public class DNATestServlet extends HttpServlet
    implements LogEnabled, Composable
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
     * Get resources, the pinger, and ping it.
     *
     * @param locator a resource locator with the needed resources
     * @throws MissingResourceException if something goes wrong
     */
    public void compose( ResourceLocator locator ) throws MissingResourceException
    {
        m_logger.info( "DNATestServlet service called." );
        m_pinger = (Pinger)locator.lookup( "pinger" );
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
        m_logger.info( "DNATestServlet doGet called." );
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