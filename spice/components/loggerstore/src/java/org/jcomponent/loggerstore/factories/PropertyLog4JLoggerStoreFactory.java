/*
 * Copyright (C) The Spice Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */
package org.jcomponent.loggerstore.factories;

import java.io.InputStream;
import java.util.Map;
import java.util.Properties;
import org.jcomponent.loggerstore.LoggerStore;
import org.jcomponent.loggerstore.stores.Log4JLoggerStore;

/**
 * PropertyLog4JLoggerStoreFactory is an implementation of LoggerStoreFactory
 * for the Log4J Logger using a property configuration resource.
 *
 * @author <a href="mailto:mauro.talevi at aquilonia.org">Mauro Talevi</a>
 * @author <a href="mailto:peter at realityforge.org">Peter Donald</a>
 * @version $Revision: 1.3 $ $Date: 2003-10-19 01:51:35 $
 */
public class PropertyLog4JLoggerStoreFactory
    extends AbstractLoggerStoreFactory
{
    /**
     * Creates a LoggerStore from a given set of configuration parameters.
     *
     * @param config the Map of parameters for the configuration of the store
     * @return the LoggerStore
     * @throws Exception if unable to create the LoggerStore
     */
    protected LoggerStore doCreateLoggerStore( final Map config )
        throws Exception
    {
        final Properties properties = (Properties)config.get( Properties.class.getName() );
        if( null != properties )
        {
            return new Log4JLoggerStore( properties );
        }

        final InputStream resource = getInputStream( config );
        if( null != resource )
        {
            return new Log4JLoggerStore( createPropertiesFromStream( resource ) );
        }

        return missingConfiguration();
    }

    private Properties createPropertiesFromStream( final InputStream resource )
        throws Exception
    {
        final Properties properties = new Properties();
        properties.load( resource );
        return properties;
    }
}
