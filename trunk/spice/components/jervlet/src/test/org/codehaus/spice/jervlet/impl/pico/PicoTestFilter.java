/*
 * Copyright (C) The Spice Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */
package org.codehaus.spice.jervlet.impl.pico;

import org.codehaus.spice.jervlet.impl.Pinger;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * Picocontainer style servlet filter, for testing.
 *
 * @author Johan Sjoberg
 */
public class PicoTestFilter implements Filter
{
    /** Pinger */
    Pinger m_pinger;

    /**
     * Get a pinger and ping back.
     *
     * @param pinger the Pinger to ping
     */
    public PicoTestFilter( Pinger pinger )
    {
        System.out.println( "PicoTestFilter created." );
        m_pinger = pinger;
        m_pinger.ping( this.getClass().getName() + " "
          + m_pinger.getMessages().get( 0 ).toString() );
    }

    /**
     * Initialize the filter
     */
    public void init( FilterConfig filterConfig ) throws ServletException
    {
        System.out.println( "PicoTestFilter init called." );
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
        System.out.println( "PicoTestFilter doFilter called." );
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
        System.out.println( "PicoTestFilter processing for " + name + " took "
          + ( after - before ) + "ms." );
    }

    /**
     * Destroy the filter
     */
    public void destroy()
    {
        System.out.println( "PicoTestFilter destroy called." );
    }
}