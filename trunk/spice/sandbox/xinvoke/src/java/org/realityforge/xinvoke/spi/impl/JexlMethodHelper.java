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
 * This is a adapter that exposes some selector expressions
 * to the Jexl expression language.
 *
 * @author <a href="mailto:peter at www.stocksoftware.com.au">Peter Donald</a>
 * @version $Revision: 1.1 $ $Date: 2003-04-16 10:47:01 $
 */
public class JexlMethodHelper
{
    /**
     * The method this helper accesses information from.
     */
    private Method m_method;

    /**
     * Set the method that is used by helper.
     *
     * @param method the method that is used by helper.
     */
    public void setMethod( final Method method )
    {
        m_method = method;
    }

    /**
     * Return true if the method has specified name.
     *
     * @return true if the method has specified name.
     */
    public boolean withName( final String name )
    {
        return m_method.getName().equals( name );
    }

    /**
     * Return true if the method has name matching regex pattern.
     *
     * @return true if the method has name matching regex pattern.
     */
    public boolean withNameMatching( final String pattern )
    {
        final String name = m_method.getName();
        return MatcherUtil.match( pattern, name );
    }

    /**
     * Return true if the method is a synchronized method.
     *
     * @return true if the method is a synchronized method.
     */
    public boolean isSynchronized()
    {
        return Modifier.isSynchronized( m_method.getModifiers() );
    }

    /**
     * Return true if the method has the specified Method Attribute.
     *
     * @return true if the method has the specified Method Attribute.
     */
    public boolean withAttribute( final String name )
    {
        return false;//Attributes.getMethodAttribute( m_method, name );
    }
}
