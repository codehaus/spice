/*
 * Copyright (C) The Spice Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */
package org.realityforge.xinvoke.spi.impl;

import java.awt.event.ActionListener;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.security.PrivilegedAction;
import java.util.HashMap;
import junit.framework.TestCase;
import org.realityforge.xinvoke.Invocation;
import org.realityforge.xinvoke.spi.impl.test.ExceptionTestInterceptor;
import org.realityforge.xinvoke.spi.impl.test.ReturnValueTestInterceptor;
import org.realityforge.xinvoke.spi.impl.test.SimpleTestInterceptor;
import org.realityforge.xinvoke.spi.impl.test.XInvokeTestUtil;

/**
 * A Simple test case for invocation handler.
 *
 * @author <a href="mailto:peter at realityforge.org">Peter Donald</a>
 * @version $Revision: 1.1 $ $Date: 2003-01-29 00:14:37 $
 */
public class RootInvocationHandlerTestCase
    extends TestCase
{
    public RootInvocationHandlerTestCase( final String name )
    {
        super( name );
    }

    public void testCtorNullInterceptor()
    {
        try
        {
            new RootInvocationHandler( null, new HashMap() );
        }
        catch( Exception e )
        {
            return;
        }
        fail( "Expected to fail when creating invocation " +
              "Handler with null interceptor" );
    }

    public void testCtorNullContext()
    {
        try
        {
            new RootInvocationHandler( new SimpleTestInterceptor(), null );
        }
        catch( Exception e )
        {
            return;
        }
        fail( "Expected to fail when creating invocation " +
              "Handler with null context" );
    }

    public void testSimpleInvoke()
        throws Throwable
    {
        final SimpleTestInterceptor interceptor = new SimpleTestInterceptor();
        final HashMap context = new HashMap();
        final RootInvocationHandler handler =
            new RootInvocationHandler( interceptor, context );
        final Method method = Runnable.class.getMethod( "run", new Class[ 0 ] );
        final Object proxy = new Object();
        final Object[] args = new Object[ 0 ];
        final Object returnValue = handler.invoke( proxy, method, args );
        assertNull( "returnValue:", returnValue );

        final Invocation invocation = new Invocation();
        invocation.setProxy( proxy );
        invocation.setMethod( method );
        invocation.setParameters( args );
        invocation.setReturnValue( null );
        invocation.setThrowable( null );
        invocation.setContext( context );

        XInvokeTestUtil.assertEqualsInvocations( "SimpleInvoke.",
                                                 interceptor.getInvocation(),
                                                 invocation );
    }

    public void testSimpleInvokeWithParams()
        throws Throwable
    {
        final SimpleTestInterceptor interceptor = new SimpleTestInterceptor();
        final HashMap context = new HashMap();
        final RootInvocationHandler handler =
            new RootInvocationHandler( interceptor, context );
        final Object proxy = new Object();
        final Object returnValue = handler.invoke( proxy, XInvokeTestUtil.ACTION_METHOD, XInvokeTestUtil.ACTION_PARAMS );
        assertNull( "returnValue:", returnValue );

        final Invocation invocation = new Invocation();
        invocation.setProxy( proxy );
        invocation.setMethod( XInvokeTestUtil.ACTION_METHOD );
        invocation.setParameters( XInvokeTestUtil.ACTION_PARAMS );
        invocation.setReturnValue( null );
        invocation.setThrowable( null );
        invocation.setContext( context );

        XInvokeTestUtil.assertEqualsInvocations( "SimpleInvokeWithParams.",
                                                 interceptor.getInvocation(),
                                                 invocation );
    }

    public void testSimpleInvokeWithException()
        throws Throwable
    {
        final ExceptionTestInterceptor interceptor = new ExceptionTestInterceptor();
        final HashMap context = new HashMap();
        final RootInvocationHandler handler =
            new RootInvocationHandler( interceptor, context );
        final Object proxy = new Object();
        Object returnValue = null;
        try
        {
            returnValue = handler.invoke( proxy, XInvokeTestUtil.ACTION_METHOD, XInvokeTestUtil.ACTION_PARAMS );
        }
        catch( final Throwable throwable )
        {
            assertEquals( "Throwable:", interceptor.getThrowable(), throwable );
        }
        finally
        {
            if( null != returnValue )
            {
                fail( "Expected exception!" );
            }
        }

        final Invocation invocation = new Invocation();
        invocation.setProxy( proxy );
        invocation.setMethod( XInvokeTestUtil.ACTION_METHOD );
        invocation.setParameters( XInvokeTestUtil.ACTION_PARAMS );
        invocation.setReturnValue( null );
        invocation.setThrowable( interceptor.getThrowable() );
        invocation.setContext( context );

        XInvokeTestUtil.assertEqualsInvocations( "SimpleInvokeWithException.",
                                                 interceptor.getInvocation(),
                                                 invocation );
    }

    public void testSimpleInvokeWithReturnValue()
        throws Throwable
    {
        final ReturnValueTestInterceptor interceptor = new ReturnValueTestInterceptor();
        final HashMap context = new HashMap();
        final RootInvocationHandler handler =
            new RootInvocationHandler( interceptor, context );
        final Object proxy = new Object();

        final Object returnValue = handler.invoke( proxy, XInvokeTestUtil.PRIV_METHOD, XInvokeTestUtil.PRIV_PARAMS );
        assertEquals( "returnValue:", interceptor.getReturnValue(), returnValue );

        final Invocation invocation = new Invocation();
        invocation.setProxy( proxy );
        invocation.setMethod( XInvokeTestUtil.PRIV_METHOD );
        invocation.setParameters( XInvokeTestUtil.PRIV_PARAMS );
        invocation.setReturnValue( interceptor.getReturnValue() );
        invocation.setThrowable( null );
        invocation.setContext( context );

        XInvokeTestUtil.assertEqualsInvocations( "SimpleInvokeWithReturnValue.",
                                                 interceptor.getInvocation(),
                                                 invocation );
    }

    public void testSimpleInvokePostDispose()
        throws Throwable
    {
        final SimpleTestInterceptor interceptor = new SimpleTestInterceptor();
        final HashMap context = new HashMap();
        final RootInvocationHandler handler =
            new RootInvocationHandler( interceptor, context );
        final Object proxy = new Object();
        handler.invoke( proxy, XInvokeTestUtil.ACTION_METHOD, XInvokeTestUtil.ACTION_PARAMS );
        handler.dispose();
        try
        {
            handler.invoke( proxy, XInvokeTestUtil.ACTION_METHOD, XInvokeTestUtil.ACTION_PARAMS );
        }
        catch( final Throwable throwable )
        {
            return;
        }
        fail( "Expected exception to be throw after handler disposed" );

    }

    public void testComplexInvoke()
        throws Throwable
    {
        final SimpleTestInterceptor interceptor = new SimpleTestInterceptor();
        final HashMap context = new HashMap();
        final RootInvocationHandler handler =
            new RootInvocationHandler( interceptor, context );
        final Runnable proxy = (Runnable)newProxy( XInvokeTestUtil.RUN_CLASS, handler );
        proxy.run();

        final Invocation invocation = new Invocation();
        invocation.setProxy( proxy );
        invocation.setMethod( XInvokeTestUtil.RUN_METHOD );
        invocation.setParameters( XInvokeTestUtil.RUN_PARAMS );
        invocation.setReturnValue( null );
        invocation.setThrowable( null );
        invocation.setContext( context );

        XInvokeTestUtil.assertEqualsInvocations( "ComplexInvoke.",
                                                 interceptor.getInvocation(),
                                                 invocation );
    }

    public void testComplexInvokeWithParams()
        throws Throwable
    {
        final SimpleTestInterceptor interceptor = new SimpleTestInterceptor();
        final HashMap context = new HashMap();
        final RootInvocationHandler handler =
            new RootInvocationHandler( interceptor, context );

        final ActionListener proxy = (ActionListener)newProxy( XInvokeTestUtil.ACTION_CLASS, handler );
        proxy.actionPerformed( XInvokeTestUtil.ACTION_EVENT );

        final Invocation invocation = new Invocation();
        invocation.setProxy( proxy );
        invocation.setMethod( XInvokeTestUtil.ACTION_METHOD );
        invocation.setParameters( XInvokeTestUtil.ACTION_PARAMS );
        invocation.setReturnValue( null );
        invocation.setThrowable( null );
        invocation.setContext( context );

        XInvokeTestUtil.assertEqualsInvocations( "ComplexInvoke.",
                                                 interceptor.getInvocation(),
                                                 invocation );
    }

    public void testComplexInvokeWithException()
        throws Throwable
    {
        final ExceptionTestInterceptor interceptor =
            new ExceptionTestInterceptor( new IllegalArgumentException() );
        final HashMap context = new HashMap();
        final RootInvocationHandler handler =
            new RootInvocationHandler( interceptor, context );
        final ActionListener proxy = (ActionListener)newProxy( XInvokeTestUtil.ACTION_CLASS, handler );
        boolean excepted = false;
        try
        {
            proxy.actionPerformed( XInvokeTestUtil.ACTION_EVENT );
        }
        catch( final Throwable throwable )
        {
            assertEquals( "Throwable:", interceptor.getThrowable(), throwable );
            excepted = true;
        }
        finally
        {
            if( !excepted )
            {
                fail( "Expected exception!" );
            }
        }

        final Invocation invocation = new Invocation();
        invocation.setProxy( proxy );
        invocation.setMethod( XInvokeTestUtil.ACTION_METHOD );
        invocation.setParameters( XInvokeTestUtil.ACTION_PARAMS );
        invocation.setReturnValue( null );
        invocation.setThrowable( interceptor.getThrowable() );
        invocation.setContext( context );

        XInvokeTestUtil.assertEqualsInvocations( "ComplexInvokeWithException.",
                                                 interceptor.getInvocation(),
                                                 invocation );
    }

    public void testComplexInvokeWithReturnValue()
        throws Throwable
    {
        final ReturnValueTestInterceptor interceptor = new ReturnValueTestInterceptor();
        final HashMap context = new HashMap();
        final RootInvocationHandler handler =
            new RootInvocationHandler( interceptor, context );

        final PrivilegedAction proxy = (PrivilegedAction)newProxy( XInvokeTestUtil.PRIV_CLASS, handler );
        final Object returnValue = proxy.run();
        assertEquals( "returnValue:", interceptor.getReturnValue(), returnValue );

        final Invocation invocation = new Invocation();
        invocation.setProxy( proxy );
        invocation.setMethod( XInvokeTestUtil.PRIV_METHOD );
        invocation.setParameters( XInvokeTestUtil.PRIV_PARAMS );
        invocation.setReturnValue( returnValue );
        invocation.setThrowable( null );
        invocation.setContext( context );

        XInvokeTestUtil.assertEqualsInvocations( "ComplexInvoke.",
                                                 interceptor.getInvocation(),
                                                 invocation );
    }

    public void testComplexInvokePostDispose()
        throws Throwable
    {
        final SimpleTestInterceptor interceptor = new SimpleTestInterceptor();
        final HashMap context = new HashMap();
        final RootInvocationHandler handler =
            new RootInvocationHandler( interceptor, context );
        final ActionListener proxy = (ActionListener)newProxy( XInvokeTestUtil.ACTION_CLASS, handler );
        proxy.actionPerformed( XInvokeTestUtil.ACTION_EVENT );
        handler.dispose();
        try
        {
            proxy.actionPerformed( XInvokeTestUtil.ACTION_EVENT );
        }
        catch( final Throwable throwable )
        {
            return;
        }
        fail( "Expected exception to be throw after handler disposed" );
    }

    private Object newProxy( final Class clazz, final RootInvocationHandler handler )
    {
        return Proxy.newProxyInstance( getClass().getClassLoader(), new Class[]{clazz}, handler );
    }

}
