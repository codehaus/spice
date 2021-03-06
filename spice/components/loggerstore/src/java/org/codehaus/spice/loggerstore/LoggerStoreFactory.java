/*
 * Copyright (C) The Spice Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */
package org.codehaus.spice.loggerstore;

import java.util.Map;

/**
 * <p>LoggerStoreFactory is a factory interface for LoggerStore instances. There
 * is a factory implementation for each specific logger implementation (LogKit,
 * Log4J, JDK14).
 *
 * <p>The factory also acts a configurator, handling the specific way in which a
 * logger is configured. The LoggerStore is configured via a map of parameters
 * passed in the create method.  LoggerStoreFactory defines the keys used to
 * retrieve the elements of the map.</p>
 *
 * @author Mauro Talevi
 * @author Peter Donald
 * @version $Revision: 1.3 $ $Date: 2004-02-28 21:13:23 $
 */
public interface LoggerStoreFactory
{
    /**
     * The URL key.  Used to define the URL where the configuration for
     * LoggerStore can be found.
     */
    String URL_LOCATION = "org.codehaus.spice.loggerstore.url";

    /**
     * The URL key.  Used to define the URL where the configuration for
     * LoggerStore can be found.
     */
    String FILE_LOCATION = "org.codehaus.spice.loggerstore.file";

    /**
     * Creates a LoggerStore from a given set of configuration parameters.
     *
     * @param config the Map of parameters for the configuration of the store
     * @return the LoggerStore
     * @throws Exception if unable to create the LoggerStore
     */
    LoggerStore createLoggerStore( Map config )
        throws Exception;
}
