/*
 * Copyright (C) The Spice Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */
package org.realityforge.xsyslog.manager;

import org.apache.log.util.LoggerListener;
import org.apache.log.Logger;

/**
 * This class intercepts creation events from logger framework.
 *
 * @author <a href="mailto:peter at realityforge.org">Peter Donald</a>
 * @version $Revision: 1.1 $ $Date: 2003-06-27 03:27:51 $
 */
class SyslogLoggerListener
    extends LoggerListener
{
    /**
     * The SyslogManager listener is associated with.
     */
    private final SyslogManager m_syslogManager;

    /**
     * Create adapter for specified SyslogManager.
     *
     * @param syslogManager the SyslogManager
     */
    public SyslogLoggerListener( final SyslogManager syslogManager )
    {
        if( null == syslogManager )
        {
            throw new NullPointerException( "syslogManager" );
        }
        m_syslogManager = syslogManager;
    }

    /**
     * Route message via SyslogManager.
     *
     * @param channel the newly created channel name
     * @param logger the logger
     */
    public void loggerCreated( final String channel,
                               final Logger logger )
    {
        m_syslogManager.loggerCreated( channel, logger );
    }
}
