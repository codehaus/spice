/*
 * Copyright (C) The Spice Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */
package org.realityforge.nativekit.impl;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import org.apache.avalon.framework.logger.AbstractLogEnabled;

/**
 * This class is responsible for monitoring a process.
 * It will monitor a process and if it goes longer than its timeout
 * then it will terminate it. The monitor will also read data from
 * stdout and stderr of process and pass it onto user specified streams.
 * It will also in the future do the same for stdin.
 *
 * @author <a href="mailto:peter at realityforge.org">Peter Donald</a>
 * @version $Revision: 1.1 $ $Date: 2003-01-29 00:14:35 $
 */
final class ProcessMonitor
    extends AbstractLogEnabled
    implements Runnable
{
    //Time to sleep in loop while processing output
    //of command and monitoring for timeout
    private static final int SLEEP_TIME = 5;

    //State to indicate process is still running
    private static final int STATE_RUNNING = 0;

    //State to indicate process shutdown by itself
    private static final int STATE_STOPPED = 1;

    //State to indicate process was terminated due to timeout
    private static final int STATE_TERMINATED = 2;

    /**
     * The state of the process monitor and thus
     * the state of the underlying process.
     */
    private int m_state = STATE_RUNNING;

    /**
     * This is the process we are monitoring.
     */
    private final Process m_process;

    /**
     * This specifies the time at which this process will
     * timeout. 0 implies no timeout.
     */
    private final long m_timeout;

    /**
     * Stream from which to read standard input.
     */
    private InputStream m_input;

    /**
     * Stream to write standard output to.
     */
    private final OutputStream m_output;

    /**
     * Stream to write standard error to.
     */
    private final OutputStream m_error;

    /**
     * Specifies whether the monitor should shutdown
     * input, output and error Streams when it finishes execution.
     * This should be set to <code>true</code> for processes which
     * will run asynchronously.
     */
    private final boolean m_shutdownStreams;

    /**
     * Creates a monitor for a given {@link java.lang.Process}, which pipes
     * input from the given input stream to the process, and pipes the process
     * output to the given OutputStreams.
     *
     * @param process the Process to be monitored
     * @param input is read into the Process' stdin
     * @param output receives the Process' stdout
     * @param error receives the Process' stderr
     * @param timeoutDuration how long to let the Process run before killing it.
     * @param shutdownStreams specifies if the monitor should shutdown the
     *             streams when the Process exits.
     */
    public ProcessMonitor( final Process process,
                           final InputStream input,
                           final OutputStream output,
                           final OutputStream error,
                           final long timeoutDuration,
                           final boolean shutdownStreams )
    {
        if( null == process )
        {
            throw new NullPointerException( "process" );
        }

        if( 0 > timeoutDuration )
        {
            throw new IllegalArgumentException( "timeoutDuration" );
        }

        final long now = System.currentTimeMillis();
        long timeout = 0;
        if( 0 != timeoutDuration )
        {
            timeout = now + timeoutDuration;
        }

        m_process = process;
        m_input = input;
        m_output = output;
        m_error = error;
        m_timeout = timeout;
        m_shutdownStreams = shutdownStreams;
    }

    /**
     * Utility method to check if process timed out.
     * Only valid after run() has exited.
     */
    public boolean didProcessTimeout()
    {
        return ( m_state == STATE_TERMINATED );
    }

    /**
     * Thread method to monitor the state of the process.
     */
    public void run()
    {
        while( STATE_RUNNING == m_state )
        {
            processStreams();

            if( !isProcessStopped() )
            {
                checkTimeout();
            }

            try
            {
                Thread.sleep( SLEEP_TIME );
            }
            catch( final InterruptedException ie )
            {
                //swallow it
            }
        }

        //Process streams again to make sure
        //that we have got all the data
        processStreams();

        cleanupStreams();
    }

    /**
     * Utility method which cleans up all IO Streams, if required.
     */
    private void cleanupStreams()
    {
        if( m_shutdownStreams )
        {
            shutdownStream( m_input );
            shutdownStream( m_output );
            shutdownStream( m_error );
        }
    }

    /**
     * Utility method to process all the standard streams.
     */
    private void processStreams()
    {
        processStandardInput();
        processStandardOutput();
        processStandardError();
    }

    /**
     * Check if process has stopped. If it has then update state
     * and return true, else return false.
     */
    private boolean isProcessStopped()
    {
        boolean stopped;
        try
        {
            m_process.exitValue();
            stopped = true;
        }
        catch( final IllegalThreadStateException itse )
        {
            stopped = false;
        }

        if( stopped )
        {
            m_state = STATE_STOPPED;
        }

        return stopped;
    }

    /**
     * Check if the process has exceeded time allocated to it.
     * If it has reached timeout then terminate the process
     * and set state to <code>STATE_TERMINATED</code>.
     */
    private void checkTimeout()
    {
        if( 0 == m_timeout )
        {
            return;
        }

        final long now = System.currentTimeMillis();
        if( now > m_timeout )
        {
            m_state = STATE_TERMINATED;
            m_process.destroy();
        }
    }

    /**
     * Process the standard input of process.
     * Reading it from user specified stream and copy it
     * to processes standard input stream.
     */
    private void processStandardInput()
    {
        if( null != m_input )
        {
            //Note can not do this as the process may block
            //when written to which will result in this whole
            //thread being blocked. Probably need to write to
            //stdin in another thread
            //copy( m_input, m_process.getOutputStream() );

            //Should we shutdown the processes input stream ?
            //Why not - at least for now
            shutdownStream( m_process.getOutputStream() );

            shutdownStream( m_input );
            m_input = null;
        }
    }

    /**
     * Process the standard output of process.
     * Reading it and sending it to user specified stream
     * or into the void.
     */
    private void processStandardOutput()
    {
        final InputStream input = m_process.getInputStream();
        copy( input, m_output );
    }

    /**
     * Process the standard error of process.
     * Reading it and sending it to user specified stream
     * or into the void.
     */
    private void processStandardError()
    {
        final InputStream input = m_process.getErrorStream();
        copy( input, m_error );
    }

    /**
     * Copy data from specified input stream to output stream if
     * output stream exists. The size of data that should be attempted
     * to read is determined by calling available() on input stream.
     */
    private void copy( final InputStream input,
                       final OutputStream output )
    {
        try
        {
            final int available = input.available();
            if( 0 >= available )
            {
                return;
            }

            final byte[] data = new byte[ available ];
            final int read = input.read( data );

            if( null != output )
            {
                output.write( data, 0, read );
            }
        }
        catch( final IOException ioe )
        {
            final String message = "Error processing streams";
            getLogger().error( message, ioe );
        }
    }

    /**
     * Unconditionally close an <code>InputStream</code>.
     * Equivalent to {@link InputStream#close()}, except any exceptions will be ignored.
     * @param output A (possibly null) InputStream
     */
    private static void shutdownStream( final OutputStream output )
    {
        if( null == output )
        {
            return;
        }

        try
        {
            output.close();
        }
        catch( final IOException ioe )
        {
        }
    }

    /**
     * Unconditionally close an <code>InputStream</code>.
     * Equivalent to {@link InputStream#close()}, except any exceptions will be ignored.
     * @param input A (possibly null) InputStream
     */
    private static void shutdownStream( final InputStream input )
    {
        if( null == input )
        {
            return;
        }

        try
        {
            input.close();
        }
        catch( final IOException ioe )
        {
        }
    }
}
