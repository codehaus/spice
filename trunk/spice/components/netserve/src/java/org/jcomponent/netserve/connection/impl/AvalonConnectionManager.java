/*
 * Copyright (C) The Spice Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */
package org.jcomponent.netserve.connection.impl;

import org.apache.avalon.framework.activity.Disposable;
import org.apache.avalon.framework.activity.Initializable;
import org.apache.avalon.framework.configuration.Configurable;
import org.apache.avalon.framework.configuration.Configuration;
import org.apache.avalon.framework.configuration.ConfigurationException;
import org.apache.avalon.framework.container.ContainerUtil;
import org.apache.avalon.framework.logger.LogEnabled;
import org.apache.avalon.framework.logger.Logger;
import org.apache.avalon.framework.service.ServiceException;
import org.apache.avalon.framework.service.ServiceManager;
import org.apache.avalon.framework.service.Serviceable;
import org.jcomponent.netserve.connection.ConnectionManager;
import org.jcomponent.threadpool.ThreadPool;

/**
 * This component is used to manage serverside network acceptors.
 * To establish a connection the service is provided with a
 * ServerSocket and a ConnectionHandlerManager. The service will start
 * accepting connections to ServerSocket and then pass the accepted socket
 * to ConnectionHandler instances to handle the connection.
 *
 * <p>A sample configuration for the component is below. Note that on some OS/JVM
 * combinations <tt>soTimeout</tt> must be set to non-0 value or else the ServerSocket will
 * never get out of accept() system call and we wont be able to shutdown the server
 * socket properly. However it can introduce performance problems if constantly
 * timing out. <tt>shutdownTimeout</tt> indicates how long we should wait to see if
 * incoming connections will shutdown gracefully when asked. If they dont shutdown
 * gracefully and <tt>forceShutdown</tt> is true then the connection will be forced
 * to be shutdown if the user asked for connection to be "tearedDown".</p>
 * <pre>
 *  &lt;soTimeout&gt;500&lt;/soTimeout&gt; &lt;!-- 500 ms timeouts on Server Sockets --&gt;
 *  &lt;forceShutdown&gt;true&lt;/forceShutdown&gt; &lt;!-- forcefully shutdown connections
 *                                           if they dont shutdown gracefully --&gt;
 *  &lt;shutdownTimeout&gt;200&lt;/shutdownTimeout&gt; &lt;!-- wait 200ms for connections to gracefully
 *                                              shutdown --&gt;
 * </pre>
 *
 * @author <a href="mailto:peter at realityforge.org">Peter Donald</a>
 * @version $Revision: 1.4 $ $Date: 2003-08-31 02:48:52 $
 * @phoenix.component
 * @phoenix.service type="ConnectionManager"
 */
public class AvalonConnectionManager
    extends AbstractConnectionManager
    implements ConnectionManager, Serviceable, Configurable, Initializable, Disposable, LogEnabled
{
    /** The Avalon Logger */
    private Logger m_logger;

    /**
     * Enable logging
     *
     * @param logger the Logger
     */
    public void enableLogging( final Logger logger )
    {
        m_logger = logger;
    }

    /**
     * Get ThreadPool service if present.
     *
     * @param manager the manager to retrieve services from
     * @throws ServiceException if unable to aquire ThreadPool
     * @phoenix.dependency type="ThreadPool" optional="true"
     */
    public void service( final ServiceManager manager )
        throws ServiceException
    {
        if( manager.hasService( ThreadPool.ROLE ) )
        {
            setDefaultThreadPool( (ThreadPool)manager.lookup( ThreadPool.ROLE ) );
        }
    }

    /**
     * Configure the ConnectionManager.
     *
     * @param configuration the configuration
     * @throws ConfigurationException if error reading configuration
     * @phoenix.configuration type="http://relaxng.org/ns/structure/1.0"
     *    location="DefaultConnectionManager-schema.xml"
     */
    public void configure( final Configuration configuration )
        throws ConfigurationException
    {
        setSoTimeout( configuration.getChild( "soTimeout" ).getValueAsInteger( 1000 ) );
        setForceShutdown( configuration.getChild( "forceShutdown" ).getValueAsBoolean( false ) );
        setShutdownTimeout( configuration.getChild( "shutdownTimeout" ).getValueAsInteger( 0 ) );
    }

    /**
     * Initialize Connection Manager.
     * Essentially involves specifying a correct monitor.
     *
     * @throws Exception if unable to initialize COnnectionManager
     */
    public void initialize()
        throws Exception
    {
        final AvalonConnectionMonitor monitor = new AvalonConnectionMonitor();
        ContainerUtil.enableLogging( monitor, m_logger );
        setMonitor( monitor );
    }

    /**
     * Dispose the ConnectionManager which involves shutting down all
     * the connected acceptors.
     */
    public void dispose()
    {
        shutdownAcceptors();
    }
}
