/*
 * Copyright (C) The Spice Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */
package org.realityforge.xinvoke.spi.impl.test;

import junit.framework.TestCase;
import org.realityforge.xinvoke.spi.ProxyManager;
import org.realityforge.xinvoke.spi.InterceptorManager;
import org.realityforge.xinvoke.spi.impl.DefaultProxyManager;
import org.apache.avalon.framework.container.ContainerUtil;
import org.apache.avalon.framework.logger.ConsoleLogger;
import org.apache.avalon.framework.service.DefaultServiceManager;
import java.util.HashMap;

/**
 *
 *
 * @author <a href="mailto:peter at www.stocksoftware.com.au">Peter Donald</a>
 * @version $Revision: 1.2 $ $Date: 2003-09-02 04:17:28 $
 */
public class AbstractXInvokeTestCase
    extends TestCase
{
    public AbstractXInvokeTestCase( final String message )
    {
        super( message );
    }

    protected Object createSimpleProxy( final SimpleRunnable target,
                                        final Class[] interfaces,
                                        final ClassLoader classLoader )
        throws Exception
    {
        final ProxyManager proxyManager = createProxyManager();
        final Object proxy =
            createProxy( proxyManager, target, interfaces, classLoader );

        for( int i = 0; i < interfaces.length; i++ )
        {
            final Class intf = interfaces[ i ];
            assertTrue( "proxy instanceof " + intf.getName(),
                        intf.isInstance( proxy ) );
        }
        assertEquals( "proxy interface count",
                      interfaces.length,
                      proxy.getClass().getInterfaces().length );
        return proxy;
    }

    protected Object createProxy( final ProxyManager proxyManager,
                                  final SimpleRunnable target,
                                  final Class[] interfaces,
                                  final ClassLoader classLoader )
        throws Exception
    {
        final HashMap parameters = new HashMap();
        parameters.put( ProxyManager.INTERFACES, interfaces );
        parameters.put( ProxyManager.TARGET, target );
        parameters.put( ProxyManager.CLASSLOADER, classLoader );
        return proxyManager.createProxy( parameters );
    }

    protected DefaultProxyManager createProxyManager()
        throws Exception
    {
        final DefaultProxyManager proxyManager = new DefaultProxyManager();
        ContainerUtil.enableLogging( proxyManager, new ConsoleLogger() );

        final DefaultServiceManager manager = new DefaultServiceManager();
        final InterceptorManager interceptorManager = createInterceptorManager();

        manager.put( InterceptorManager.class.getName(), interceptorManager );
        manager.makeReadOnly();
        ContainerUtil.service( proxyManager, manager );
        return proxyManager;
    }

    protected InterceptorManager createInterceptorManager()
        throws Exception
    {
        return new SimpleTestInterceptorManager();
    }
}
