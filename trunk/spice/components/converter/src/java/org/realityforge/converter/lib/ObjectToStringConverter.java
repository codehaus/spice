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
 * A general-purpose converter that converts an Object to a String using
 * its toString() method.
 *
 * @author <a href="mailto:adammurdoch@apache.org">Adam Murdoch</a>
 * @version $Revision: 1.2 $ $Date: 2003-03-18 09:14:12 $
 */
public class ObjectToStringConverter
    extends AbstractConverter
{
    /**
     * Construct the converter.
     */
    public ObjectToStringConverter()
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
    public ObjectToStringConverter( final String defaultValue )
    {
        super( Object.class, String.class, defaultValue );
    }

    /**
     * Converts an Object to a String.
     *
     * @param original the original object to convert
     * @param context the context in which to convert object (unused)
     * @return the converted object
     * @throws ConverterException if error converting object
     */
    protected Object convert( final Object original, final Object context )
        throws ConverterException
    {
        return original.toString();
    }
}
