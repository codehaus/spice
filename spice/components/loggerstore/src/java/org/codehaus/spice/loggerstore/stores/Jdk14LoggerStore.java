/*
 * Copyright (C) The Spice Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */
package org.codehaus.spice.loggerstore.stores;

import java.io.InputStream;
import java.util.logging.LogManager;
import org.jcontainer.dna.Logger;
import org.jcontainer.dna.impl.Jdk14Logger;

/**
 * Jdk14LoggerStore extends AbstractLoggerStore to provide the implementation
 * specific to the JDK14 logger.
 *
 * @author <a href="mailto:mauro.talevi at aquilonia.org">Mauro Talevi</a>
 */
public class Jdk14LoggerStore
    extends AbstractLoggerStore
{
    /** The LogManager repository */
    private final LogManager m_manager;

    /**
     * Creates a <code>Log4JLoggerStore</code> using the configuration resource.
     *
     * @param resource the InputStream encoding the configuration resource
     * @throws Exception if fails to create or configure Logger
     */
    public Jdk14LoggerStore( final InputStream resource )
        throws Exception
    {
        m_manager = LogManager.getLogManager();
        m_manager.readConfiguration( resource );
        setRootLogger( new Jdk14Logger( m_manager.getLogger( "global" ) ) );
    }

    /**
     *  Creates new Jdk14Logger for the given category.
     */
    protected Logger createLogger( final String name )
    {
        return new Jdk14Logger( java.util.logging.Logger.getLogger( name ) );
    }

    /**
     *  Closes the LoggerStore and shuts down the logger hierarchy.
     */
    public void close()
    {
        m_manager.reset();
    }
}
