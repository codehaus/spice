/*
 * Copyright (C) The Spice Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */
package org.realityforge.xinvoke.spi.impl;

import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import org.apache.avalon.framework.service.ServiceException;
import org.apache.avalon.framework.service.ServiceManager;
import org.apache.avalon.framework.service.Serviceable;
import org.realityforge.xinvoke.Interceptor;
import org.realityforge.xinvoke.spi.InterceptorManager;
import org.realityforge.xinvoke.spi.ProxyManager;

/**
 * A simple ProxyManager that creates a proxy with an
 * Interceptor attached. Uses JDK1.3 Proxy infrastructure.
 * This ProxyManager implementation will proxy an object with
 * interfaces as specified in the parameter or by deriving
 * all the interfaces that the target object implements.
 * The proxy class will be created in a new ClassLoader as
 * specified by ClassLoader parameter or by deriving the ClassLoader
 * via which the target object was created.
 *
 * @author <a href="mailto:peter at realityforge.org">Peter Donald</a>
 * @version $Revision: 1.1 $ $Date: 2003-01-29 00:14:37 $
 */
public class DefaultProxyManager
    implements ProxyManager, Serviceable
{
    /**
     * The set of proxys created by this ProxyManager.
     */
    private final Map m_proxys = new HashMap();

    /**
     * The {@link InterceptorManager} used to create
     * {@link Interceptor} objects for proxies.
     */
    private InterceptorManager m_interceptorManager;

    /**
     * @avalon.dependency type="InterceptorManager"
     */
    public void service( final ServiceManager manager )
        throws ServiceException
    {
        m_interceptorManager =
            (InterceptorManager)manager.lookup( InterceptorManager.ROLE );
    }

    /**
     * @see ProxyManager#createProxy
     */
    public synchronized Object createProxy( final Map parameters )
        throws Exception
    {
        final ClassLoader classLoader = getClassLoader( parameters );
        final Class[] interfaces = getInterfaces( parameters );

        //Retrieve the Interceptor from the InterceptorManager
        final Interceptor interceptor =
            m_interceptorManager.getInterceptor( parameters );

        //Create the handler that the proxy delegates to
        final RootInvocationHandler handler =
            new RootInvocationHandler( interceptor, parameters );

        //Create the actual proxy
        final Object proxy =
            Proxy.newProxyInstance( classLoader, interfaces, handler );

        m_proxys.put( getKey( proxy ), proxy );
        return proxy;
    }

    /**
     * @see ProxyManager#isManagedProxy
     */
    public synchronized boolean isManagedProxy( final Object object )
    {
        return m_proxys.containsKey( getKey( object ) );
    }

    /**
     * @see ProxyManager#destroyProxy
     */
    public synchronized void destroyProxy( final Object object )
        throws Exception
    {
        if( !isManagedProxy( object ) )
        {
            final String message = "object not a managed proxy";
            throw new IllegalArgumentException( message );
        }

        m_proxys.remove( getKey( object ) );
        final RootInvocationHandler handler =
            (RootInvocationHandler)Proxy.getInvocationHandler( object );
        handler.dispose();
    }

    /**
     * Generate key used to store proxy in map.
     * This needs to be the system identity hash code because
     * otherwise you would need to invoke hashCode() on proxy
     * to determine hashCode which may result in calls to invalid
     * proxy.
     *
     * @param proxy the proxy object
     * @return the key for proxy
     */
    private Integer getKey( final Object proxy )
    {
        return new Integer( System.identityHashCode( proxy ) );
    }

    /**
     * Return the parent ClassLoader of Proxys ClassLoader.
     * It will use the ClassLoader specified if present else it
     * will use reflection to get ClassLoader of target object.
     * If classloader and target object are not specified then an
     * exception is thrown.
     *
     * @param parameters the parameters
     * @return thje parent classloader of Proxys ClassLoader
     * @throws Exception if unable to get classloader
     */
    private ClassLoader getClassLoader( final Map parameters )
        throws Exception
    {
        final ClassLoader classLoader =
            (ClassLoader)parameters.get( ProxyManager.CLASSLOADER );
        if( null != classLoader )
        {
            return classLoader;
        }
        final Object target = parameters.get( ProxyManager.TARGET );
        if( null != target )
        {
            return target.getClass().getClassLoader();
        }

        final String message =
            "Unable to determine the ClassLoader to proxy from as " +
            "no classloader or target object was specified.";
        throw new Exception( message );
    }

    /**
     * Return the set of interfaces to proxy for specified
     * parameters. By default this method will return any interfaces
     * that the user specified. If the user did not specify the
     * interfaces to proxy then this method will attempt to auto-discover
     * the interfaces via reflection on the target object. If no target
     * object was specified then the method will throw an exception.
     *
     * @param parameters the parameters
     * @return the list of interfaces to proxy
     * @throws Exception if unable to determine list of interfaces to proxy
     */
    private Class[] getInterfaces( final Map parameters )
        throws Exception
    {
        final Class[] interfaces = (Class[])parameters.get( ProxyManager.INTERFACES );
        if( null != interfaces )
        {
            return interfaces;
        }

        final Object target = parameters.get( ProxyManager.TARGET );
        if( null != target )
        {
            final ArrayList list = new ArrayList();
            collectInterfaces( target.getClass(), list );
            return (Class[])list.toArray( new Class[ list.size() ] );
        }

        final String message =
            "Unable to determine the interfaces to proxy as " +
            "no interfaces or target object was specified.";
        throw new Exception( message );
    }

    /**
     * Collect all the interfaces defined by class
     * and all it's superclasses.
     *
     * @param clazz the class
     * @param list the list to add interfaces to
     */
    private void collectInterfaces( final Class clazz,
                                    final ArrayList list )
    {
        if( null == clazz )
        {
            return;
        }

        final Class[] interfaces = clazz.getInterfaces();
        for( int i = 0; i < interfaces.length; i++ )
        {
            list.add( interfaces[ i ] );
        }

        collectInterfaces( clazz.getSuperclass(), list );
    }
}
