/*
 * Copyright (C) The Spice Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */
package org.codehaus.spice.alchemist.logger;

import java.util.logging.Level;
import junit.framework.TestCase;
import org.apache.avalon.framework.logger.Jdk14Logger;

public class DNALoggerTestCase
    extends TestCase
{
    private MockLogger m_mockLogger;

    public void testLoggerEmptyCtor()
        throws Exception
    {
        try
        {
            new DNALogger( null );
        }
        catch( NullPointerException npe )
        {
            assertEquals( "npe.getMessage()", "logger", npe.getMessage() );
        }
    }

    public void testLoggerGetChildLogger()
        throws Exception
    {
        final DNALogger logger = createLogger( Level.FINE );

        assertNotSame( "logger.getChildLogger == logger",
                       logger,
                       logger.getChildLogger( "whatever" ) );
    }

    public void testLoggerTraceEnabled()
        throws Exception
    {
        final Level level = Level.ALL;
        final Level type = Level.FINE;
        final String message = "Meep!";
        final Throwable throwable = null;
        final boolean output = true;

        final DNALogger logger = createLogger( level );
        logger.trace( message );
        checkLogger( output, message, throwable, type );
    }

    public void testLoggerTraceDisabled()
        throws Exception
    {
        final Level level = Level.OFF;
        final String message = "Meep!";

        final DNALogger logger = createLogger( level );
        logger.trace( message );
        checkLogger( false, null, null, null );
    }

    public void testLoggerTraceWithExceptionEnabled()
        throws Exception
    {
        final Level level = Level.ALL;
        final Level type = Level.FINE;
        final String message = "Meep!";
        final Throwable throwable = new Throwable();
        final boolean output = true;

        final DNALogger logger = createLogger( level );

        logger.trace( message, throwable );
        checkLogger( output, message, throwable, type );
    }

    public void testLoggerTraceWithExceptionDisabled()
        throws Exception
    {
        final Level level = Level.OFF;
        final String message = "Meep!";
        final Throwable throwable = new Throwable();

        final DNALogger logger = createLogger( level );

        logger.trace( message, throwable );
        checkLogger( false, null, null, null );
    }

    public void testLoggerDebugEnabled()
        throws Exception
    {
        final Level level = Level.ALL;
        final Level type = Level.FINE;
        final String message = "Meep!";
        final Throwable throwable = null;
        final boolean output = true;

        final DNALogger logger = createLogger( level );
        logger.debug( message );
        checkLogger( output, message, throwable, type );
    }

    public void testLoggerDebugDisabled()
        throws Exception
    {
        final Level level = Level.OFF;
        final String message = "Meep!";

        final DNALogger logger = createLogger( level );
        logger.debug( message );
        checkLogger( false, null, null, null );
    }

    public void testLoggerDebugWithExceptionEnabled()
        throws Exception
    {
        final Level level = Level.ALL;
        final Level type = Level.FINE;
        final String message = "Meep!";
        final Throwable throwable = new Throwable();
        final boolean output = true;

        final DNALogger logger = createLogger( level );
        logger.debug( message, throwable );
        checkLogger( output, message, throwable, type );
    }

    public void testLoggerDebugWithExceptionDisabled()
        throws Exception
    {
        final Level level = Level.OFF;
        final String message = "Meep!";
        final Throwable throwable = new Throwable();

        final DNALogger logger = createLogger( level );
        logger.debug( message, throwable );
        checkLogger( false, null, null, null );
    }

    public void testLoggerInfoEnabled()
        throws Exception
    {
        final Level level = Level.ALL;
        final Level type = Level.INFO;
        final String message = "Meep!";
        final Throwable throwable = null;
        final boolean output = true;

        final DNALogger logger = createLogger( level );
        logger.info( message );
        checkLogger( output, message, throwable, type );
    }

    public void testLoggerInfoDisabled()
        throws Exception
    {
        final Level level = Level.OFF;
        final String message = "Meep!";

        final DNALogger logger = createLogger( level );
        logger.info( message );
        checkLogger( false, null, null, null );
    }

    public void testLoggerInfoWithExceptionEnabled()
        throws Exception
    {
        final Level level = Level.ALL;
        final Level type = Level.INFO;
        final String message = "Meep!";
        final Throwable throwable = new Throwable();
        final boolean output = true;

        final DNALogger logger = createLogger( level );
        logger.info( message, throwable );
        checkLogger( output, message, throwable, type );
    }

    public void testLoggerInfoWithExceptionDisabled()
        throws Exception
    {
        final Level level = Level.OFF;
        final String message = "Meep!";
        final Throwable throwable = new Throwable();

        final DNALogger logger = createLogger( level );
        logger.info( message, throwable );
        checkLogger( false, null, null, null );
    }

    public void testLoggerWarnEnabled()
        throws Exception
    {
        final Level level = Level.ALL;
        final Level type = Level.WARNING;
        final String message = "Meep!";
        final Throwable throwable = null;
        final boolean output = true;

        final DNALogger logger = createLogger( level );
        logger.warn( message );
        checkLogger( output, message, throwable, type );
    }

    public void testLoggerWarnDisabled()
        throws Exception
    {
        final Level level = Level.OFF;
        final String message = "Meep!";

        final DNALogger logger = createLogger( level );
        logger.warn( message );
        checkLogger( false, null, null, null );
    }

    public void testLoggerWarnWithExceptionEnabled()
        throws Exception
    {
        final Level level = Level.ALL;
        final Level type = Level.WARNING;
        final String message = "Meep!";
        final Throwable throwable = new Throwable();
        final boolean output = true;

        final DNALogger logger = createLogger( level );
        logger.warn( message, throwable );
        checkLogger( output, message, throwable, type );
    }

    public void testLoggerWarnWithExceptionDisabled()
        throws Exception
    {
        final Level level = Level.OFF;
        final String message = "Meep!";
        final Throwable throwable = new Throwable();

        final DNALogger logger = createLogger( level );
        logger.warn( message, throwable );
        checkLogger( false, null, null, null );
    }

    public void testLoggerErrorEnabled()
        throws Exception
    {
        final Level level = Level.ALL;
        final Level type = Level.SEVERE;
        final String message = "Meep!";
        final Throwable throwable = null;
        final boolean output = true;

        final DNALogger logger = createLogger( level );
        logger.error( message );
        checkLogger( output, message, throwable, type );
    }

    public void testLoggerErrorWithExceptionEnabled()
        throws Exception
    {
        final Level level = Level.ALL;
        final Level type = Level.SEVERE;
        final String message = "Meep!";
        final Throwable throwable = new Throwable();
        final boolean output = true;

        final DNALogger logger = createLogger( level );
        logger.error( message, throwable );
        checkLogger( output, message, throwable, type );
    }

    public void testConsoleLevelComparisonWithDebugEnabled()
        throws Exception
    {
        final DNALogger logger = createLogger( Level.FINEST );

        assertEquals( "logger.isTraceEnabled()", true, logger.isTraceEnabled() );
        assertEquals( "logger.isDebugEnabled()", true, logger.isDebugEnabled() );
        assertEquals( "logger.isInfoEnabled()", true, logger.isInfoEnabled() );
        assertEquals( "logger.isWarnEnabled()", true, logger.isWarnEnabled() );
        assertEquals( "logger.isErrorEnabled()", true, logger.isErrorEnabled() );
    }

    public void testConsoleLevelComparisonWithInfoEnabled()
        throws Exception
    {
        final DNALogger logger = createLogger( Level.INFO );

        assertEquals( "logger.isTraceEnabled()", false, logger.isTraceEnabled() );
        assertEquals( "logger.isDebugEnabled()", false, logger.isDebugEnabled() );
        assertEquals( "logger.isInfoEnabled()", true, logger.isInfoEnabled() );
        assertEquals( "logger.isWarnEnabled()", true, logger.isWarnEnabled() );
        assertEquals( "logger.isErrorEnabled()", true, logger.isErrorEnabled() );
    }

    public void testConsoleLevelComparisonWithWarnEnabled()
        throws Exception
    {
        final DNALogger logger = createLogger( Level.WARNING );

        assertEquals( "logger.isTraceEnabled()", false, logger.isTraceEnabled() );
        assertEquals( "logger.isDebugEnabled()", false, logger.isDebugEnabled() );
        assertEquals( "logger.isInfoEnabled()", false, logger.isInfoEnabled() );
        assertEquals( "logger.isWarnEnabled()", true, logger.isWarnEnabled() );
        assertEquals( "logger.isErrorEnabled()", true, logger.isErrorEnabled() );
    }

    public void testConsoleLevelComparisonWithErrorEnabled()
        throws Exception
    {
        final DNALogger logger = createLogger( Level.SEVERE );

        assertEquals( "logger.isTraceEnabled()", false, logger.isTraceEnabled() );
        assertEquals( "logger.isDebugEnabled()", false, logger.isDebugEnabled() );
        assertEquals( "logger.isInfoEnabled()", false, logger.isInfoEnabled() );
        assertEquals( "logger.isWarnEnabled()", false, logger.isWarnEnabled() );
        assertEquals( "logger.isErrorEnabled()", true, logger.isErrorEnabled() );
    }


    private DNALogger createLogger( final Level priority )
    {
        m_mockLogger = new MockLogger( priority );
        return new DNALogger( new Jdk14Logger( m_mockLogger ) );
    }

    private void checkLogger( final boolean output,
                              final String message,
                              final Throwable throwable,
                              final Level priority )
    {
        assertEquals( "logger.m_message == message", message, m_mockLogger.m_message );
        assertEquals( "logger.m_output == output", output, m_mockLogger.m_output );
        assertEquals( "logger.m_throwable == throwable", throwable, m_mockLogger.m_throwable );
        assertEquals( "logger.m_priority == priority", priority, m_mockLogger.m_priority );
    }
}
