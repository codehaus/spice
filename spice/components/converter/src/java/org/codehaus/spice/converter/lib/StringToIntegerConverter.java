/*
 * Copyright (C) The Spice Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */
package org.codehaus.spice.converter.lib;

import org.codehaus.spice.converter.AbstractConverter;
import org.codehaus.spice.converter.ConverterException;

/**
 * String to Integer converter.
 *
 * <p>Hexadecimal numbers begin with 0x, Octal numbers begin with 0o and binary
 * numbers begin with 0b, all other values are assumed to be decimal.</p>
 *
 * @author Peter Donald
 * @version $Revision: 1.1 $ $Date: 2003-12-02 08:37:56 $
 */
public class StringToIntegerConverter
    extends AbstractConverter
{
    /**
     * Construct the converter.
     */
    public StringToIntegerConverter()
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
    public StringToIntegerConverter( final Integer defaultValue )
    {
        super( String.class, Integer.class, defaultValue );
    }

    /**
     * Converts a String to an Integer.
     *
     * @param object the original object to convert
     * @param context the context in which to convert object (unused)
     * @return the converted object
     * @throws ConverterException if error converting object
     */
    public Object convert( final Object object, final Object context )
        throws ConverterException
    {
        try
        {
            final String value = (String)object;
            return Integer.decode( value );
        }
        catch( final NumberFormatException nfe )
        {
            return noConvert( object, nfe );
        }
    }
}

