/*
 * Copyright (C) The Spice Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */
package org.realityforge.configkit;

import java.io.InputStream;
import java.net.URL;
import org.xml.sax.InputSource;

/**
 * Utility class to get ConfigValidator objects for components.
 * See {@link #getComponentConfigValidator} for a detailed
 * explanation about how ConfigValidator objects are loaded.
 *
 * @author <a href="mailto:peter at realityforge.org">Peter Donald</a>
 * @version $Revision: 1.7 $ $Date: 2003-10-06 12:24:34 $
 */
public class ComponentConfigUtil
{
    /**
     * Postfix added to classname of component to look for schema.
     */
    private static final String DEFAULT_LOCATION_POSTFIX = "-schema.xml";

    /**
     * Return the ConfigValidator for specified component.
     * The component is specified by classname and classloader.
     * The ConfigValidator is loaded from specified location
     * and has specified type. If the type is null then ConfigKit
     * will attempt to guess the type based on schema location.
     * The location parameter can be relative to the component (ie
     * FooSchema.xml for class com.biz.Bar will load resource
     * "/com/biz/FooSchema.xml" from classloader), absolute resource
     * location in classloader (must start with "/") or null. If the
     * location is null then it will assume resource name is the same
     * name as class with the postfix "-schema.xml" added to classname.
     * If no such resource is located in the ClassLoader then null is
     * returned.
     *
     * @param classname the classname of component
     * @param classLoader the classloader component loaded from
     * @param location the location of schema
     * @param type the type of schema
     * @return the ConfigValidator
     * @throws java.lang.Exception if error creating validator
     */
    public static ConfigValidator getComponentConfigValidator( final String classname,
                                                               final ClassLoader classLoader,
                                                               final String location,
                                                               final String type )
        throws Exception
    {
        if( null == classname )
        {
            throw new NullPointerException( "classname" );
        }
        if( null == classLoader )
        {
            throw new NullPointerException( "classLoader" );
        }
        final String actualLocation = calculateLocation( classname, location );
        final InputSource inputSource =
            getSchemaInputSource( actualLocation.substring( 1 ), classLoader );
        if( null != inputSource )
        {
            return ConfigValidatorFactory.create( type, inputSource );
        }
        else
        {
            return null;
        }
    }

    /**
     * Determine the location of configuration schema for class.
     * If the specified location is not null then that will
     * be returned otherwise the location is the resource name
     * of the with {@link #DEFAULT_LOCATION_POSTFIX} appended
     * rather than ".class". ie If the classname was "com.biz.Foo"
     * then the schema location would be at "/com/biz/Foo-schema.xml".
     *
     * @param classname the name of the class
     * @param location the specified location of schema
     * @return the actual location of schema
     */
    static String calculateLocation( final String classname,
                                     final String location )
    {
        if( null == location )
        {
            return "/" + classname.replace( '.', '/' ) +
                DEFAULT_LOCATION_POSTFIX;
        }
        else if( location.startsWith( "/" ) )
        {
            return location;
        }
        else
        {
            final int index = classname.lastIndexOf( '.' );
            String packageName;
            if( -1 != index )
            {
                packageName = classname.substring( 0, index + 1 );
            }
            else
            {
                packageName = "";
            }
            return "/" + packageName.replace( '.', '/' ) + location;
        }
    }

    /**
     * Get the input source for schema specified for component.
     *
     * @param resource the resource location of schema
     * @param classLoader the ClassLoader to load schema from
     * @return the InputSource for schema
     */
    static InputSource getSchemaInputSource( final String resource,
                                             final ClassLoader classLoader )
    {
        final InputStream inputStream = classLoader.getResourceAsStream( resource );
        if( null == inputStream )
        {
            return null;
        }

        final InputSource inputSource = new InputSource( inputStream );
        final URL url = classLoader.getResource( resource );
        if( null != url )
        {
            inputSource.setSystemId( url.toExternalForm() );
        }
        else
        {
            inputSource.setSystemId( resource );
        }
        return inputSource;
    }
}
