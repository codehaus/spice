/*
 * Copyright (C) The Spice Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */
package org.jcomponent.threadpool.impl;

import org.apache.avalon.framework.activity.Disposable;
import org.apache.avalon.framework.activity.Initializable;
import org.apache.avalon.framework.configuration.Configurable;
import org.apache.avalon.framework.configuration.Configuration;
import org.apache.avalon.framework.configuration.ConfigurationException;
import org.apache.avalon.framework.logger.LogEnabled;
import org.apache.avalon.framework.logger.Logger;
import org.apache.commons.pool.PoolableObjectFactory;
import org.jcomponent.threadpool.ThreadPool;

/**
 * The CommonsThreadPool is a component that provides a basic
 * mechanism for pooling threads. A sample configuration for this
 * component is;
 * <pre>
 * &lt;config&gt;
 *   &lt;name&gt;MyThreadPool&lt;/name&gt; &lt;!-- base name of all threads --&gt;
 *   &lt;priority&gt;5&lt;/priority&gt; &lt;!-- set to default priority --&gt;
 *   &lt;is-daemon&gt;false&lt;/is-daemon&gt; &lt;!-- are threads daemon threads? --&gt;
 *   &lt;resource-limiting&gt;false&lt;/resource-limiting&gt; &lt;!-- will pool block when max threads reached? --&gt;
 *   &lt;max-threads&gt;10&lt;/max-threads&gt;
 *   &lt;max-idle&gt;5&lt;/max-idle&gt; &lt;!-- maximum number of idle threads --&gt;
 * &lt;/config&gt;
 * </pre>
 *
 * @author <a href="mailto:peter at realityforge.org">Peter Donald</a>
 * @version $Revision: 1.1 $ $Date: 2003-08-27 21:38:14 $
 * @phoenix.service type="ThreadPool"
 */
public class AvalonAdaptedPicoCommonsThreadPool
    extends AbstractThreadPool
    implements ThreadPool, LogEnabled, Configurable, Initializable, Disposable, PoolableObjectFactory
{

    private PicoCommonsThreadPool delegate;
    private String name;
    private int priority;
    private boolean isDaemon;
    private boolean resourceLimited;
    private int maxActive;
    private int maxIdle;

    /**
     * The logger for component.
     */
    private Logger m_logger;

    /**
     * Set the logger for component.
     *
     * @param logger the logger for component.
     */
    public void enableLogging( final Logger logger )
    {
        m_logger = logger;
    }

    /**
     * Configure the pool. See class javadocs for example.
     *
     * @param configuration the configuration object
     * @phoenix.configuration
     *    type="http://relaxng.org/ns/structure/1.0"
     *    location="CommonsThreadPool-schema.xml"
     */
    public void configure( final Configuration configuration )
        throws ConfigurationException
    {
        name = configuration.getChild( "name" ).getValue();
        priority = configuration.getChild( "priority" ).getValueAsInteger( Thread.NORM_PRIORITY );
        isDaemon = configuration.getChild( "is-daemon" ).getValueAsBoolean( false );
        resourceLimited = configuration.getChild( "resource-limiting" ).getValueAsBoolean( false );
        maxActive = configuration.getChild( "max-threads" ).getValueAsInteger( 10 );
        maxIdle = configuration.getChild( "max-idle" ). getValueAsInteger( maxActive / 2 );
    }

    /**
     * Initialize the underlying pool.
     *
     * @throws Exception if error occurs creating pool
     */
    public void initialize()
        throws Exception
    {
        AvalonThreadPoolMonitor monitor = new AvalonThreadPoolMonitor();
        monitor.enableLogging(m_logger);
        delegate = new PicoCommonsThreadPool.WithMonitorAndConfig(monitor,
                                                                       name, priority, isDaemon, resourceLimited,
                                                                       maxActive, maxIdle);
    }

    /**
     * Shutdown all threads associated with pool.
     */
    public void dispose()
    {
        delegate.dispose();
        delegate = null;
    }

    /**
     * Retrieve a worker thread from pool.
     *
     * @return the worker thread retrieved from pool
     */
    protected WorkerThread getWorker()
    {
        return delegate.getWorker();
    }

    /**
     * Return the WorkerThread to the pool.
     *
     * @param worker the worker thread to put back in pool
     */
    protected void releaseWorker( final WorkerThread worker )
    {
        delegate.releaseWorker( worker );
    }

    /**
     * Override creation of worker to add logging.
     *
     * @return the new worker thread
     */
    protected WorkerThread createWorker()
    {
        return delegate.createWorker();
    }

    /**
     * Overide destruction of worker to add logging.
     *
     * @param worker the worker thread
     */
    protected void destroyWorker( final WorkerThread worker )
    {
        delegate.destroyWorker( worker );
    }

    /**
     * Create a new worker. (Part of {@link PoolableObjectFactory} interface)
     *
     * @return the new worker
     */
    public Object makeObject()
    {
        return createWorker();
    }

    /**
     * Destroy a worker. (Part of {@link PoolableObjectFactory} interface)
     *
     * @param worker the new worker
     */
    public void destroyObject( final Object worker )
    {
        destroyWorker( (WorkerThread)worker );
    }

    /**
     * validate a worker. (Part of {@link PoolableObjectFactory} interface)
     *
     * @return true (no validation occurs)
     */
    public boolean validateObject( final Object worker )
    {
        return delegate.validateObject( worker );
    }

    /**
     * activate a worker. (Part of {@link PoolableObjectFactory} interface)
     * No-op.
     */
    public void activateObject( final Object worker )
    {
        delegate.activateObject( worker );
    }

    /**
     * passivate a worker. (Part of {@link PoolableObjectFactory} interface)
     * No-op.
     */
    public void passivateObject( final Object worker )
    {
        delegate.passivateObject( worker );
    }
}
