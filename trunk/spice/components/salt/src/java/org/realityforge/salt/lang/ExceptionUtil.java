/*
 * Copyright (C) The Spice Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */
package org.realityforge.salt.lang;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.Method;
import java.util.StringTokenizer;

/**
 * This class makes it easy to manipulate data stored in exceptions.
 *
 * @author <a href="mailto:peter at apache.org">Peter Donald</a>
 * @version $Revision: 1.4 $ $Date: 2003-06-12 22:44:01 $
 */
public final class ExceptionUtil
{
    /**
     * Constant for the current line property.
     */
    private static final String LINE_SEPARATOR = System.getProperty( "line.separator" );

    /**
     * Constant for name of method to lookup to get cause of exception.
     */
    private static final String GET_CAUSE_NAME = "getCause";

    /**
     * Constant for method parameter types used to lookup to get cause of exception.
     */
    private static final Class[] GET_CAUSE_PARAMTYPES = new Class[ 0 ];

    /**
     * Constant that used to separate causes when recursively printing exceptions.
     * Matches JDK1.4s separator.
     */
    private static final String CAUSED_BY = "Caused by: ";

    /**
     * Generate string for specified exception and the cause of
     * this exception (if any).
     * @param throwable a <code>Throwable</code>
     * @return the stack trace as a <code>String</code>
     */
    public static String printStackTrace( final Throwable throwable )
    {
        return printStackTrace( throwable, 0, true );
    }

    /**
     * Generate string for specified exception and if printCascading
     * is true will print all cascading exceptions.
     * @param throwable a <code>Throwable</code>
     * @param printCascading if <code>true</code> will print all cascading exceptions
     * @return the stack trace as a <code>String</code>
     */
    public static String printStackTrace( final Throwable throwable,
                                          final boolean printCascading )
    {
        return printStackTrace( throwable, 0, printCascading );
    }

    /**
     * Serialize the specified <code>Throwable</code> to a string.
     * Restrict the number of frames printed out to the specified depth.
     * If the depth specified is <code>0</code> then all the frames are
     * converted into a string.
     * @param throwable a <code>Throwable</code>
     * @param depth number of stack trace frames to show
     * @return the stack trace as a <code>String</code>
     */
    public static String printStackTrace( final Throwable throwable, final int depth )
    {
        int actualDepth = depth;
        final String[] lines = captureStackTrace( throwable );

        if( 0 == actualDepth || actualDepth > lines.length )
        {
            actualDepth = lines.length;
        }

        final StringBuffer sb = new StringBuffer();

        for( int i = 0; i < actualDepth; i++ )
        {
            sb.append( lines[ i ] );
            sb.append( LINE_SEPARATOR );
        }

        return sb.toString();
    }

    /**
     * Generate exception string for specified exception to specified depth
     * and all Cascading exceptions if printCascading is true. If useReflection
     * is true then the method will also attempt to use reflection to find a
     * method with signature <code>Throwable getCause()</code>. This makes
     * it compatible with JDK1.4 mechanisms for nesting exceptions.
     *
     * @param throwable a <code>Throwable</code>
     * @param depth number of stack trace frames to show
     * @param printCascading if <code>true</code> will print the causes of all exceptions
     * @return the stack trace as a <code>String</code>
     */
    public static String printStackTrace( final Throwable throwable,
                                          final int depth,
                                          final boolean printCascading )
    {
        final String result = printStackTrace( throwable, depth );

        if( !printCascading )
        {
            return result;
        }
        else
        {
            final StringBuffer sb = new StringBuffer();
            sb.append( result );

            Throwable cause = getCause( throwable );
            while( null != cause )
            {
                sb.append( CAUSED_BY );
                sb.append( printStackTrace( cause, depth ) );

                cause = getCause( cause );
            }

            return sb.toString();
        }
    }

    /**
     * Utility method to get thr root cause of an exception.
     *
     * @param throwable a <code>Throwable</code>
     * @return cause of specified exception
     */
    public static Throwable getRootCause( final Throwable throwable )
    {
        Throwable candidate = throwable;
        Throwable cause = getCause( throwable );
        while( null != cause )
        {
            candidate = cause;
            cause = getCause( candidate );
        }
        return candidate;
    }

    /**
     * Utility method to get cause of exception.
     *
     * @param throwable a <code>Throwable</code>
     * @return cause of specified exception
     */
    public static Throwable getCause( final Throwable throwable )
    {
        try
        {
            final Class clazz = throwable.getClass();
            final Method method =
                clazz.getMethod( GET_CAUSE_NAME, GET_CAUSE_PARAMTYPES );
            return (Throwable)method.invoke( throwable, null );
        }
        catch( final Throwable t )
        {
            return null;
        }
    }

    /**
     * Captures the stack trace associated with this exception.
     *
     * @param throwable a <code>Throwable</code>
     * @return an array of Strings describing stack frames.
     */
    public static String[] captureStackTrace( final Throwable throwable )
    {
        final StringWriter sw = new StringWriter();
        throwable.printStackTrace( new PrintWriter( sw, true ) );
        final String[] lines = splitLines( sw.toString() );
        for( int i = 0; i < lines.length; i++ )
        {
            if( lines[ i ].startsWith( "Caused by: " ) )
            {
                final String[] result = new String[ i ];
                System.arraycopy( lines, 0, result, 0, i );
                return result;
            }
        }
        return lines;
    }

    /**
     * Splits the string into lines.
     *
     * @param string the string to split
     * @return the lines in string
     */
    private static String[] splitLines( final String string )
    {
        final StringTokenizer tokenizer = new StringTokenizer( string, LINE_SEPARATOR );
        final String[] result = new String[ tokenizer.countTokens() ];

        for( int i = 0; i < result.length; i++ )
        {
            result[ i ] = tokenizer.nextToken();
        }

        return result;
    }
}
