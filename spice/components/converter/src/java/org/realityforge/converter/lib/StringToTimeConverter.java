/*
 * Copyright (C) The Spice Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */
package org.realityforge.converter.lib;

import java.sql.Time;
import org.realityforge.converter.AbstractConverter;
import org.realityforge.converter.ConverterException;

/**
 * String to Time converter.
 *
 * @author <a href="mailto:peter at realityforge.org">Peter Donald</a>
 * @version $Revision: 1.2 $ $Date: 2003-02-12 11:01:33 $
 */
public class StringToTimeConverter
    extends AbstractConverter
{
    /**
     * Construct the converter.
     */
    public StringToTimeConverter()
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
    public StringToTimeConverter( final Time defaultValue )
    {
        super( String.class, Time.class, defaultValue );
    }

    /**
     * Converts a String to a Time.
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
            return Time.valueOf( object.toString() );
        }
        catch( final Exception e )
        {
            return noConvert( object, e );
        }
    }
}

