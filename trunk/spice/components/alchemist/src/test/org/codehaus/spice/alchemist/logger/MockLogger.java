/*
 * Copyright (C) The Spice Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */
package org.codehaus.spice.alchemist.logger;

import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

class MockLogger
    extends Logger
{
    boolean m_output;
    Level m_priority;
    String m_message;
    Throwable m_throwable;

    public MockLogger( final Level level )
    {
        super( "test", null );
        setLevel( level );
    }

    public void log( final LogRecord record )
    {
        m_output = true;
        m_priority = record.getLevel();
        m_message = record.getMessage();
        m_throwable = record.getThrown();
    }
}
