/*
 * Copyright (C) The Spice Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */
package org.realityforge.converter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import org.apache.avalon.excalibur.i18n.ResourceManager;
import org.apache.avalon.excalibur.i18n.Resources;

/**
 * This is a Converter implementation that is capable of converting between
 * many different source and destination types, by delegating delegates to
 * other converters that do the actual work.
 *
 * <p>To use this class you must subclass it, and register some converters
 * using the (@link #registerConverter} method.</p>
 *
 * @author <a href="mailto:peter at realityforge.org">Peter Donald</a>
 * @version $Revision: 1.3 $ $Date: 2003-03-25 02:51:26 $
 */
public abstract class AbstractMasterConverter
    implements Converter
{
    private static final Resources REZ =
        ResourceManager.getPackageResources( AbstractMasterConverter.class );

    /**
     * Cache of converter instances.  This is a map from ConverterFactory to
     * a Converter instance created by that factory.
     */
    private final Map m_converters = new HashMap();

    /**
     * This holds the mapping between source/destination and ConverterFactory.
     */
    private final HashMap m_mapping = new HashMap();

    /**
     * Convert object to destination type.
     *
     * @param destination the destination type
     * @param original the original object
     * @param context the context in which to convert
     * @return the converted object
     * @throws ConverterException if an error occurs
     */
    public Object convert( final Class destination,
                           final Object original,
                           final Object context )
        throws ConverterException
    {
        final Class originalClass = original.getClass();

        if( destination.isAssignableFrom( originalClass ) )
        {
            return original;
        }

        try
        {
            // Determine which converter to use
            final Converter converter = findConverter( originalClass, destination );

            // Convert
            final Object object = converter.convert( destination, original, context );
            if( destination.isInstance( object ) )
            {
                return object;
            }

            final String message =
                REZ.getString( "bad-return-type.error",
                               object.getClass().getName(),
                               destination.getName() );
            throw new ConverterException( message );
        }
        catch( final Exception e )
        {
            final String message =
                REZ.getString( "convert.error",
                               originalClass.getName(),
                               destination.getName() );
            throw new ConverterException( message, e );
        }
    }

    /**
     * Returns the Converter instance to use to convert between a particular
     * pair of classes.
     */
    private Converter findConverter( final Class originalClass,
                                     final Class destination )
        throws Exception
    {
        // Locate the factory to use
        final ConverterFactory factory = findConverterFactory( originalClass, destination );

        // Create the converter
        Converter converter = (Converter)m_converters.get( factory );
        if( converter == null )
        {
            converter = factory.createConverter();
            m_converters.put( factory, converter );
        }
        return converter;
    }

    /**
     * Register a converter
     *
     * @param factory the factory to use to create converter instances.
     * @param source the source classname
     * @param destination the destination classname
     */
    protected void registerConverter( final ConverterFactory factory,
                                      final String source,
                                      final String destination )
    {
        HashMap map = (HashMap)m_mapping.get( source );
        if( null == map )
        {
            map = new HashMap();
            m_mapping.put( source, map );
        }

        map.put( destination, factory );

        //Remove instance of converter if it has already been created
        m_converters.remove( factory );
    }

    /**
     * Determine the type of converter (represented as a ConverterFactory) to
     * use to convert between original and destination classes.
     */
    private ConverterFactory findConverterFactory( final Class originalClass,
                                                   final Class destination )
        throws ConverterException
    {
        //TODO: Maybe we should search the destination classes hierarchy as well

        // Recursively iterate over the super-types of the original class,
        // looking for a converter from source type -> destination type.
        // If more than one is found, choose the most specialised.

        Class bestSrcMatch = null;
        ConverterFactory matchFactory = null;
        ArrayList queue = new ArrayList();
        queue.add( originalClass );

        while( !queue.isEmpty() )
        {
            final Class clazz = (Class)queue.remove( 0 );

            // Add superclass and all interfaces
            if( clazz.getSuperclass() != null )
            {
                queue.add( clazz.getSuperclass() );
            }
            final Class[] interfaces = clazz.getInterfaces();
            for( int i = 0; i < interfaces.length; i++ )
            {
                queue.add( interfaces[ i ] );
            }

            // Check if we can convert from current class to destination
            final ConverterFactory factory =
                getConverterFactory( clazz.getName(), destination.getName() );
            if( factory == null )
            {
                continue;
            }

            // Choose the more specialised source class
            if( bestSrcMatch == null || bestSrcMatch.isAssignableFrom( clazz ) )
            {
                bestSrcMatch = clazz;
                matchFactory = factory;
            }
            else if( clazz.isAssignableFrom( bestSrcMatch ) )
            {
                continue;
            }
            else
            {
                // Duplicate
                final String message = REZ.getString( "ambiguous-converter.error" );
                throw new ConverterException( message );
            }
        }

        // TODO - should cache the (src, dest) -> converter mapping
        if( bestSrcMatch != null )
        {
            return matchFactory;
        }

        // Could not find a converter
        final String message = REZ.getString( "no-converter.error" );
        throw new ConverterException( message );
    }

    /**
     * Retrieve factory for the converter that converts from source to destination.
     */
    private ConverterFactory getConverterFactory( final String source,
                                                  final String destination )
    {
        final HashMap map = (HashMap)m_mapping.get( source );
        if( null == map )
        {
            return null;
        }
        return (ConverterFactory)map.get( destination );
    }
}
