/*
 * Copyright (C) The Spice Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */
package org.codehaus.spice.alchemist.configuration;

import org.codehaus.dna.Configurable;
import org.codehaus.dna.Configuration;
import org.codehaus.dna.impl.DefaultConfiguration;

/**
 * Utility class containing methods to transform Configuration objects.
 *
 * @author Mauro Talevi
 * @author Peter Donald
 * @version $Revision: 1.6 $ $Date: 2004-06-20 14:03:58 $
 */
public class ConfigurationAlchemist
{
    /**
     * Convert Avalon Configuration to DNA Configuration
     *
     * @param configuration the Avalon Configuration
     * @return the DNA Configuration
     */
    public static Configuration toDNAConfiguration( final org.apache.avalon.framework.configuration.Configuration configuration )
    {
        if ( null == configuration ) { 
            throw new NullPointerException( "configuration" ); 
        }
		final DefaultConfiguration result = new DefaultConfiguration(
                configuration.getName(), configuration.getLocation(), "");
        final String[] names = configuration.getAttributeNames();
        for ( int i = 0; i < names.length; i++ ) 
        {
            final String name = names[i];
            final String value = configuration.getAttribute( name, null );
            result.setAttribute( name, value );
        }
        final org.apache.avalon.framework.configuration.Configuration[] children = configuration.getChildren();
        for ( int i = 0; i < children.length; i++ )
        {
            final Configuration child = toDNAConfiguration( children[i] );
            result.addChild( child );
        }
        final String value = configuration.getValue( null );
        if ( null != value ) 
        {
            result.setValue( value );
        }
        return result;
    }

    /**
     * Convert DNA Configuration to Avalon Configuration
     *
     * @param configuration the DNA  Configuration
     * @return the Avalon Configuration
     */
    public static org.apache.avalon.framework.configuration.Configuration toAvalonConfiguration( final Configuration configuration )
    {
        if ( null == configuration ) { 
            throw new NullPointerException( "configuration" ); 
        }
		final org.apache.avalon.framework.configuration.DefaultConfiguration result = new org.apache.avalon.framework.configuration.DefaultConfiguration(
                configuration.getName(), configuration.getLocation() );
        final String[] names = configuration.getAttributeNames();
        for ( int i = 0; i < names.length; i++ ) 
        {
            final String name = names[i];
            final String value = configuration.getAttribute( name, null );
            result.setAttribute( name, value );
        }
        final Configuration[] children = configuration.getChildren();
        for ( int i = 0; i < children.length; i++ )
        {
            final org.apache.avalon.framework.configuration.Configuration child = toAvalonConfiguration( children[i] );
            result.addChild( child );
        }
        final String value = configuration.getValue( null );
        if ( null != value ) 
        {
            result.setValue( value );
        }
        return result;
    }
    
    /**
     * Determines if an object is Avalon Configurable
     * 
     * @param object the Object to check
     * @return A boolean <code>true</code> if the object is an instance of
     *         {@link org.apache.avalon.framework.configuration.Configurable}
     */
    public static boolean isAvalonConfigurable( final Object object ){
        if ( object instanceof org.apache.avalon.framework.configuration.Configurable )
        {
            return true;
        }
        return false;
    }
    
    /**
     * Determines if an object is DNA Configurable
     * 
     * @param object the Object to check
     * @return A boolean <code>true</code> if the object is an instance of
     * 		   {@link Configurable}
     */
    public static boolean isDNAConfigurable( final Object object ){
        if ( object instanceof Configurable )
        {
            return true;
        }
        return false;
    }
    
    /**
     * Casts an object to Avalon Configurable if possible.
     * 
     * @param object the Object to cast
     * @return A {@link org.apache.avalon.framework.configuration.Configurable}
     * @throws IllegalArgumentException if not Avalon Configurable.
     */
    public static org.apache.avalon.framework.configuration.Configurable toAvalonConfigurable( final Object object ){
        if ( isAvalonConfigurable( object ) )
        {
            return (org.apache.avalon.framework.configuration.Configurable)object; 
        }
        final String message = ( object != null ? object.getClass().getName() : "Object" )
        						+ " is not Avalon Configurable";
        throw new IllegalArgumentException( message );
    }

    /**
     * Casts an object to DNA Configurable if possible.
     * 
     * @param object the Object to cast
     * @return A {@link Configurable}
     * @throws IllegalArgumentException if not DNA Configurable.
     */
    public static Configurable toDNAConfigurable( final Object object ){
        if ( isDNAConfigurable( object ) )
        {
            return (Configurable)object; 
        }
        final String message = ( object != null ? object.getClass().getName() : "Object" )
        						+ " is not DNA Configurable";
        throw new IllegalArgumentException( message );
    }
}
