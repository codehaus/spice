/*
 * Copyright (C) The Spice Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */
package org.realityforge.metaclass.model;

import java.io.Serializable;
import java.util.Properties;
import java.util.Set;

/**
 * Attributes are the mechanism via which metadata is represented.
 * Each Attribute is made up of
 * <ul>
 *   <li>name: the name of the Attribute</li>
 *   <li>value: a simple text value of the Attribute.
 *              Is incompatible with parameters.</li>
 *   <li>parameters: a set of strings specifying parameters
 *                   for the Attribute. Is incompatible with
 *                   also having a text value.</li>
 * </ul>
 *
 * @author <a href="mailto:peter at realityforge.org">Peter Donald</a>
 * @author <a href="mailto:doug at doug@stocksoftware.com.au">Doug Hagan</a>
 * @version $Revision: 1.6 $ $Date: 2003-08-15 08:35:18 $
 */
public final class Attribute
    implements Serializable
{
    /**
     * The constant for Empty Set of attributes.
     */
    public static final Attribute[] EMPTY_SET = new Attribute[ 0 ];

    /**
     * The constant for Empty Set of keys.
     */
    private static final String[] EMPTY_NAMES_SET = new String[ 0 ];

    /**
     * The name of the Attribute.
     */
    private final String _name;

    /**
     * The value of the Attribute.
     */
    private final String _value;

    /**
     * The arbitrary set of parameters associated with the Attribute.
     */
    private final Properties _parameters;

    /**
     * Create a Attribute with specified name.
     *
     * @param name the Attribute name
     */
    public Attribute( final String name )
    {
        this( name, null, null );
    }

    /**
     * Create a Attribute with specified name and parameters.
     *
     * @param name the Attribute name
     * @param parameters the Attribute parameters
     */
    public Attribute( final String name,
                      final Properties parameters )
    {
        this( name, null, parameters );
    }

    /**
     * Create a Attribute with specified name and value.
     *
     * @param name the Attribute name
     * @param value the Attribute value
     */
    public Attribute( final String name,
                      final String value )
    {
        this( name, value, null );
    }

    /**
     * Create a Attribute with specified name, value
     * and parameters. Note that it is invalid for both
     * the value and parameters to be invalid.
     *
     * @param name the Attribute name
     * @param value the Attribute value
     * @param parameters the Attribute parameters
     */
    private Attribute( final String name,
                       final String value,
                       final Properties parameters )
    {
        if( null == name )
        {
            throw new NullPointerException( "name" );
        }

        if( null != value && null != parameters )
        {
            final String message =
                "Can not create an Attribute with both " +
                "a text value and parameters. (" +
                "name='" + name + "', " +
                "value='" + value + "', " +
                "parameters = " + parameters +
                "')";
            throw new IllegalArgumentException( message );
        }

        _name = name;
        _value = value;
        _parameters = parameters;
    }

    /**
     * Return the name of the Attribute.
     *
     * @return the name of the Attribute.
     */
    public String getName()
    {
        return _name;
    }

    /**
     * Return the value of the Attribute.
     * @return the value of the Attribute.
     */
    public String getValue()
    {
        return _value;
    }

    /**
     * Return the parameters of the Attribute.
     * @return the parameters of the Attribute.
     */
    public Properties getParameters()
    {
        return _parameters;
    }

    /**
     * Return the parameter for specified key.
     *
     * @return the parameter for specified key.
     */
    public String getParameter( final String key )
    {
        if( null == _parameters )
        {
            return null;
        }
        else
        {
            return _parameters.getProperty( key );
        }
    }

    /**
     * Return the parameter for specified key, or defaultValue if unspecified.
     *
     * @return the parameter for specified key, or defaultValue if unspecified.
     */
    public String getParameter( final String key,
                                final String defaultValue )
    {
        if( null == _parameters )
        {
            return defaultValue;
        }
        else
        {
            return _parameters.getProperty( key, defaultValue );
        }
    }

    /**
     * Returns an array of parameter names available under this Attribute.
     *
     * @return an array of parameter names available under this Attribute.
     */
    public String[] getParameterNames()
    {
        if( null == _parameters )
        {
            return EMPTY_NAMES_SET;
        }
        else
        {
            final Set set = _parameters.keySet();
            return (String[])set.toArray( EMPTY_NAMES_SET );
        }
    }
}
