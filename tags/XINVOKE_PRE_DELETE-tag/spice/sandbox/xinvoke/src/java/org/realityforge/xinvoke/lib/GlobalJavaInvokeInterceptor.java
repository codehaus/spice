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
import org.realityforge.xinvoke.spi.ProxyManager;

/**
 * A simple Interceptor that just invokes
 * method on java object via reflection.
 * It extracts the target object from the InvocationContext
 * using key {@link ProxyManager#TARGET}.
 *
 * @author <a href="mailto:peter at realityforge.org">Peter Donald</a>
 * @version $Revision: 1.1 $ $Date: 2003-04-16 10:47:00 $
 */
public class GlobalJavaInvokeInterceptor
    implements Interceptor
{
    /**
     * Process specified invocation on target object.
     *
     * @param invocation the invocation
     */
    public void invoke( final Invocation invocation )
    {
        try
        {
            final Object target = getTarget( invocation );
            final Method method = invocation.getMethod();
            final Object returnValue =
                method.invoke( target, invocation.getParameters() );
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

    /**
     * Get target object out of invocation context or if not present
     * throw an exception.
     *
     * @param invocation the invocation
     * @return the target object
     * @throws IllegalStateException if unable to locate target object
     */
    private Object getTarget( final Invocation invocation )
        throws IllegalStateException
    {
        final Object target =
            invocation.getContext().get( ProxyManager.TARGET );
        if( null == target )
        {
            final String message =
                "Invalid invocation context does not specify target object";
            throw new IllegalStateException( message );
        }
        return target;
    }
}
