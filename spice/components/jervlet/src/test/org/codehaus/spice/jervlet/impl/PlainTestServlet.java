/*
 * Copyright (C) The Spice Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */
package org.codehaus.spice.jervlet.impl;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Test servlet
 *
 * @author Johan Sjoberg
 */
public class PlainTestServlet extends HttpServlet
{
    /**
     * Perform an HTTP GET, meaning write back whatever the pinger
     * was pinged with.
     *
     * @param request the request
     * @param response the response
     */
    public void doGet( HttpServletRequest request, HttpServletResponse response )
    {
        System.out.println( "PlainTestServlet doGet called." );
        try
        {
            response.getWriter().print( this.getClass().getName() );
            response.getWriter().flush();
            response.getWriter().close();
        }
        catch( IOException ioe )
        {
            ioe.printStackTrace();
        }
    }
}
