/*
 * Copyright (C) The Spice Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */
package org.jcomponent.loggerstore.stores;

import org.jcontainer.dna.Logger;
import org.jcontainer.dna.impl.ConsoleLogger;

/**
 * ConsoleLoggerStore extends AbstractLoggerStore to provide the implementation
 * specific that just writes to console.
 *
 * @author <a href="mailto:mauro.talevi at aquilonia.org">Mauro Talevi</a>
 */
public class ConsoleLoggerStore
    extends AbstractLoggerStore
{
    /**
     * Creates a <code>ConsoleLoggerStore</code> using the specified Logger level.
     *
     * @param level the debug level of ConsoleLoggerStore
     * @throws Exception if fails to create or configure Logger
     */
    public ConsoleLoggerStore( final int level )
        throws Exception
    {
        setRootLogger( new ConsoleLogger( level ) );
    }

    /**
     *  Creates new ConsoleLogger for the given category.
     */
    protected Logger createLogger( final String name )
    {
        return getRootLogger().getChildLogger( name );
    }

    /**
     *  Closes the LoggerStore and shuts down the logger hierarchy.
     */
    public void close()
    {
    }
}
