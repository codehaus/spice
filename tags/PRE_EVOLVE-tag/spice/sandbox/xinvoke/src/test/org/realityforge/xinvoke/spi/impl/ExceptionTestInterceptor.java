/*
 * Copyright (C) The Spice Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */
package org.realityforge.xinvoke.spi.impl;

import org.realityforge.xinvoke.Interceptor;
import org.realityforge.xinvoke.Invocation;

/**
 *
 * @author <a href="mailto:peter at realityforge.org">Peter Donald</a>
 * @version $Revision: 1.1 $ $Date: 2003-04-16 10:47:02 $
 */
class ExceptionTestInterceptor
    implements Interceptor
{
    private Invocation m_invocation;
    private Throwable m_throwable;

    public ExceptionTestInterceptor( Throwable throwable )
    {
        m_throwable = throwable;
    }

    public ExceptionTestInterceptor()
    {
        this( new Throwable() );
    }

    public void invoke( final Invocation invocation )
    {
        m_invocation = invocation;
        m_invocation.setThrowable( getThrowable() );
    }

    public Throwable getThrowable()
    {
        return m_throwable;
    }

    public Invocation getInvocation()
    {
        return m_invocation;
    }
}
