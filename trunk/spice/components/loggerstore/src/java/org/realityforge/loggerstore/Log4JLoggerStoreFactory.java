/*
 * Copyright (C) The Spice Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */
package org.realityforge.loggerstore;

import java.io.InputStream;
import java.util.Map;
import java.util.Properties;
import org.w3c.dom.Element;

/**
 * Log4JLoggerStoreFactory is an implementation of LoggerStoreFactory
 * for the Log4J Logger.
 *
 * @author <a href="mailto:mauro.talevi at aquilonia.org">Mauro Talevi</a>
 * @author <a href="mailto:peter at realityforge.org">Peter Donald</a>
 * @version $Revision: 1.5 $ $Date: 2003-05-24 22:29:25 $
 */
public class Log4JLoggerStoreFactory
    extends AbstractLoggerStoreFactory
{
    /**
     * The CONFIGURATION_TYPE key.  Used to denote the type of configuration,
     * which can take value PROPERTIES or XML.
     */
    public static final String CONFIGURATION_TYPE = "configurationType";
    /**
     * The PROPERTIES bound value.
     */
    public static final String PROPERTIES = "properties";
    /**
     * The XML bound value.
     */
    public static final String XML = "xml";

    /**
     * Creates a LoggerStore from a given set of configuration parameters.
     * The configuration Map must contain:
     * <ol>
     * <li> <code>InputStream</code> object keyed on <code>LoggerStoreFactory.CONFIGURATION</code>
     * encoding the configuration resource</li>
     * <li> a <code>LoggerStoreFactory.CONFIGURATION_TYPE</code>
     * containing  the configuration type - either <code>LoggerStoreFactory.PROPERTIES</code>
     * or <code>LoggerStoreFactory.XML</code></li>
     * </ol>
     * @param config the Map of parameters for the configuration of the store
     * @return the LoggerStore
     * @throws Exception if unable to create the LoggerStore
     */
    protected LoggerStore doCreateLoggerStore( final Map config )
        throws Exception
    {
        final Element element = (Element)config.get( Element.class.getName() );
        if( null != element )
        {
            return new Log4JLoggerStore( element );
        }
        final Properties properties = (Properties)config.get( Properties.class.getName() );
        if( null != properties )
        {
            return new Log4JLoggerStore( properties );
        }

        final InputStream resource = getInputStream( config );
        if( null != resource )
        {
            final String type = (String)config.get( CONFIGURATION_TYPE );
            return createStoreFromStream( type, resource );
        }
        return missingConfiguration();
    }

    private LoggerStore createStoreFromStream( final String type,
                                               final InputStream resource )
        throws Exception
    {
        if( null == type || XML.equals( type ) )
        {
            return new Log4JLoggerStore( resource );
        }
        else
        {
            final Properties properties = new Properties();
            properties.load( resource );
            return new Log4JLoggerStore( properties );
        }
    }
}
