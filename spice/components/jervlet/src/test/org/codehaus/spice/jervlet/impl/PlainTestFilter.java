/*
 * Copyright (C) The Spice Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */
package org.codehaus.spice.jervlet.impl;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * Test servlet-filter
 *
 * @author Johan Sjoberg
 */
public class PlainTestFilter implements Filter
{
    /**
     * Initialize the filter
     */
    public void init( FilterConfig filterConfig ) throws ServletException
    {
        System.out.println( "PlainTestFilter init called." );
    }

    /**
     * Filter a transaction
     *
     * @param request the request
     * @param response the response
     * @param chain the next step
     */
    public void doFilter( ServletRequest request,
                          ServletResponse response,
                          FilterChain chain ) throws IOException, ServletException
    {
        System.out.println( "PlainTestFilter doFilter called." );
        long before = System.currentTimeMillis();
        try
        {
            response.getWriter().print( this.getClass().getName() + " " );
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
        System.out.println( "PlainTestFilter processing for " + name + " took "
          + ( after - before ) + "ms." );
    }

    /**
     * Destroy the filter
     */
    public void destroy()
    {
        System.out.println( "PlainTestFilter destroy called." );
    }
}
