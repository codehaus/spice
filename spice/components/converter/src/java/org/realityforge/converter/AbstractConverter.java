/*
 * Copyright (C) The Spice Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */
package org.realityforge.converter;

import org.apache.avalon.excalibur.i18n.ResourceManager;
import org.apache.avalon.excalibur.i18n.Resources;

/**
 * Instances of this interface are used to convert between different types.
 *
 * @author <a href="mailto:peter at realityforge.org">Peter Donald</a>
 * @version $Revision: 1.3 $ $Date: 2003-03-25 02:51:26 $
 */
public abstract class AbstractConverter
    implements Converter
{
    private static final Resources REZ =
        ResourceManager.getPackageResources( AbstractConverter.class );

    private final Class m_source;
    private final Class m_destination;
    private final Object m_defaultValue;

    /**
     * Constructor for a converter between types source and destination
     *
     * @param source the source type
     * @param destination the destination type
     */
    protected AbstractConverter( final Class source, final Class destination )
    {
        this( source, destination, null );
    }

    /**
     * Constructor for a converter between types source and destination
     * with a default value.
     *
     * @param source the source type
     * @param destination the destination type
     * @param defaultValue the default value
     */
    protected AbstractConverter( final Class source,
                                 final Class destination,
                                 final Object defaultValue )
    {
        m_source = source;
        m_destination = destination;
        m_defaultValue = defaultValue;
    }

    /**
     * Convert an object from original to destination types
     *
     * @param destination the destination type
     * @param original the original Object
     * @param context the context in which to convert
     * @return the converted object
     * @exception ConverterException if an error occurs
     */
    public Object convert( final Class destination,
                           final Object original,
                           final Object context )
        throws ConverterException
    {
        if( m_destination != destination )
        {
            final String message =
                REZ.getString( "bad-destination.error", destination.getName(), m_destination );
            throw new IllegalArgumentException( message );
        }

        if( !m_source.isInstance( original ) )
        {
            final String message =
                REZ.getString( "bad-instance.error", original, m_source.getName() );
            throw new IllegalArgumentException( message );
        }

        return convert( original, context );
    }

    /**
     * A helper method to throw an exception indicating that could
     * not perform conversion of specified object due to an exception.
     */
    protected final Object noConvert( final Object value, final Throwable throwable )
        throws ConverterException
    {
        if( null != m_defaultValue )
        {
            return m_defaultValue;
        }
        else
        {
            final String message =
                REZ.getString( "no-convert.error",
                               m_source.getName(),
                               m_destination.getName(),
                               value,
                               throwable );
            throw new ConverterException( message );
        }
    }

    /**
     * Overide this in a particular converter to do the conversion.
     *
     * @param original the original Object
     * @param context the context in which to convert
     * @return the converted object
     * @throws ConverterException if an error occurs
     */
    protected abstract Object convert( Object original, Object context )
        throws ConverterException;
}

