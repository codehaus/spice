/*
 * Copyright (C) The Spice Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */
package org.realityforge.xinvoke.lib;

import org.apache.avalon.framework.service.ServiceException;
import org.apache.avalon.framework.service.ServiceManager;
import org.apache.avalon.framework.service.Serviceable;
import org.realityforge.xinvoke.Interceptor;
import org.realityforge.xinvoke.Invocation;

/**
 * A simple interceptor that just passes
 * through invocation to next interceptor.
 *
 * @author <a href="mailto:peter at www.stocksoftware.com.au">Peter Donald</a>
 * @version $Revision: 1.1 $ $Date: 2003-04-16 10:47:00 $
 */
public class PassThroughInterceptor
    implements Interceptor, Serviceable
{
    private Interceptor m_next;

    /**
     * Aquire services required by interceptor.
     *
     * @avalon.dependency type="Interceptor"
     */
    public void service( final ServiceManager manager )
        throws ServiceException
    {
        m_next = (Interceptor)manager.lookup( Interceptor.ROLE );
    }

    /**
     * Invoke interceptor.
     *
     * @param invocation the invocation.
     */
    public void invoke( final Invocation invocation )
    {
        invokeNext( invocation );
    }

    /**
     * Invoke the next interceptor in the chain.
     *
     * @param invocation the invocation
     */
    protected final void invokeNext( final Invocation invocation )
    {
        m_next.invoke( invocation );
    }
}
