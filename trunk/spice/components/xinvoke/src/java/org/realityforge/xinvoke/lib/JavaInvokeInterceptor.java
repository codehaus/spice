/*
 * Copyright (C) The Spice Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */
package org.realityforge.xinvoke.lib;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import org.realityforge.xinvoke.Interceptor;
import org.realityforge.xinvoke.Invocation;

/**
 * A simple Interceptor that just invokes method on java object via reflection.
 *
 * @author <a href="mailto:peter at realityforge.org">Peter Donald</a>
 * @version $Revision: 1.1 $ $Date: 2003-01-29 00:14:38 $
 */
public class JavaInvokeInterceptor
    implements Interceptor
{
    /**
     * The target object on which to invoke method.
     */
    private final Object m_target;

    /**
     * Create the invoker with specified target.
     *
     * @param target the target object
     */
    public JavaInvokeInterceptor( final Object target )
    {
        if( null == target )
        {
            throw new NullPointerException( "target" );
        }
        m_target = target;
    }

    /**
     * Process specified invocation on target object.
     *
     * @param invocation the invocation
     */
    public void invoke( final Invocation invocation )
    {
        try
        {
            final Method method = invocation.getMethod();
            final Object returnValue =
                method.invoke( m_target, invocation.getParameters() );
            invocation.setReturnValue( returnValue );
        }
        catch( final InvocationTargetException ite )
        {
            invocation.setThrowable( ite.getTargetException() );
        }
        catch( final Throwable throwable )
        {
            invocation.setThrowable( throwable );
        }
    }
}
