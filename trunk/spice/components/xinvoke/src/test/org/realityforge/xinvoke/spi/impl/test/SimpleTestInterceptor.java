/*
 * Copyright (C) The Spice Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */
package org.realityforge.xinvoke.spi.impl.test;

import org.realityforge.xinvoke.Interceptor;
import org.realityforge.xinvoke.Invocation;

/**
 *
 * @author <a href="mailto:peter at realityforge.org">Peter Donald</a>
 * @version $Revision: 1.1 $ $Date: 2003-01-29 00:14:33 $
 */
public class SimpleTestInterceptor
    implements Interceptor
{
    private Invocation m_invocation;

    public void invoke( final Invocation invocation )
    {
        m_invocation = invocation;
    }

    public Invocation getInvocation()
    {
        return m_invocation;
    }
}
