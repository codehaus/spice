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
 * String to class converter
 *
 * @author Peter Donald
 * @version $Revision: 1.1 $ $Date: 2003-12-02 08:37:56 $
 */
public class StringToClassConverter
    extends AbstractConverter
{
    /**
     * Construct the converter.
     */
    public StringToClassConverter()
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
    public StringToClassConverter( final Class defaultValue )
    {
        super( String.class, Class.class, defaultValue );
    }

    /**
     * Converts a String to a Class.
     *
     * @param object the original object to convert
     * @param context the context in which to convert object (unused)
     * @return the converted object
     * @throws ConverterException if error converting object
     */
    public Object convert( final Object object, final Object context )
        throws ConverterException
    {
        //TODO: Should we use ContextClassLoader here???
        try
        {
            return Class.forName( (String)object );
        }
        catch( final Exception e )
        {
            return noConvert( object, e );
        }
    }
}

