/*
 * Copyright (C) The Spice Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */
package org.codehaus.spice.converter.lib;

import java.sql.Date;
import org.codehaus.spice.converter.AbstractConverter;
import org.codehaus.spice.converter.ConverterException;

/**
 * String to Date converter.
 *
 * <p>Parses a date according to the same rules as the Date.parse() method. In
 * particular it recognizes the IETF standard date syntax:</p>
 * <p>"Sat, 12 Aug 1995 13:30:00 GMT"</p>
 *
 * @author Peter Donald
 * @version $Revision: 1.1 $ $Date: 2003-12-02 08:37:56 $
 */
public class StringToSQLDateConverter
    extends AbstractConverter
{
    /**
     * Construct the converter.
     */
    public StringToSQLDateConverter()
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
    public StringToSQLDateConverter( final Date defaultValue )
    {
        super( String.class, Date.class, defaultValue );
    }

    /**
     * Converts a String to a Date.
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
            return Date.valueOf( (String)object );
        }
        catch( final IllegalArgumentException iae )
        {
            return noConvert( object, iae );
        }
    }
}

