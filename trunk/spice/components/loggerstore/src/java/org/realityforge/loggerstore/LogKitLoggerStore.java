/*
 * Copyright (C) The Spice Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */
package org.realityforge.loggerstore;

import org.apache.avalon.framework.logger.LogKitLogger;
import org.apache.avalon.framework.logger.Logger;
import org.apache.avalon.framework.configuration.Configuration;
import org.apache.log.Hierarchy;

/**
 * <p>LogKitLoggerStore extends AbstractLoggerStore to provide the implementation
 * specific to the LogKit logger. </p>
 *
 * @author <a href="mailto:mauro.talevi at aquilonia.org">Mauro Talevi</a>
 */
public class LogKitLoggerStore
    extends AbstractLoggerStore
{
    /** The Logger Hierarchy  */
    private Hierarchy m_hierarchy;

    /**
     * Creates a <code>LogKitLoggerStore</code> using the configuration resource
     *
     * @param resource the Configuration encoding the configuration resource
     * @throws Exception if fails to create or configure Logger
     */
    public LogKitLoggerStore( final Configuration resource )
        throws Exception
    {
        m_hierarchy = new Hierarchy();
        HierarchyUtil.configure( resource, m_hierarchy );
        setRootLogger( new LogKitLogger( m_hierarchy.getRootLogger() ) );
    }

    /**
     *  Creates new LogKitLogger for the given category.
     */
    protected Logger createLogger( final String categoryName )
    {
        return new LogKitLogger( m_hierarchy.getLoggerFor( categoryName ) );
    }

    /**
     *  Closes the LoggerStore and shuts down the logger hierarchy.
     */
    public void close()
    {
        HierarchyUtil.closeLogTargets( m_hierarchy );
    }

}
