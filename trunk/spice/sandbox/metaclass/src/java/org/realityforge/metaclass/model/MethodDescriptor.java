/*
 * Copyright (C) The Spice Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */
package org.realityforge.metaclass.model;

import java.io.Serializable;

/**
 * A descriptor that describes a Method. It contains
 * information about;
 * <ul>
 *   <li>name: the name of the method</li>
 *   <li>return type: the return type of the method</li>
 *   <li>modifiers: the access modifiers for the method</li>
 *   <li>parameters: the parameters a method takes</li>
 * </ul>
 *
 * <p>Also associated with each method is a set of arbitrary
 * Attributes that can be used to store extra information
 * about method.</p>
 *
 * @author <a href="mailto:peter at realityforge.org">Peter Donald</a>
 * @version $Revision: 1.3 $ $Date: 2003-08-15 06:53:32 $
 */
public final class MethodDescriptor
    extends FeatureDescriptor
    implements Serializable
{
    /**
     * The name of the Method.
     */
    private final String _name;

    /**
     * The return type of the method.
     */
    private final String _returnType;

    /**
     * The parameters associated with the method.
     */
    private final ParameterDescriptor[] _parameters;

    /**
     * Create a descriptor for a method.
     *
     * @param name the name of the method
     * @param returnType the return type of the method
     * @param modifiers the access modifiers for method
     * @param parameters the parameters of the method
     * @param attributes any attributes associated with method
     */
    public MethodDescriptor( final String name,
                             final String returnType,
                             final int modifiers,
                             final ParameterDescriptor[] parameters,
                             final Attribute[] attributes )
    {
        super( attributes, modifiers );
        if( null == name )
        {
            throw new NullPointerException( "name" );
        }
        if( null == returnType )
        {
            throw new NullPointerException( "returnType" );
        }
        if( null == parameters )
        {
            throw new NullPointerException( "parameters" );
        }

        _name = name;
        _returnType = returnType;
        _parameters = parameters;
    }

    /**
     * Return the name of the method.
     *
     * @return the name of the method.
     */
    public String getName()
    {
        return _name;
    }

    /**
     * Return the return type of the method.
     *
     * @return the return type of the method.
     */
    public String getReturnType()
    {
        return _returnType;
    }

    /**
     * Return the parameters associated with method.
     *
     * @return the parameters associated with method.
     */
    public ParameterDescriptor[] getParameters()
    {
        return _parameters;
    }
}
