/*
 * Copyright (C) The Spice Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 *
 * This product includes software developed by the
 * Apache Software Foundation (http://www.apache.org/).
 */
package org.realityforge.converter.lib;

import org.realityforge.converter.AbstractConverter;
import org.realityforge.converter.ConverterException;

/**
 * String to Boolean converter
 *
 * @author <a href="mailto:peter at realityforge.org">Peter Donald</a>
 * @version $Revision: 1.3 $ $Date: 2003-03-18 09:14:14 $
 */
public class StringToBooleanConverter
    extends AbstractConverter
{
    /**
     * Construct the converter.
     */
    public StringToBooleanConverter()
    {
        this( null );
    }

    /**
     * Construct the converter with a default value.
     * If the default value is non-null, it will be returned if unable
     * to convert object to correct type.
     *
     * @param defaultValue the default value
     */
    public StringToBooleanConverter( final Boolean defaultValue )
    {
        super( String.class, Boolean.class, defaultValue );
    }

    /**
     * Converts a String to a Boolean.
     *
     * @param object the original object to convert
     * @param context the context in which to convert object (unused)
     * @return the converted object
     * @throws ConverterException if error converting object
     */
    public Object convert( final Object object, final Object context )
        throws ConverterException
    {
        final String string = (String)object;
        if( string.equalsIgnoreCase( "1" ) ||
            string.equalsIgnoreCase( "true" ) ||
            string.equalsIgnoreCase( "yes" ) )
        {
            return Boolean.TRUE;
        }
        else if( string.equalsIgnoreCase( "0" ) ||
            string.equalsIgnoreCase( "false" ) ||
            string.equalsIgnoreCase( "no" ) )
        {
            return Boolean.FALSE;
        }
        else
        {
            return noConvert( object, null );
        }
    }
}

