/*
 * Copyright (C) The Spice Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */
package org.jcomponent.swingactions;

import java.io.InputStream;
import java.util.HashMap;

/**
 * Configurator is a collection of utility methods to create and configure
 * ActionManager objects of different types using configuration resources.
 * Presently only XML is supported.  
 *
 * @author <a href="mailto:mauro.talevi at aquilonia.org">Mauro Talevi</a>
 */
public class Configurator
{
    
    /**
     * Constant used to define XML type
     */
    public static final String XML = "xml";

    /**
     * Create and configure a {@link ActionManager} from a specified
     * configuration resource.
     *
     * @param configuratorType the type of the configurator
     * @param resource the String encoding the path of the configuration resource
     * @return the configured ActionManager
     * @throws Exception if unable to create the ActionManager
     */
    public static ActionManager createActionManager( final String configuratorType,
                                                 final String resource )
        throws Exception
    {
        final InitialActionManagerFactory factory = new InitialActionManagerFactory();
        final HashMap data = new HashMap();
        data.put( InitialActionManagerFactory.FACTORY, getFactoryClassName( configuratorType ) );
        data.put( ActionManagerFactory.FILE_LOCATION, resource );
        return factory.createActionManager( data );
    }

    /**
     * Create and configure a {@link ActionManager} from a specified
     * configuration resource.
     *
     * @param configuratorType the type of the configurator
     * @param resource the InputStream of the configuration resource
     * @return the configured ActionManager
     * @throws Exception if unable to create the ActionManager
     */
    public static ActionManager createActionManager( final String configuratorType,
                                                 final InputStream resource )
        throws Exception
    {
        final InitialActionManagerFactory factory = new InitialActionManagerFactory();
        final HashMap data = new HashMap();
        data.put( InitialActionManagerFactory.FACTORY, getFactoryClassName( configuratorType ) );
        data.put( InputStream.class.getName(), resource );
        return factory.createActionManager( data );
    }

    /**
     * Get the Factory class name of the ActionManagerFactory that corresponds
     * to specified type of Logger.
     *
     * @param type the type of Configurator 
     */
    private static String getFactoryClassName( final String type )
    {
        if( XML.equals( type ) )
        {
            return XMLActionManagerFactory.class.getName();
        }
        else
        {
            final String message = "Unknown type " + type;
            throw new IllegalArgumentException( message );
        }
    }
}
