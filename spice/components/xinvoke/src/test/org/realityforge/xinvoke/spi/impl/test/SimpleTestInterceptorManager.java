/*
 * Copyright (C) The Apache Software Foundation. All rights reserved.
 *
 * This software is published under the terms of the Apache Software License
 * version 1.1, a copy of which has been included  with this distribution in
 * the LICENSE.txt file.
 */
package org.realityforge.xinvoke.spi.impl.test;

import org.realityforge.xinvoke.spi.InterceptorManager;
import org.realityforge.xinvoke.spi.ProxyManager;
import org.realityforge.xinvoke.Interceptor;
import org.realityforge.xinvoke.lib.JavaInvokeInterceptor;
import org.realityforge.xinvoke.lib.NoopInterceptor;
import java.util.Map;

/**
 *
 * @author <a href="mailto:peter at realityforge.org">Peter Donald</a>
 * @version $Revision: 1.1 $ $Date: 2003-01-29 00:14:33 $
 */
public class SimpleTestInterceptorManager
    implements InterceptorManager
{
    public Interceptor getInterceptor( Map parameters )
        throws Exception
    {
        final Object target = parameters.get( ProxyManager.TARGET );
        if( null == target )
        {
            return new NoopInterceptor();
        }
        else
        {
            return new JavaInvokeInterceptor( target );
        }
    }

    public void releaseInterceptor( final Interceptor interceptor )
        throws Exception
    {
        //ignore
    }
}
