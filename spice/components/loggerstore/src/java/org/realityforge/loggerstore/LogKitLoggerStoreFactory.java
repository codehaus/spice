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
import org.apache.avalon.framework.configuration.Configuration;
import org.apache.avalon.framework.configuration.DefaultConfigurationBuilder;

/**
 * LogKitLoggerStoreFactory is an implementation of LoggerStoreFactory
 * for the LogKit Logger.
 *
 * @author <a href="mailto:mauro.talevi at aquilonia.org">Mauro Talevi</a>
 * @author <a href="mailto:peter at realityforge.org">Peter Donald</a>
 * @version $Revision: 1.6 $ $Date: 2003-05-24 22:53:12 $
 */
public class LogKitLoggerStoreFactory
    extends AbstractLoggerStoreFactory
{
    /**
     * Creates a LoggerStore from a given set of configuration parameters.
     * The configuration Map must contain:
     * <ol>
     * <li> <code>InputStream</code> object keyed on <code>LoggerStoreFactory.CONFIGURATION</code>
     * encoding the configuration resource</li>
     * <li> a <code>LoggerStoreFactory.CONFIGURATION_TYPE</code>
     * containing  the configuration type - currently only <code>LoggerStoreFactory.XML</code>
     * is supported
     * </li>
     * </ol>
     *
     * @param config the Map of parameters for the configuration of the store
     * @return the LoggerStore
     * @throws Exception if unable to create the LoggerStore
     */
    protected LoggerStore doCreateLoggerStore( final Map config )
        throws Exception
    {
        /*
        final Element element = (Element)config.get( Element.class.getName() );
        if( null != element )
        {
            return new LogKitLoggerStore( ConfigurationUtil.toConfiguration( element ) );
        }
        */
        final Configuration configuration =
            (Configuration)config.get( Configuration.class.getName() );
        if( null != configuration )
        {
            return new LogKitLoggerStore( configuration );
        }

        final InputStream resource = getInputStream( config );
        if( null != resource )
        {
            final DefaultConfigurationBuilder builder = new DefaultConfigurationBuilder();
            return new LogKitLoggerStore( builder.build( resource ) );
        }

        return missingConfiguration();
    }
}
