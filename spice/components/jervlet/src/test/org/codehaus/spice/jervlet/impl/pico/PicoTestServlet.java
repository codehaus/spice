/*
 * Copyright (C) The Spice Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */
package org.codehaus.spice.jervlet.impl.pico;

import org.codehaus.spice.jervlet.impl.Pinger;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Picocontainer style servlet, for testing.
 *
 * @author Johan Sjoberg
 */
public class PicoTestServlet extends HttpServlet
{
    /** Pinger */
    Pinger m_pinger;

    /**
     * Get a pinger and ping back.
     *
     * @param pinger the Pinger to ping
     */
    public PicoTestServlet( Pinger pinger )
    {
        System.out.println( "PicoTestServlet created." );
        m_pinger = pinger;
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
        System.out.println( "PicoTestServlet doGet called." );
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
