/*
 * Copyright (C) The Spice Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */
package org.realityforge.xinvoke.spi.impl;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

/**
 * This is a adapter that exposes some target related selector
 * expressions to the Jexl expression language.
 *
 * @author <a href="mailto:peter at www.stocksoftware.com.au">Peter Donald</a>
 * @version $Revision: 1.1 $ $Date: 2003-01-29 00:14:37 $
 */
public class JexlTargetHelper
    extends JexlClassHelper
{
    /**
     * The method this helper accesses information from.
     */
    private Method m_method;

    /**
     * The target object this helper accesses information from.
     * May be null.
     */
    private Object m_target;

    /**
     * Set the method.
     *
     * @param method the method.
     */
    public void setMethod( final Method method )
    {
        m_method = method;
    }

    /**
     * Set the target object.
     *
     * @param target the target object.
     */
    public void setTarget( final Object target )
    {
        m_target = target;
    }

    /**
     * Return true if target is null.
     *
     * @return true if target is null.
     */
    public boolean isNull()
    {
        return null == m_target;
    }

    /**
     * Return true if target does not implement method.
     *
     * @return true if target does not implement method.
     */
    public boolean noImplement()
    {
        if( isNull() )
        {
            return true;
        }

        //Make sure that the target object has a public method
        //with same name, parameter types and return type. Ignore
        //other method parts (like is it synchronized etc.
        //This is because the tested parts are part of public interface
        //of method but the other parts are implementation details
        final Class clazz = m_target.getClass();
        try
        {
            final Method other =
                clazz.getMethod( m_method.getName(), m_method.getParameterTypes() );

            if( !Modifier.isPublic( other.getModifiers() ) )
            {
                return true;
            }

            final Class returnType = other.getReturnType();
            if(  returnType == m_method.getReturnType() )
            {
                return true;
            }
            else
            {
                return false;
            }
        }
        catch( Exception e )
        {
            return true;
        }
    }
}
