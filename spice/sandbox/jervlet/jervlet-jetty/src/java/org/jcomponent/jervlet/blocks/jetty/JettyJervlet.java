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
import java.util.HashMap;
import java.util.Set;
import java.util.Collections;
import java.net.UnknownHostException;

import org.jcomponent.jervlet.Jervlet;
import org.jcomponent.jervlet.JervletContext;
import org.jcomponent.jervlet.JervletException;
import org.apache.avalon.framework.CascadingRuntimeException;
import org.apache.avalon.framework.activity.Initializable;
import org.apache.avalon.framework.activity.Startable;
import org.apache.avalon.framework.configuration.Configurable;
import org.apache.avalon.framework.configuration.Configuration;
import org.apache.avalon.framework.configuration.ConfigurationException;
import org.apache.avalon.framework.context.Context;
import org.apache.avalon.framework.context.ContextException;
import org.apache.avalon.framework.context.Contextualizable;
import org.apache.avalon.framework.logger.AbstractLogEnabled;
import org.apache.avalon.framework.service.ServiceException;
import org.apache.avalon.framework.service.ServiceManager;
import org.apache.avalon.framework.service.Serviceable;

import org.mortbay.http.SocketListener;
import org.mortbay.jetty.Server;
import org.mortbay.util.Log;
import org.mortbay.util.MultiException;

/**
 * @phoenix:block
 * @phoenix:service name="org.jcomponent.jervlet.Jervlet"
 * @phoenix:mx-topic name="ServletContainer"
 *
 * Jetty Wrapper.
 *
 *
 * @see <a href="http://jetty.mortbay.com/">Jetty Project Page</a>
 *
 * @author  Paul Hammant
 * @author  Ulrich Mayring
 * @author  Peter Royal
 * @author  Ryan Hoegg
 * @version 1.0
 */
public class JettyJervlet extends AbstractLogEnabled
    implements Jervlet, Startable, Contextualizable, Configurable, Initializable, Serviceable
{
    private Server m_server;

    /** Virtual host to bind the Jetty to.  null implies all hosts are in context. */
    private String m_hostName;

    private HashMap m_webcontexts = new HashMap();
    private int m_port;
    private int m_minThreads;
    private int m_maxThreads;
    private boolean m_extractWebArchive;
    private File m_sarRootDir;
    private JervletContext m_jervletContext;
    private Context m_context;

    /**
     * @param serviceManager
     * @throws ServiceException
     *
     * @phoenix:dependency name="org.jcomponent.jervlet.blocks.jetty.RequestLogger"
     */
    public void service( ServiceManager serviceManager ) throws ServiceException
    {
        m_jervletContext = new JervletContext( m_context, serviceManager, getLogger() );
    }

    /**
     * Contextualize
     * @param context the context
     */
    public void contextualize( final Context context ) throws ContextException
    {
        m_context = context;
        m_sarRootDir = (File)context.get( "app.home" );
    }

    /**
     * Configure
     * @param configuration the configuration
     * @throws ConfigurationException if a problem
     *
     * @phoenix:configuration-schema type="http://relaxng.org/ns/structure/1.0"
     */
    public void configure( final Configuration configuration ) throws ConfigurationException
    {
        m_hostName = configuration.getChild( "hostname" ).getValue( null );
        m_port = configuration.getChild( "port" ).getValueAsInteger( 8080 );
        m_minThreads = configuration.getChild( "minthreads" ).getValueAsInteger( 5 );
        m_maxThreads = configuration.getChild( "maxthreads" ).getValueAsInteger( 250 );
        m_extractWebArchive = configuration.getChild( "extract-war" ).getValueAsBoolean( true );

        if( m_maxThreads < m_minThreads )
        {
            throw new ConfigurationException( "maxthreads must be greater than minthreads" );
        }
    }

    /**
     * Initialize
     * @throws Exception if a problem
     */
    public void initialize() throws Exception
    {
        m_server = createHttpServer();
        m_server.addListener( createSocketListener() );

        PhoenixLogSink phoenixLogSink = new PhoenixLogSink();
        phoenixLogSink.enableLogging( getLogger() );
        Log.instance().add( phoenixLogSink );

        RequestLogger logger = (RequestLogger)
            m_jervletContext.getServiceManager().lookup( RequestLogger.ROLE );
        m_server.setRequestLog( new JettyRequestLogAdapter( logger ) );
    }

    protected Server createHttpServer()
    {
        return new Server();
    }

    private SocketListener createSocketListener() throws UnknownHostException
    {
        SocketListener listener = new SocketListener();

        if( null != m_hostName )
        {
            listener.setHost( m_hostName );
        }

        listener.setPort( m_port );
        listener.setMinThreads( m_minThreads );
        listener.setMaxThreads( m_maxThreads );
        return listener;
    }

    /**
     * Start
     */
    public final void start()
    {
        try
        {
            m_server.start();
        }
        catch( MultiException e )
        {
            throw new CascadingRuntimeException( "Some problem starting Jetty", e );
        }
    }

    /**
     * Stop
     */
    public void stop()
    {
        try
        {
            m_server.stop();
        }
        catch( InterruptedException e )
        {
            throw new CascadingRuntimeException( "Some problem stopping Jetty", e );
        }
    }

    /**
     * Deploy a webapp
     * @param context the contxct for the webapp
     * @param pathToWebAppFolder the path to it
     * @throws JervletException if a problem
     */
    public void deploy( String context, File pathToWebAppFolder ) throws JervletException
    {
        deploy( context, pathToWebAppFolder, m_jervletContext );
    }

    /**
     * Deploy a webapp
     * @param context the contxct for the webapp
     * @param pathToWebAppFolder the path to it
     * @param jervletContext The optional context to apply to servlets (LogEnabled, Serviceable).
     * @throws JervletException if a problem
     */
    public void deploy( String context, File pathToWebAppFolder, JervletContext jervletContext )
        throws JervletException
    {
        if( m_webcontexts.containsKey( context ) )
        {
            throw new JervletException( "Context '" + context + "' has already been deployed" );
        }

        WebApplicationContextHolder holder;

        try
        {
            holder = new WebApplicationContextHolder( context,
                                                      pathToWebAppFolder,
                                                      jervletContext,
                                                      m_sarRootDir,
                                                      m_extractWebArchive );
            m_webcontexts.put( context, holder );
        }
        catch( Exception e )
        {
            final String msg = "Problem deploying web application (" + pathToWebAppFolder
                + ") in Jetty";

            throw new JervletException( msg, e );
        }

        deploy( context );
    }

    /**
     * Get a list of all webapps currently installed with the container
     *
     * @phoenix:mx-attribute
     *
     * @return The list of all contexts installed in the container
     */
    public Set getContextList()
    {
        return Collections.unmodifiableSet( m_webcontexts.keySet() );
    }

    /**
     * Get the status of an installed webapp
     *
     * @phoenix:mx-operation
     *
     * @param context webapp to get status of
     * @return status of webapp
     * @throws JervletException if any problems occur
     */
    public String getStatus( String context ) throws JervletException
    {
        final WebApplicationContextHolder holder = getWebApplicationContextHolder( context );

        return holder.getStatus();
    }

    /**
     * Start a stopped webapp
     *
     * @phoenix:mx-operation
     *
     * @param context webapp to start
     * @throws JervletException if any problems occur
     */
    public void start( String context ) throws JervletException
    {
        final WebApplicationContextHolder holder = getWebApplicationContextHolder( context );

        try
        {
            holder.start();
        }
        catch( Exception e )
        {
            final String msg = "Unable to start '" + context + "' : " + e.getMessage();
            getLogger().error( msg, e );
            throw new JervletException( msg, e );
        }
    }

    /**
     * Stop a running webapp
     *
     * @phoenix:mx-operation
     *
     * @param context webapp to stop
     * @throws JervletException if any problems occur
     */
    public void stop( String context ) throws JervletException
    {
        final WebApplicationContextHolder holder = getWebApplicationContextHolder( context );

        try
        {
            holder.stop();
        }
        catch( Exception e )
        {
            final String msg = "Unable to stop '" + context + "' : " + e.getMessage();
            getLogger().error( msg, e );
            throw new JervletException( msg, e );
        }
    }

    /**
     * Redeploy a webapp. This operation fully reloads the webapp, and is equivalent to
     * doing undeploy() followed by deploy()
     *
     * @phoenix:mx-operation
     *
     * @param context webapp to reload
     * @throws JervletException if any problems occur
     */
    public void redeploy( String context ) throws JervletException
    {
        try
        {
            undeploy( context );
            deploy( context );
        }
        catch( JervletException e )
        {
            getLogger().error( "Exception restarting: " + context, e );

            throw e;
        }
    }

    /**
     * Deploy a webapp
     *
     * @phoenix:mx-operation
     *
     * @param context webapp to deploy
     * @throws JervletException if any problems occur
     */
    public void deploy( String context ) throws JervletException
    {
        final WebApplicationContextHolder holder = getWebApplicationContextHolder( context );

        //Set the context classloader because the current classloader will be that of the
        Thread.currentThread().setContextClassLoader( getClass().getClassLoader() );

        try
        {
            m_server.addContext( m_hostName, holder.create() );

            if( getLogger().isInfoEnabled() )
                getLogger().info( "deploying context=" + context + ", webapp="
                                  + holder.getWebappUrl() + " to host="
                                  + ( m_hostName == null ? "(All Hosts)" : m_hostName ) );

            holder.start();
        }
        catch( Exception e )
        {
            final String msg = "Unable to deploy '" + context + "' : " + e.getMessage();

            getLogger().error( msg, e );
            throw new JervletException( msg, e );
        }
    }

    private WebApplicationContextHolder getWebApplicationContextHolder( String context )
        throws JervletException
    {
        WebApplicationContextHolder holder =
            (WebApplicationContextHolder)m_webcontexts.get( context );

        if( null == holder )
        {
            throw new JervletException( "Unknown context: " + context );
        }

        return holder;
    }

    /**
     * Start a stopped webapp
     *
     * @phoenix:mx-operation
     *
     * @param context webapp to start
     * @throws JervletException if any problems occur
     */
    public void undeploy( String context ) throws JervletException
    {
        final WebApplicationContextHolder holder = getWebApplicationContextHolder( context );

        try
        {
            holder.stop();
        }
        catch( InterruptedException e )
        {
            final String msg = "Problem stopping web application in Jetty";

            getLogger().error( msg, e );

            throw new JervletException( msg, e );
        }

        if( !m_server.removeContext( holder.getWebApplicationContext() ) )
        {
            getLogger().warn( "Potential problems removing context" );
        }

        try
        {
            holder.destroy();
        }
        catch( Exception e )
        {
            final String msg = "Problem destroying web application in Jetty";

            getLogger().error( msg, e );

            throw new JervletException( msg, e );
        }
    }
}



