/*
 * Copyright (C) The Spice Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */
package org.realityforge.xinvoke.spi.impl;

import junit.framework.Assert;
import org.realityforge.xinvoke.Invocation;

/**
 *
 * @author <a href="mailto:peter at realityforge.org">Peter Donald</a>
 * @version $Revision: 1.1 $ $Date: 2003-01-29 00:14:37 $
 */
public class XInvokeTestUtil
{
    public static void assertEqualsInvocations( final String prefix,
                                                final Invocation invocation1,
                                                final Invocation invocation2 )
    {
        //Next line has to be identity or ese it will make a call on the proxy
        Assert.assertTrue( prefix + "getProxy()",
                           invocation1.getProxy() == invocation2.getProxy() );
        Assert.assertEquals( prefix + "getMethod()",
                             invocation1.getMethod(),
                             invocation2.getMethod() );
        Assert.assertEquals( prefix + "getReturnValue()",
                             invocation1.getReturnValue(),
                             invocation2.getReturnValue() );
        Assert.assertEquals( prefix + "getThrowable()",
                             invocation1.getThrowable(),
                             invocation2.getThrowable() );
        assertEqualArrays( prefix + "getParameters()",
                           invocation1.getParameters(),
                           invocation2.getParameters() );
    }

    public static void assertEqualArrays( final String prefix,
                                          final Object[] objects1,
                                          final Object[] objects2 )
    {
        int length1 = 0;
        int length2 = 0;
        if( null != objects1 )
        {
            length1 = objects1.length;
        }
        if( null != objects2 )
        {
            length2 = objects2.length;
        }

        Assert.assertEquals( prefix + ".length.", length1, length2 );

        for( int i = 0; i < length1; i++ )
        {
            Assert.assertEquals( prefix + "[ " + i + " ]",
                                 objects1[ i ], objects2[ i ] );
        }
    }
}
