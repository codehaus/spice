/*
 * Copyright (C) The Spice Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */
package org.jcomponent.loggerstore.factories;

import java.util.Map;
import org.jcomponent.loggerstore.LoggerStore;
import org.jcomponent.loggerstore.stores.ConsoleLoggerStore;
import org.jcontainer.dna.impl.ConsoleLogger;

/**
 * This is a basic factory for ConsoleLoggerStore.
 *
 * @author <a href="mailto:peter at realityforge.org">Peter Donald</a>
 * @version $Revision: 1.3 $ $Date: 2003-10-19 01:51:35 $
 */
public class ConsoleLoggerStoreFactory
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
        final int level = ConsoleLogger.LEVEL_INFO;
        return new ConsoleLoggerStore( level );
    }
}
