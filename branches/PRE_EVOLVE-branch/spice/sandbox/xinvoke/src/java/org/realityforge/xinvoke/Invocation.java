/*
 * Copyright (C) The Spice Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */
package org.realityforge.xinvoke;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.Map;

/**
 * Object representing invocation. Note that this assumes
 * that parameters are ordered.
 *
 * @author <a href="mailto:peter at realityforge.org">Peter Donald</a>
 * @version $Revision: 1.1 $ $Date: 2003-04-16 10:47:00 $
 */
public class Invocation
    implements Serializable
{
    /**
     * The proxy on which the method was invoked.
     */
    private Object m_proxy;

    /**
     * The actual method that is being invoked.
     */
    private Method m_method;

    /**
     * The parameters of method invocation.
     */
    private Object[] m_parameters;

    /**
     * The return value of the invocation.
     */
    private Object m_returnValue;

    /**
     * The exception thrown by the invocation if any.
     */
    private Throwable m_throwable;

    /**
     * The context in which invocation occurs.
     */
    private Map m_context;

    /**
     * Return the proxy on which the method was invoked.
     *
     * @return the proxy on which the method was invoked.
     */
    public Object getProxy()
    {
        return m_proxy;
    }

    /**
     * Set the proxy on which the method was invoked.
     *
     * @param proxy the proxy on which the method was invoked.
     */
    public void setProxy( final Object proxy )
    {
        m_proxy = proxy;
    }

    /**
     * Return the method that was invoked.
     *
     * @return the method that was invoked.
     */
    public Method getMethod()
    {
        return m_method;
    }

    /**
     * Set the method that was invoked.
     *
     * @param method the method that was invoked.
     */
    public void setMethod( final Method method )
    {
        m_method = method;
    }

    /**
     * Return the parameters passed to method for invocation.
     *
     * @return the parameters passed to method for invocation.
     */
    public Object[] getParameters()
    {
        return m_parameters;
    }

    /**
     * Set the parameters passed to method for invocation.
     *
     * @param parameters the parameters passed to method for invocation.
     */
    public void setParameters( final Object[] parameters )
    {
        m_parameters = parameters;
    }

    /**
     * Return the return value of the invocation.
     *
     * @return the return value of the invocation.
     */
    public Object getReturnValue()
    {
        return m_returnValue;
    }

    /**
     * Set the return value of the invocation.
     *
     * @param returnValue the return value of the invocation.
     */
    public void setReturnValue( final Object returnValue )
    {
        m_returnValue = returnValue;
    }

    /**
     * Return the exception thrown by the invocation if any.
     *
     * @return the exception thrown by the invocation if any.
     */
    public Throwable getThrowable()
    {
        return m_throwable;
    }

    /**
     * Set the exception thrown by the invocation if any.
     *
     * @param throwable the exception thrown by the invocation if any.
     */
    public void setThrowable( Throwable throwable )
    {
        m_throwable = throwable;
    }

    /**
     * Return the context in which invocation occurs
     *
     * @return the context in which invocation occurs
     */
    public Map getContext()
    {
        return m_context;
    }

    /**
     * Set the context in which invocation occurs.
     *
     * @param context the context in which invocation occurs.
     */
    public void setContext( final Map context )
    {
        m_context = context;
    }
}
