/*
 * Copyright (C) The Spice Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */
package org.realityforge.converter.lib;

import org.realityforge.converter.AbstractConverter;
import org.realityforge.converter.ConverterException;

/**
 * String to float converter
 *
 * @author <a href="mailto:peter at realityforge.org">Peter Donald</a>
 * @version $Revision: 1.2 $ $Date: 2003-02-12 11:01:31 $
 */
public class StringToFloatConverter
    extends AbstractConverter
{
    /**
     * Construct the converter.
     */
    public StringToFloatConverter()
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
    public StringToFloatConverter( final Float defaultValue )
    {
        super( String.class, Float.class, defaultValue );
    }

    /**
     * Converts a String to a Float.
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
            return new Float( (String)object );
        }
        catch( final NumberFormatException nfe )
        {
            return noConvert( object, nfe );
        }
    }
}

