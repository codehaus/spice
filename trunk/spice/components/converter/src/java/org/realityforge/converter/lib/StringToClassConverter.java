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
 * String to class converter
 *
 * @author <a href="mailto:peter at realityforge.org">Peter Donald</a>
 * @version $Revision: 1.3 $ $Date: 2003-03-18 09:14:16 $
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

