/*

 ============================================================================
                   The Apache Software License, Version 1.1
 ============================================================================

 Copyright (C) 2002,2003 The Apache Software Foundation. All rights reserved.

 Redistribution and use in source and binary forms, with or without modifica-
 tion, are permitted provided that the following conditions are met:

 1. Redistributions of  source code must  retain the above copyright  notice,
    this list of conditions and the following disclaimer.

 2. Redistributions in binary form must reproduce the above copyright notice,
    this list of conditions and the following disclaimer in the documentation
    and/or other materials provided with the distribution.

 3. The end-user documentation included with the redistribution, if any, must
    include  the following  acknowledgment:  "This product includes  software
    developed  by the  Apache Software Foundation  (http://www.apache.org/)."
    Alternately, this  acknowledgment may  appear in the software itself,  if
    and wherever such third-party acknowledgments normally appear.

 4. The names "Jakarta", "Avalon", "Excalibur" and "Apache Software Foundation"
    must not be used to endorse or promote products derived from this  software
    without  prior written permission. For written permission, please contact
    apache@apache.org.

 5. Products  derived from this software may not  be called "Apache", nor may
    "Apache" appear  in their name,  without prior written permission  of the
    Apache Software Foundation.

 THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED WARRANTIES,
 INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
 FITNESS  FOR A PARTICULAR  PURPOSE ARE  DISCLAIMED.  IN NO  EVENT SHALL  THE
 APACHE SOFTWARE  FOUNDATION  OR ITS CONTRIBUTORS  BE LIABLE FOR  ANY DIRECT,
 INDIRECT, INCIDENTAL, SPECIAL,  EXEMPLARY, OR CONSEQUENTIAL  DAMAGES (INCLU-
 DING, BUT NOT LIMITED TO, PROCUREMENT  OF SUBSTITUTE GOODS OR SERVICES; LOSS
 OF USE, DATA, OR  PROFITS; OR BUSINESS  INTERRUPTION)  HOWEVER CAUSED AND ON
 ANY  THEORY OF LIABILITY,  WHETHER  IN CONTRACT,  STRICT LIABILITY,  OR TORT
 (INCLUDING  NEGLIGENCE OR  OTHERWISE) ARISING IN  ANY WAY OUT OF THE  USE OF
 THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.

 This software  consists of voluntary contributions made  by many individuals
 on  behalf of the Apache Software  Foundation. For more  information on the
 Apache Software Foundation, please see <http://www.apache.org/>.

*/
package org.jcomponent.jervlet.blocks.jetty;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;

import org.jcomponent.jervlet.JervletContext;

import org.mortbay.jetty.servlet.WebApplicationContext;

/**
 * Class to hold a WebApplicationContext in Jetty
 *
 * @author <a href="proyal@apache.org">peter royal</a>
 */
class WebApplicationContextHolder
{
    private final String m_context;
    private final String m_webappUrl;
    private final JervletContext m_jervletContext;
    private final File m_rootDirectory;
    private boolean m_extractWebArchive;

    private WebApplicationContext m_webApplicationContext;

    public WebApplicationContextHolder( String context,
                                  File pathToWebAppFolder,
                                  JervletContext jervletContext,
                                  File rootDirectory,
                                  boolean extractWebArchive )
        throws MalformedURLException
    {
        m_context = context;
        m_webappUrl = pathToWebAppFolder.toURL().toString();
        m_jervletContext = jervletContext;
        m_rootDirectory = rootDirectory;
        m_extractWebArchive = extractWebArchive;
    }

    String getWebappUrl()
    {
        return m_webappUrl;
    }

    String getContext()
    {
        return m_context;
    }

    WebApplicationContext getWebApplicationContext()
    {
        return m_webApplicationContext;
    }

    String getStatus()
    {
        if( null == m_webApplicationContext )
        {
            return "Ready to deploy";
        }
        else if( m_webApplicationContext.isStarted() )
        {
            return "Running";
        }
        else
        {
            return "Stopped";
        }
    }

    WebApplicationContext create() throws IOException
    {
        if( null != m_webApplicationContext )
        {
            throw new IllegalStateException( "context already exists" );
        }

        m_webApplicationContext =
            new JervletWebApplicationContext( m_jervletContext, m_rootDirectory, m_webappUrl );
        m_webApplicationContext.setContextPath( m_context );
        m_webApplicationContext.setExtractWAR( m_extractWebArchive );

        return m_webApplicationContext;
    }

    void start() throws Exception
    {
        if( m_webApplicationContext == null )
        {
            throw new IllegalStateException( "haven't been created");
        }
        else if ( m_webApplicationContext.isStarted() )
        {
            throw new IllegalStateException( "already started" );
        }

        m_webApplicationContext.start();
    }

    void stop() throws InterruptedException
    {
        if( m_webApplicationContext == null || !m_webApplicationContext.isStarted() )
        {
            throw new IllegalStateException( "not started");
        }

        m_webApplicationContext.stop();
    }

    void destroy()
    {
        if( m_webApplicationContext == null )
        {
            throw new IllegalStateException( "already destroyed");
        }
        else if( m_webApplicationContext.isStarted() )
        {
            throw new IllegalStateException( "must stop before destroying ");
        }

        m_webApplicationContext.destroy();
        m_webApplicationContext = null;
    }
}
