/*
 * Copyright (C) The Spice Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */
package org.realityforge.xinvoke.spi.impl;

import java.util.ArrayList;
import java.util.Map;
import org.apache.avalon.framework.activity.Disposable;
import org.apache.avalon.framework.activity.Initializable;
import org.apache.avalon.framework.configuration.Configurable;
import org.apache.avalon.framework.configuration.Configuration;
import org.apache.avalon.framework.configuration.ConfigurationException;
import org.apache.avalon.framework.container.ContainerUtil;
import org.apache.avalon.framework.logger.AbstractLogEnabled;
import org.apache.avalon.framework.service.DefaultServiceManager;
import org.realityforge.xinvoke.Interceptor;
import org.realityforge.xinvoke.spi.InterceptorManager;

/**
 * A simple InterceptorManager that just builds the same
 * chain of Interceptors for all objects. The chain is defined
 * in the configuration supplied to component. The configuration format
 * looks like;
 *
 * <interceptors>
 *   <interceptor class="org.realityforge.xinvoke.lib.AuthenticationInterceptor"/>
 *   <interceptor class="org.realityforge.xinvoke.lib.TransactionInterceptor">
 *     <!-- Period over which transaction will timeout -->
 *     <period>300</period>
 *     <!-- policy to use for locking resources -->
 *     <policy>pessimistic</policy>
 *     ...insert more config here...
 *   </interceptor>
 *   <interceptor class="org.realityforge.xinvoke.lib.JavaInvokeInterceptor"/>
 * <interceptors>
 *
 * @author <a href="mailto:peter at www.stocksoftware.com.au">Peter Donald</a>
 * @version $Revision: 1.1 $ $Date: 2003-01-29 00:14:37 $
 */
public final class SimpleInterceptorManager
    extends AbstractLogEnabled
    implements InterceptorManager, Configurable, Initializable, Disposable
{
    /**
     * The interceptor at the head of chain that is managed by this component.
     */
    private Interceptor m_interceptor;

    /**
     * the configuration used to create the interceptor chain.
     */
    private Configuration m_configuration;

    /**
     * A list of interceptors in order that they were added.
     * Only used to shutdown the interceptors
     */
    private final ArrayList m_elements = new ArrayList();

    /**
     * Specify configuration used to build interceptor chain.
     *
     * @param configuration the configuration
     */
    public void configure( final Configuration configuration )
    {
        m_configuration = configuration;
    }

    /**
     * Init InterceptorManager which involves creating
     * the InterceptorChain.
     *
     * @throws Exception if unable to initialize chain
     */
    public void initialize()
        throws Exception
    {
        m_interceptor = createChain( m_configuration );
        m_configuration = null;
    }

    /**
     * Shutdown InterceptorManager which simply involes shutting down
     * all the interceptors in chain.
     */
    public void dispose()
    {
        shutdownChain();
        m_interceptor = null;
    }

    /**
     * @see InterceptorManager#getInterceptor
     */
    public Interceptor getInterceptor( final Map parameters )
        throws Exception
    {
        //Ignore the parameters as we will always
        //return the same interceptor chain
        return m_interceptor;
    }

    /**
     * @see InterceptorManager#releaseInterceptor
     */
    public void releaseInterceptor( final Interceptor interceptor )
        throws Exception
    {
        //Do nothing
    }

    /**
     * Create the InterceptorChain.
     *
     * @param configuration
     * @throws ConfigurationException
     */
    public Interceptor createChain( final Configuration configuration )
        throws Exception
    {
        final Configuration[] children = configuration.getChildren( "interceptor" );
        Interceptor lastInterceptor = null;
        Interceptor firstInterceptor = null;
        for( int i = children.length - 1; i >= 0; i-- )
        {
            final Configuration child = children[ i ];
            final Interceptor interceptor = createInterceptor( child, lastInterceptor );
            m_elements.add( interceptor );

            if( 0 == i )
            {
                firstInterceptor = interceptor;
            }
            lastInterceptor = interceptor;
        }

        final String message =
            "Created a singleton Interceptor that has " + children.length +
            " interceptors in the chain.";
        getLogger().info( message );

        return firstInterceptor;
    }

    /**
     * Shutdown the Interceptor chain by disposing each element.
     */
    private void shutdownChain()
    {
        final Interceptor[] interceptors =
            (Interceptor[])m_elements.toArray( new Interceptor[ m_elements.size() ] );
        m_elements.clear();
        for( int i = interceptors.length - 1; i >= 0; i-- )
        {
            try
            {
                ContainerUtil.shutdown( interceptors[ i ] );
            }
            catch( final Exception e )
            {
                final String message =
                    "Error disposing Interceptor chain element " +
                    i + " due to " + e;
                getLogger().error( message, e );
            }
        }
    }

    /**
     * Create an interceptor in the chain.
     *
     * @param configuration the interceptors configuration
     * @param previous the previous interceptor (if any)
     * @return the newly created interceptor
     */
    private Interceptor createInterceptor( final Configuration configuration,
                                           final Interceptor previous )
        throws Exception
    {
        final String classname = configuration.getAttribute( "class" );
        final Object object = Class.forName( classname ).newInstance();
        if( !( object instanceof Interceptor ) )
        {
            final String message =
                classname + " does not designate an Interceptor";
            throw new Exception( message );
        }
        final Interceptor interceptor = (Interceptor)object;
        ContainerUtil.enableLogging( interceptor, getLogger() );
        ContainerUtil.configure( interceptor, configuration );
        final DefaultServiceManager sm = new DefaultServiceManager();
        sm.put( Interceptor.ROLE, previous );
        sm.makeReadOnly();
        ContainerUtil.service( interceptor, sm );
        ContainerUtil.initialize( interceptor );
        ContainerUtil.start( interceptor );
        return interceptor;
    }
}
