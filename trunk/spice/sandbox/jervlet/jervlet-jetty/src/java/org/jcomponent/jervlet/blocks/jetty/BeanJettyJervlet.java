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

import org.jcomponent.jervlet.*;
import org.mortbay.jetty.Server;

import java.io.File;
import java.net.UnknownHostException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Set;

/**
 *
 * Jetty Wrapper for SpringFramework etc
 * Type-2 IoC
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
public class BeanJettyJervlet extends AbstractJettyJervlet
    implements Jervlet
{

    private JervletMonitor monitor;
    private File appRootDir;
    private JervletContext jervletContext;

    private Server m_server;

    private HashMap m_webcontexts = new HashMap();
    private RequestLogger requestLogger;

    public void setJervletConfig(JervletConfig config) {
        super.config = config;
    }

    public void setRequestLogger(RequestLogger requestLogger) {
        this.requestLogger = requestLogger;
    }

    public void setAppRootDir(File appRootDir) {
        this.appRootDir = appRootDir;
    }

    public void setJervletMonitor(JervletMonitor jervletMonitor) {
        this.monitor = jervletMonitor;
    }

    public void initialize() throws UnknownHostException {

        this.jervletContext = new SimpleJervletContext();

        m_server = createHttpServer();
        m_server.addListener( createSocketListener() );

        //TODO
        //PhoenixLogSink phoenixLogSink = new PhoenixLogSink();
        //phoenixLogSink.enableLogging( getLogger() );
        //Log.instance().add( phoenixLogSink );

        m_server.setRequestLog( new JettyRequestLogAdapter( requestLogger ) );
    }

    public void dispose() {

    }

    /**
     * Deploy a webapp
     * @param context the contxct for the webapp
     * @param pathToWebAppFolder the path to it
     * @throws JervletException if a problem
     */
    public void deploy( String context, File pathToWebAppFolder ) throws JervletException
    {
        deploy( context, pathToWebAppFolder, jervletContext );
    }

    /**
     * Deploy a webapp
     * @param context the contxct for the webapp
     * @param pathToWebAppFolder the path to it
     * @param jervletContext The optional jervletContext to use to resolve dependancies for servlets
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
                                                      appRootDir,
                                                      config.getExtractWarFile() );
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

            monitor.startException(this.getClass(),context,e);
        }
    }

    /**
     * Stop a running webapp
     *
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
            monitor.stopException(this.getClass(), context,e);
        }
    }

    /**
     * Redeploy a webapp. This operation fully reloads the webapp, and is equivalent to
     * doing undeploy() followed by deploy()
     *     *
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
            monitor.redeployException(this.getClass(), context,e);
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
            m_server.addContext( config.getHostName(), holder.create() );

            monitor.deployingContext(this.getClass(), context, holder.getWebappUrl(), config.getHostName());

            holder.start();
        }
        catch( Exception e )
        {

            monitor.deployingException(this.getClass(), context, holder.getWebappUrl(), config.getHostName(), e);

        }
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

            monitor.undeployException(this.getClass(), context, e);

        }

        if( !m_server.removeContext( holder.getWebApplicationContext() ) )
        {
            monitor.undeployWarning(this.getClass(), context, "Context Not Removed");
        }

        try
        {
            holder.destroy();
        }
        catch( Exception e )
        {
            monitor.undeployException(this.getClass(), context, e);

        }
    }
}



