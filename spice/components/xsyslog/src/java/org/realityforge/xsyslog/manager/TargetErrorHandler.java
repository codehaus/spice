/*
 * Copyright (C) The Spice Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */
package org.realityforge.xsyslog.manager;

import org.apache.avalon.framework.logger.AbstractLogEnabled;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXParseException;

/**
 * An error handler that prints out error for a particular
 * destinations config.
 *
 * @author <a href="mailto:peter at realityforge.org">Peter Donald</a>
 * @version $Revision: 1.1 $ $Date: 2003-04-16 10:56:13 $
 */
class TargetErrorHandler
    extends AbstractLogEnabled
    implements ErrorHandler
{
    /**
     * The name of destination.
     */
    private final String m_name;

    /**
     * The type of destination.
     */
    private final String m_type;

    /**
     * Create error handler for specified destination.
     *
     * @param name the name of destination
     * @param type the type of destination
     */
    TargetErrorHandler( final String name, final String type )
    {
        m_name = name;
        m_type = type;
    }

    /**
     * Handle error while validating configuration.
     *
     * @param exception the exception
     */
    public void warning( final SAXParseException exception )
    {
        if( getLogger().isWarnEnabled() )
        {
            getLogger().warn( "Target named " + m_name + " of type " + m_type +
                              " could not have its config validated due to: " + exception,
                              exception );
        }
    }

    /**
     * Handle error while validating configuration.
     *
     * @param exception the exception
     */
    public void error( final SAXParseException exception )
    {
        if( getLogger().isWarnEnabled() )
        {
            getLogger().warn( "Target named " + m_name + " of type " + m_type +
                              " could not have its config validated due to: " + exception,
                              exception );
        }
    }

    /**
     * Handle error while validating configuration.
     *
     * @param exception the exception
     */
    public void fatalError( final SAXParseException exception )
    {
        if( getLogger().isWarnEnabled() )
        {
            getLogger().warn( "Target named " + m_name + " of type " + m_type +
                              " could not have its config validated due to: " + exception,
                              exception );
        }
    }
}
