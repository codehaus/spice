/*
 * Copyright (C) The Spice Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */
package org.realityforge.xinvoke.spi.impl.test;

import junit.framework.Assert;
import org.realityforge.xinvoke.Invocation;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.reflect.Method;
import java.security.PrivilegedAction;

/**
 *
 * @author <a href="mailto:peter at realityforge.org">Peter Donald</a>
 * @version $Revision: 1.1 $ $Date: 2003-01-29 00:14:33 $
 */
public class XInvokeTestUtil
{
    public static final ActionEvent ACTION_EVENT =
        new ActionEvent( new Object(), 0, "DoNothing" );
    public static final Class[] ACTION_PARAMETER_TYPES = new Class[]{ActionEvent.class};
    public static final Class ACTION_CLASS = ActionListener.class;
    public static final Method ACTION_METHOD =
        getMethod( ActionListener.class, "actionPerformed", ACTION_PARAMETER_TYPES );
    public static final Object[] ACTION_PARAMS = new Object[]{ACTION_EVENT};
    public static final Object[] RUN_PARAMS = new Object[ 0 ];
    public static final Method RUN_METHOD =
        getMethod( Runnable.class, "run", new Class[ 0 ] );
    public static final Class RUN_CLASS = Runnable.class;
    public static final Object[] PRIV_PARAMS = new Object[ 0 ];
    public static final Method PRIV_METHOD =
        getMethod( PrivilegedAction.class, "run", new Class[ 0 ] );
    public static final Class PRIV_CLASS = PrivilegedAction.class;

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

    public static Method getMethod( final Class clazz,
                                     final String name,
                                     final Class[] parameterTypes )
    {
        try
        {
            return clazz.getMethod( name, parameterTypes );
        }
        catch( final Exception e )
        {
            throw new IllegalStateException( e.toString() );
        }
    }
}
