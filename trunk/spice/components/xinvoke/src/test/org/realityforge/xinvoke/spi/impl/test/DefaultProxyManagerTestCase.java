/*
 * Copyright (C) The Spice Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */
package org.realityforge.xinvoke.spi.impl.test;

import org.realityforge.xinvoke.spi.ProxyManager;
import org.realityforge.xinvoke.spi.impl.DefaultProxyManager;

/**
 *
 * @author <a href="mailto:peter at realityforge.org">Peter Donald</a>
 * @version $Revision: 1.1 $ $Date: 2003-01-29 00:14:33 $
 */
public class DefaultProxyManagerTestCase

    extends AbstractXInvokeTestCase
{
    public DefaultProxyManagerTestCase( final String name )
    {
        super( name );
    }

    public void testCreateProxy()
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

    public void testDestroyProxy()
        throws Exception
    {
        final SimpleRunnable target = new SimpleRunnable();
        final Class[] interfaces = new Class[]{XInvokeTestUtil.RUN_CLASS};
        final ClassLoader classLoader = getClass().getClassLoader();

        final ProxyManager proxyManager = createProxyManager();
        final Object proxy =
            createProxy( proxyManager, target, interfaces, classLoader );

        final Runnable runnable = (Runnable)proxy;
        runnable.run();
        assertEquals( "run count", 1, target.getRunCount() );
        runnable.run();
        assertEquals( "run count", 2, target.getRunCount() );
        proxyManager.destroyProxy( proxy );
        assertTrue( "!isProxy", !proxyManager.isManagedProxy( proxy ) );
    }

    public void testDestroyProxyThenUse()
        throws Exception
    {
        final SimpleRunnable target = new SimpleRunnable();
        final Class[] interfaces = new Class[]{XInvokeTestUtil.RUN_CLASS};
        final ClassLoader classLoader = getClass().getClassLoader();

        final ProxyManager proxyManager = createProxyManager();
        final Object proxy =
            createProxy( proxyManager, target, interfaces, classLoader );

        final Runnable runnable = (Runnable)proxy;
        runnable.run();
        assertEquals( "run count", 1, target.getRunCount() );
        runnable.run();
        assertEquals( "run count", 2, target.getRunCount() );
        proxyManager.destroyProxy( proxy );
        try
        {
            runnable.run();
        }
        catch( final Throwable t )
        {
            return;
        }
        final String message =
            "Expected Runnable to fail because proxy has been shutdown";
        fail( message );
    }

    public void testDestroyProxyTwice()
        throws Exception
    {
        final SimpleRunnable target = new SimpleRunnable();
        final Class[] interfaces = new Class[]{XInvokeTestUtil.RUN_CLASS};
        final ClassLoader classLoader = getClass().getClassLoader();

        final ProxyManager proxyManager = createProxyManager();
        final Object proxy =
            createProxy( proxyManager, target, interfaces, classLoader );

        final Runnable runnable = (Runnable)proxy;
        runnable.run();
        assertEquals( "run count", 1, target.getRunCount() );
        runnable.run();
        assertEquals( "run count", 2, target.getRunCount() );
        proxyManager.destroyProxy( proxy );
        try
        {
            proxyManager.destroyProxy( proxy );
        }
        catch( final Throwable t )
        {
            return;
        }
        final String message =
            "Expected destroyProxy for second time to fail";
        fail( message );
    }

    public void testCreateProxySansClassLoader()
        throws Exception
    {
        final SimpleRunnable target = new SimpleRunnable();
        final Class[] interfaces = new Class[]{XInvokeTestUtil.RUN_CLASS};

        final Object proxy =
            createSimpleProxy( target, interfaces, null );

        final Runnable runnable = (Runnable)proxy;
        runnable.run();
        assertEquals( "run count", 1, target.getRunCount() );
        runnable.run();
        assertEquals( "run count", 2, target.getRunCount() );
    }

    public void testCreateProxySansInterfaces()
        throws Exception
    {
        final SimpleRunnable target = new SimpleRunnable();
        final Class[] interfaces = new Class[]{XInvokeTestUtil.RUN_CLASS};
        final ClassLoader classLoader = getClass().getClassLoader();

        final ProxyManager proxyManager = createProxyManager();
        final Object proxy =
            createProxy( proxyManager, target, null, classLoader );

        for( int i = 0; i < interfaces.length; i++ )
        {
            final Class intf = interfaces[ i ];
            assertTrue( "proxy instanceof " + intf.getName(),
                        intf.isInstance( proxy ) );
        }
        assertEquals( "proxy interface count",
                      interfaces.length,
                      proxy.getClass().getInterfaces().length );

        final Runnable runnable = (Runnable)proxy;
        runnable.run();
        assertEquals( "run count", 1, target.getRunCount() );
        runnable.run();
        assertEquals( "run count", 2, target.getRunCount() );
    }

    public void testCreateProxySansTarget()
        throws Exception
    {
        final Class[] interfaces = new Class[]{XInvokeTestUtil.RUN_CLASS};
        final ClassLoader classLoader = getClass().getClassLoader();

        final ProxyManager proxyManager = createProxyManager();
        final Object proxy =
            createProxy( proxyManager, null, interfaces, classLoader );

        for( int i = 0; i < interfaces.length; i++ )
        {
            final Class intf = interfaces[ i ];
            assertTrue( "proxy instanceof " + intf.getName(),
                        intf.isInstance( proxy ) );
        }
        assertEquals( "proxy interface count",
                      interfaces.length,
                      proxy.getClass().getInterfaces().length );

        final Runnable runnable = (Runnable)proxy;
        runnable.run();
    }

    public void testNoCreateProxySansClassLoader()
        throws Exception
    {
        final Class[] interfaces = new Class[]{XInvokeTestUtil.RUN_CLASS};
        final ProxyManager proxyManager = createProxyManager();
        try
        {
            createProxy( proxyManager, null, interfaces, null );
        }
        catch( Exception e )
        {
            return;
        }

        fail( "Should not be able to create a proxy " +
              "without a target and without classloader" );
    }

    public void testNoCreateProxySansInterfaces()
        throws Exception
    {
        final ClassLoader classLoader = getClass().getClassLoader();
        final ProxyManager proxyManager = createProxyManager();
        try
        {
            createProxy( proxyManager, null, null, classLoader );
        }
        catch( Exception e )
        {
            return;
        }

        fail( "Should not be able to create a proxy " +
              "without a target and without interfaces" );
    }

    public void testCreateProxyWithOnlyTarget()
        throws Exception
    {
        final SimpleRunnable target = new SimpleRunnable();
        final Class[] interfaces = new Class[]{XInvokeTestUtil.RUN_CLASS};

        final ProxyManager proxyManager = createProxyManager();
        final Object proxy =
            createProxy( proxyManager, target, null, null );

        for( int i = 0; i < interfaces.length; i++ )
        {
            final Class intf = interfaces[ i ];
            assertTrue( "proxy instanceof " + intf.getName(),
                        intf.isInstance( proxy ) );
        }
        assertEquals( "proxy interface count",
                      interfaces.length,
                      proxy.getClass().getInterfaces().length );

        final Runnable runnable = (Runnable)proxy;
        runnable.run();
        assertEquals( "run count", 1, target.getRunCount() );
        runnable.run();
        assertEquals( "run count", 2, target.getRunCount() );
    }

    public void testIsProxy()
        throws Exception
    {
        final SimpleRunnable target = new SimpleRunnable();
        final Class[] interfaces = new Class[]{XInvokeTestUtil.RUN_CLASS};
        final ClassLoader classLoader = getClass().getClassLoader();

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

        assertTrue( "proxyManager.isManagedProxy",
                    proxyManager.isManagedProxy( proxy ) );
    }

    public void testIsProxyForForeignProxys()
        throws Exception
    {
        final SimpleRunnable target = new SimpleRunnable();
        final Class[] interfaces = new Class[]{XInvokeTestUtil.RUN_CLASS};
        final ClassLoader classLoader = getClass().getClassLoader();

        final Object proxy = createSimpleProxy( target, interfaces, classLoader );

        final DefaultProxyManager proxyManager = createProxyManager();
        final boolean managedProxy = proxyManager.isManagedProxy( proxy );
        assertTrue( "isManagedProxy should return false for " +
                    "proxys created by different managers",
                    !managedProxy );
    }

    public void testNotIsManagedProxy()
        throws Exception
    {
        final DefaultProxyManager proxyManager = createProxyManager();
        final boolean managedProxy = proxyManager.isManagedProxy( new Object() );
        assertTrue( "isManagedProxy should return false for non proxys",
                    !managedProxy );
    }
}
