/*
 * Copyright (C) The Spice Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 *
 * Portions of this software are based upon software originally
 * developed as part of the Apache Myrmidon project under
 * the Apache 1.1 License.
 */
package org.realityforge.nativekit.impl;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Properties;
import org.realityforge.nativekit.ExecException;
import org.realityforge.nativekit.ExecManager;
import org.realityforge.nativekit.ExecMetaData;
import org.realityforge.nativekit.ExecOutputHandler;
import org.realityforge.nativekit.Os;
import org.realityforge.nativekit.impl.launchers.CommandLauncher;
import org.realityforge.nativekit.impl.launchers.DefaultCommandLauncher;
import org.realityforge.nativekit.impl.launchers.MacCommandLauncher;
import org.realityforge.nativekit.impl.launchers.ScriptCommandLauncher;
import org.realityforge.nativekit.impl.launchers.WinNTCommandLauncher;

/**
 * Default implementation of <code>ExecManager</code>.
 * Used to run processes in the ant environment.
 *
 * @author <a href="mailto:peter at realityforge.org">Peter Donald</a>
 * @author <a href="mailto:thomas.haas@softwired-inc.com">Thomas Haas</a>
 * @version $Revision: 1.1 $ $Date: 2003-01-29 00:14:35 $
 * @see ExecManager
 * @see ExecMetaData
 * @see Environment
 */
public final class DefaultExecManager
    implements ExecManager
{
    /**
     * Used to destroy processes when the VM exits.
     */
    private final ProcessDestroyer m_processDestroyer = new ProcessDestroyer();

    private final CommandLauncher m_launcher;
    private final CommandLauncher m_shellLauncher;

    /**
     * Utility class that is used to load and parse the native
     * environment variables.
     */
    private final Environment m_environment;

    public DefaultExecManager( final File homeDir )
        throws ExecException
    {
        m_launcher = new DefaultCommandLauncher();
        m_shellLauncher = createShellLauncher( homeDir );
        m_environment = new Environment( this );
    }

    /**
     * Retrieve a properties object that contains a list of
     * all the native environment variables.
     */
    public Properties getNativeEnvironment()
        throws ExecException
    {
        try
        {
            return m_environment.getNativeEnvironment();
        }
        catch( final IOException ioe )
        {
            throw new ExecException( ioe.getMessage(), ioe );
        }
    }

    /**
     * Execute a process and wait for it to finish before
     * returning.
     */
    public int execute( final ExecMetaData execMetaData,
                        final ExecOutputHandler handler,
                        long timeout )
        throws IOException, ExecException /*TimeoutException*/
    {
        final LogOutputStream output = new LogOutputStream( handler, false );
        final LogOutputStream error = new LogOutputStream( handler, true );
        try
        {
            return execute( execMetaData, null, output, error, timeout );
        }
        finally
        {
            try
            {
                output.close();
            }
            catch( final IOException ioe )
            {
            }
            try
            {
                error.close();
            }
            catch( final IOException ioe )
            {
            }
        }
    }

    /**
     * Execute a process and wait for it to finish before
     * returning.
     */
    public int execute( final ExecMetaData command,
                        final InputStream input,
                        final OutputStream output,
                        final OutputStream error,
                        final long timeout )
        throws IOException, ExecException
    {
        final CommandLauncher launcher = getLauncher( command );
        final Process process = launcher.exec( command );
        final ProcessMonitor monitor =
            new ProcessMonitor( process, input, output, error, timeout, false );

        final Thread thread = new Thread( monitor, "ProcessMonitor" );
        thread.start();

        // add the process to the list of those to destroy if the VM exits
        m_processDestroyer.add( process );

        waitFor( process );

        //Now wait for monitor to finish aswell
        try
        {
            thread.join();
        }
        catch( InterruptedException e )
        {
            //should never occur.
        }

        // remove the process to the list of those to destroy if the VM exits
        m_processDestroyer.remove( process );

        if( monitor.didProcessTimeout() )
        {
            throw new ExecException( "Process Timed out" );
        }

        return process.exitValue();
    }

    private void waitFor( final Process process )
    {
        //Should loop around until process is terminated.
        try
        {
            process.waitFor();
        }
        catch( final InterruptedException ie )
        {
            //should never happen
        }
    }

    private CommandLauncher getLauncher( final ExecMetaData metaData )
    {
        CommandLauncher launcher = m_launcher;
        if( false ) //!m_useVMLauncher )
        {
            launcher = m_shellLauncher;
        }
        return launcher;
    }

    private CommandLauncher createShellLauncher( final File homeDir )
    {
        CommandLauncher launcher;

        if( Os.isFamily( Os.OS_FAMILY_MAC ) )
        {
            // Mac
            launcher = new MacCommandLauncher();
        }
        else if( Os.isFamily( Os.OS_FAMILY_OS2 ) )
        {
            // OS/2 - use same mechanism as Windows 2000
            launcher = new WinNTCommandLauncher();
        }
        else if( Os.isFamily( Os.OS_FAMILY_WINNT ) )
        {
            // Windows 2000/NT
            launcher = new WinNTCommandLauncher();
        }
        else if( Os.isFamily( Os.OS_FAMILY_WINDOWS ) )
        {
            // Windows 98/95 - need to use an auxiliary script
            final String script = resolveCommand( homeDir, "bin/antRun.bat" );
            launcher = new ScriptCommandLauncher( script );
        }
        else if( Os.isFamily( Os.OS_FAMILY_NETWARE ) )
        {
            // NetWare.  Need to determine which JDK we're running in
            final String perlScript = resolveCommand( homeDir, "bin/antRun.pl" );
            final String[] script = new String[]{"perl", perlScript};
            launcher = new ScriptCommandLauncher( script );
        }
        else
        {
            // Generic
            final String script = resolveCommand( homeDir, "bin/antRun" );
            launcher = new ScriptCommandLauncher( script );
        }

        return launcher;
    }

    private String resolveCommand( final File antDir, final String command )
    {
        return resolveFile( antDir, command ).toString();
    }

    /**
     * Resolve a file <code>filename</code> to it's canonical form. If <code>filename</code> is
     * relative (doesn't start with <code>/</code>), it will be resolved relative to
     * <code>baseFile</code>, otherwise it is treated as a normal root-relative path.
     *
     * @param baseFile Where to resolve <code>filename</code> from, if <code>filename</code> is
     * relative.
     * @param filename Absolute or relative file path to resolve.
     * @return The canonical <code>File</code> of <code>filename</code>.
     */
    private static File resolveFile( final File baseFile, String filename )
    {
        String filenm = filename;
        if( '/' != File.separatorChar )
        {
            filenm = filename.replace( '/', File.separatorChar );
        }

        if( '\\' != File.separatorChar )
        {
            filenm = filename.replace( '\\', File.separatorChar );
        }

        // deal with absolute files
        if( filenm.startsWith( File.separator ) )
        {
            File file = new File( filenm );

            try
            {
                file = file.getCanonicalFile();
            }
            catch( final IOException ioe )
            {
            }

            return file;
        }
        // FIXME: I'm almost certain this // removal is unnecessary, as getAbsoluteFile() strips
        // them. However, I'm not sure about this UNC stuff. (JT)
        final char[] chars = filename.toCharArray();
        final StringBuffer sb = new StringBuffer();

        //remove duplicate file separators in succession - except
        //on win32 at start of filename as UNC filenames can
        //be \\AComputer\AShare\myfile.txt
        int start = 0;
        if( '\\' == File.separatorChar )
        {
            sb.append( filenm.charAt( 0 ) );
            start++;
        }

        for( int i = start; i < chars.length; i++ )
        {
            final boolean doubleSeparator =
                File.separatorChar == chars[ i ] && File.separatorChar == chars[ i - 1 ];

            if( !doubleSeparator )
            {
                sb.append( chars[ i ] );
            }
        }

        filenm = sb.toString();

        //must be relative
        File file = ( new File( baseFile, filenm ) ).getAbsoluteFile();

        try
        {
            file = file.getCanonicalFile();
        }
        catch( final IOException ioe )
        {
        }

        return file;
    }
}
