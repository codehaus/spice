/*
 * Copyright (C) The Spice Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */
package org.realityforge.nativekit.impl;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import org.realityforge.nativekit.ExecOutputHandler;

/**
 * Logs each line written to this stream to the specified
 * <code>ExecOutputHandler</code>. Tries to be smart about
 * line separators.
 *
 * @author <a href="mailto:peter at realityforge.org">Peter Donald</a>
 * @version $Revision: 1.1 $ $Date: 2003-04-16 10:31:48 $
 */
final class LogOutputStream
    extends OutputStream
{
    private final boolean m_isError;
    private final ExecOutputHandler m_handler;

    private final ByteArrayOutputStream m_buffer = new ByteArrayOutputStream();
    private boolean m_skip;

    public LogOutputStream( final ExecOutputHandler handler,
                            final boolean isError )
    {
        m_handler = handler;
        m_isError = isError;
    }

    /**
     * Writes all remaining
     */
    public void close()
        throws IOException
    {
        if( m_buffer.size() > 0 )
        {
            processBuffer();
        }
        super.close();
    }

    /**
     * Write the data to the buffer and flush the buffer, if a line separator is
     * detected.
     *
     * @param ch data to log (byte).
     */
    public void write( final int ch )
        throws IOException
    {
        if( ( ch == '\n' ) || ( ch == '\r' ) )
        {
            if( !m_skip )
            {
                processBuffer();
            }
        }
        else
        {
            m_buffer.write( (byte)ch );
        }

        m_skip = ( ch == '\r' );
    }

    /**
     * Converts the buffer to a string and sends it to <code>ExecOutputHandler</code>
     */
    private void processBuffer()
    {
        final String line = m_buffer.toString();
        if( m_isError )
        {
            m_handler.stderr( line );
        }
        else
        {
            m_handler.stdout( line );
        }
        m_buffer.reset();
    }
}