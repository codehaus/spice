/*
 * Copyright (C) The Spice Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */
package org.jcomponent.netserve.connection.impl;

import org.jcomponent.threadpool.ThreadPool;
import org.jcomponent.netserve.sockets.SocketAcceptorManager;
import org.jcontainer.dna.Active;
import org.jcontainer.dna.Composable;
import org.jcontainer.dna.Configurable;
import org.jcontainer.dna.Configuration;
import org.jcontainer.dna.ConfigurationException;
import org.jcontainer.dna.LogEnabled;
import org.jcontainer.dna.Logger;
import org.jcontainer.dna.MissingResourceException;
import org.jcontainer.dna.ResourceLocator;
import org.jcontainer.dna.impl.ContainerUtil;

/**
 * An implementation of ConnectionManager which honours the
 * DNA framework interfaces.
 *
 * <p>A sample configuration for the component is below. <tt>shutdownTimeout</tt>
 * indicates how long we should wait to see if incoming connections will
 * shutdown gracefully when asked. If they dont shutdown gracefully and
 * <tt>forceShutdown</tt> is true then the connection will be forced
 * to be shutdown if the user asked for connection to be "tearedDown".</p>
 * <pre>
 *  &lt;forceShutdown&gt;true&lt;/forceShutdown&gt; &lt;!-- forcefully shutdown connections
 *                                           if they dont shutdown gracefully --&gt;
 *  &lt;shutdownTimeout&gt;200&lt;/shutdownTimeout&gt; &lt;!-- wait 200ms for connections to gracefully
 *                                              shutdown --&gt;
 * </pre>
 *
 * @author <a href="mailto:peter at realityforge.org">Peter Donald</a>
 * @author <a href="mailto:mauro.talevi at aquilonia.org">Mauro Talevi</a>
 * @version $Revision: 1.3 $ $Date: 2003-10-14 04:23:58 $
 * @dna.component
 * @dna.service type="org.jcomponent.netserve.connection.ConnectionManager"
 */
public class DNAConnectionManager
    extends AbstractConnectionManager
    implements LogEnabled, Configurable, Composable, Active
{
    /** The DNA Logger */
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
     * @dna.dependency type="ThreadPool" optional="true"
     * @dna.dependency type="SocketAcceptorManager"
     */
    public void compose( ResourceLocator locator )
        throws MissingResourceException
    {
        if( locator.contains( ThreadPool.class.getName() ) )
        {
            final ThreadPool threadPool =
                (ThreadPool)locator.lookup( ThreadPool.class.getName() );
            setDefaultThreadPool( threadPool );
        }
        final SocketAcceptorManager acceptorManager =
            (SocketAcceptorManager)locator.lookup( SocketAcceptorManager.class.getName() );
        setAcceptorManager( acceptorManager );
    }

    /**
     * Configure the ConnectionManager.
     *
     * @param configuration the configuration
     * @throws ConfigurationException if error reading configuration
     * @dna.configuration type="http://relaxng.org/ns/structure/1.0"
     *    location="ConnectionManager-schema.xml"
     */
    public void configure( final Configuration configuration )
        throws ConfigurationException
    {
        setForceShutdown( configuration.getChild( "forceShutdown" ).getValueAsBoolean( false ) );
        setShutdownTimeout( configuration.getChild( "shutdownTimeout" ).getValueAsInteger( 0 ) );
    }

    /**
     * Initialize Connection Manager.
     * Essentially involves specifying a correct monitor.
     *
     * @throws Exception if unable to initialize ConnectionManager
     */
    public void initialize()
        throws Exception
    {
        final DNAConnectionMonitor monitor = new DNAConnectionMonitor();
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
