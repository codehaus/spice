/*
 * Copyright (C) The Spice Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */
package org.realityforge.xinvoke.spi.impl;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.Map;
import org.realityforge.xinvoke.Interceptor;
import org.realityforge.xinvoke.Invocation;

/**
 * This is an implementation of the InvocationHandler that
 * simply invokes the appropriate Interceptor in specified
 * context.
 *
 * @author <a href="mailto:peter@apache.org">Peter Donald</a>
 * @version $Revision: 1.1 $ $Date: 2003-04-16 10:47:01 $
 */
class RootInvocationHandler
    implements InvocationHandler
{
    /**
     * The interceptor to use for invocation.
     */
    private Interceptor m_interceptor;

    /**
     * The context in which to make invocation.
     */
    private final Map m_context;

    /**
     * Create handler with specified interceptor and context.
     *
     * @param interceptor the interceptor
     * @param context the context
     */
    RootInvocationHandler( final Interceptor interceptor,
                           final Map context )
    {
        if( null == interceptor )
        {
            throw new NullPointerException( "interceptor" );
        }
        if( null == context )
        {
            throw new NullPointerException( "context" );
        }
        m_interceptor = interceptor;
        m_context = context;
    }

    /**
     * Dispose invocation handler so that any invocations on this
     * will result in IllegalStateException or NullPointerException.
     * The NullPointerException can only occur if dispose() is called
     * halfway through an invocation and is the result of a race
     * condition. Fixing the race condition via synchronization would
     * add too much overhead and thus it is deemed acceptable to
     * instead get a NullPointerException.
     */
    void dispose()
    {
        m_interceptor = null;
    }

    /**
     * Actually perform the invocation which involves
     * executing the Interceptor.
     *
     * @param proxy the proxy object that method was invoked on
     * @param method the method invoked
     * @param args the arguments supplied to method
     * @return the return value of invocation (if any)
     * @throws Throwable the exception thrown by invocation (if any)
     */
    public Object invoke( final Object proxy,
                          final Method method,
                          final Object[] args )
        throws Throwable
    {
        if( null == m_interceptor )
        {
            final String message =
                "Invocation attempted on disposed InvocationHandler";
            throw new IllegalStateException( message );
        }

        //This is woefully inefficient so we should
        //rework this in the future
        final Invocation invocation = new Invocation();
        invocation.setMethod( method );
        invocation.setParameters( args );
        invocation.setProxy( proxy );
        invocation.setContext( m_context );

        m_interceptor.invoke( invocation );

        final Throwable throwable = invocation.getThrowable();
        if( null != throwable )
        {
            throw throwable;
        }
        return invocation.getReturnValue();
    }
}
