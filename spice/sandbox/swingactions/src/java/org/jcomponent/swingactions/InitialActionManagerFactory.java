/*
 * Copyright (C) The Spice Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */
package org.jcomponent.swingactions;

import java.io.InputStream;
import java.net.URL;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * InitialActionManagerFactory is used as an intial context factory to
 * to create an ActionManager when the factory type is configurable.
 *
 * @author Mauro Talevi
 * @author Peter Donald
 */
public class InitialActionManagerFactory implements ActionManagerFactory
{
    /**
     * The FACTORY key.  Used to define the classname of the
     * configurable ActionManagerFactory.
     */
    public static final String FACTORY = "org.jcomponent.swingactions.factory";

    /**
     * The name of properties file loaded from ClassLoader. This property
     * file will be used to load default configuration settings if user failed
     * to specify them.
     */
    public static final String DEFAULT_PROPERTIES = "META-INF/jcomponent/swingactions.properties";

    /**
     * Create ActionManager by first determining the correct ActionManagerFactory
     * to use and then delegating to that factory. See Class Javadocs for the
     * process of locating ActionManager.
     *
     * @param config the input configuration
     * @return the ActionManager
     * @throws Exception if unable to create the ActionManager for any reason.
     */
    public ActionManager createActionManager( final Map config )
        throws Exception
    {
        final ClassLoader classLoader = getClassLoader( config );

        String type = (String)config.get( FACTORY );
        Map data = config;
        if( null == type )
        {
            data = loadDefaultConfig( data, classLoader );
            type = (String)data.get( FACTORY );
        }
        final ActionManagerFactory factory =
            createActionManagerFactory( type, classLoader );
        return factory.createActionManager( data );
    }

    /**
     * Retrieve the classloader from data map. If no classloader is specified
     * then use ContextClassLoader. If ContextClassLoader not specified then
     * use ClassLoader that loaded this class.
     *
     * @param data the configuration data
     * @return a ClassLoader
     */
    private ClassLoader getClassLoader( final Map data )
    {
        ClassLoader loader = (ClassLoader)data.get( ClassLoader.class.getName() );
        if( null == loader )
        {
            loader = Thread.currentThread().getContextClassLoader();
            if( null == loader )
            {
                loader = InitialActionManagerFactory.class.getClassLoader();
            }
        }
        return loader;
    }

    /**
     * Load the default properties for ActionManagerFactory.
     *
     * @param initial the input data
     * @param classLoader the classLoader to load properties files from
     * @return the new configuration data
     * @throws Exception if unable to load properties
     */
    private Map loadDefaultConfig( final Map initial,
                                   final ClassLoader classLoader )
        throws Exception
    {
        final HashMap map = new HashMap();

        final Enumeration resources =
            classLoader.getResources( DEFAULT_PROPERTIES );
        while( resources.hasMoreElements() )
        {
            final URL url = (URL)resources.nextElement();
            final InputStream stream = url.openStream();
            final Properties properties = new Properties();
            properties.load( stream );
            map.putAll( properties );
        }

        map.putAll( initial );
        return map;
    }

    /**
     * Create a {@link ActionManagerFactory} for specified factory.
     *
     * @param type the class name of the factory to use.
     * @return the created {@link ActionManagerFactory}
     */
    private ActionManagerFactory createActionManagerFactory( final String type,
                                                             final ClassLoader classLoader )
    {
        if( null == type )
        {
            final String message = "No ActionManagerFactory class specified.";
            throw new IllegalStateException( message );
        }

        try
        {
            final Class clazz = classLoader.loadClass( type );
            return (ActionManagerFactory)clazz.newInstance();
        }
        catch( final Exception e )
        {
            final String message =
                "Failed to created ActionManagerFactory " + type;
            throw new IllegalArgumentException( message );
        }
    }
}
