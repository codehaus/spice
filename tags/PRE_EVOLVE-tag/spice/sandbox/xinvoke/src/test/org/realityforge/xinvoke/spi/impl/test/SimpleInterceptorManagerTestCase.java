/*
 * Copyright (C) The Spice Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */
package org.realityforge.xinvoke.spi.impl.test;

import org.apache.avalon.framework.configuration.Configuration;
import org.apache.avalon.framework.configuration.DefaultConfiguration;
import org.apache.avalon.framework.container.ContainerUtil;
import org.apache.avalon.framework.logger.ConsoleLogger;
import org.realityforge.xinvoke.lib.GlobalJavaInvokeInterceptor;
import org.realityforge.xinvoke.lib.PassThroughInterceptor;
import org.realityforge.xinvoke.spi.InterceptorManager;
import org.realityforge.xinvoke.spi.impl.SimpleInterceptorManager;

/**
 * @author <a href="mailto:peter at www.stocksoftware.com.au">Peter Donald</a>
 * @version $Revision: 1.1 $ $Date: 2003-04-16 10:47:03 $
 */
public class SimpleInterceptorManagerTestCase
    extends AbstractXInvokeTestCase
{
    public SimpleInterceptorManagerTestCase( final String name )
    {
        super( name );
    }

    public void testSimpleInvoke()
        throws Exception
    {
        final SimpleRunnable target = new SimpleRunnable();
        final Class[] interfaces = new Class[]{XInvokeTestUtil.RUN_CLASS};
        final ClassLoader classLoader = getClass().getClassLoader();

        final Object proxy =
            createSimpleProxy( target, interfaces, classLoader );

        final Runnable runnable = (Runnable)proxy;
        runnable.run();
        assertEquals( "run count", 1, target.getRunCount() );
        runnable.run();
        assertEquals( "run count", 2, target.getRunCount() );
    }

    public void testDisposeInterceptorManager()
        throws Exception
    {
        final InterceptorManager interceptorManager = createInterceptorManager();
        ContainerUtil.dispose( interceptorManager );
    }

    protected InterceptorManager createInterceptorManager()
        throws Exception
    {
        final SimpleInterceptorManager interceptorManager = new SimpleInterceptorManager();
        ContainerUtil.enableLogging( interceptorManager, new ConsoleLogger() );
        ContainerUtil.configure( interceptorManager, getConfiguration() );
        ContainerUtil.initialize( interceptorManager );
        ContainerUtil.start( interceptorManager );
        return super.createInterceptorManager();
    }

    private Configuration getConfiguration()
    {
        final DefaultConfiguration passThruInterceptor = new DefaultConfiguration( "interceptor", "" );
        passThruInterceptor.setAttribute( "class", PassThroughInterceptor.class.getName() );

        final DefaultConfiguration invokeInterceptor = new DefaultConfiguration( "interceptor", "" );
        invokeInterceptor.setAttribute( "class", GlobalJavaInvokeInterceptor.class.getName() );

        final DefaultConfiguration configuration = new DefaultConfiguration( "interceptors", "" );
        configuration.addChild( passThruInterceptor );
        configuration.addChild( invokeInterceptor );
        return configuration;
    }
}
