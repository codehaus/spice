/*
 * Copyright (C) The Spice Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */
package org.realityforge.salt.lang;

import junit.framework.TestCase;

/**
 *
 * @author <a href="mailto:peter at realityforge.org">Peter Donald</a>
 * @version $Revision: 1.4 $ $Date: 2003-06-12 22:46:36 $
 */
public class ExceptionUtilTestCase
    extends TestCase
{
    private static final String NL = "\n";

    private static final String TRACE1_LINE1 = "org.realityforge.salt.lang.MockThrowable: s1";
    private static final String TRACE1_LINE2 = "   at org.realityforge.CallerClass.callerMethod1( CallerClass.java:1 )";
    private static final String TRACE1_LINE3 = "   at org.realityforge.CallerClass.callerMethod2( CallerClass.java:2 )";
    private static final String TRACE1_LINE4 = "   at org.realityforge.CallerClass.callerMethod3( CallerClass.java:3 )";
    private static final String TRACE1_LINE5 = "   at org.realityforge.CallerClass.callerMethod4( CallerClass.java:4 )";
    private static final String TRACE1_LINE6 = "   at org.realityforge.CallerClass.callerMethod5( CallerClass.java:5 )";
    private static final String TRACE1_LINE7 = "   at org.realityforge.CallerClass.callerMethod6( CallerClass.java:6 )";
    private static final String TRACE1_LINE8 = "   at org.realityforge.CallerClass.callerMethod7( CallerClass.java:7 )";
    private static final String TRACE1_LINE9 = "   at org.realityforge.CallerClass.callerMethod8( CallerClass.java:8 )";
    private static final String TRACE1 =
        TRACE1_LINE1 + NL + TRACE1_LINE2 + NL + TRACE1_LINE3 + NL + TRACE1_LINE4 + NL +
        TRACE1_LINE5 + NL + TRACE1_LINE6 + NL + TRACE1_LINE7 + NL + TRACE1_LINE8 + NL +
        TRACE1_LINE9;

    private static final String TRACE2_LINE1 = "org.realityforge.salt.lang.MockThrowable: s2";
    private static final String TRACE2_LINE2 = "   at org.realityforge.CallerClass2.callerMethod1( CallerClass2.java:1 )";
    private static final String TRACE2_LINE3 = "   at org.realityforge.CallerClass2.callerMethod2( CallerClass2.java:2 )";
    private static final String TRACE2_LINE4 = "   at org.realityforge.CallerClass2.callerMethod3( CallerClass2.java:3 )";
    private static final String TRACE2_LINE5 = "   at org.realityforge.CallerClass2.callerMethod4( CallerClass2.java:4 )";
    private static final String TRACE2_LINE6 = "   at org.realityforge.CallerClass2.callerMethod5( CallerClass2.java:5 )";
    private static final String TRACE2_LINE7 = "   at org.realityforge.CallerClass2.callerMethod6( CallerClass2.java:6 )";
    private static final String TRACE2_LINE8 = "   at org.realityforge.CallerClass2.callerMethod7( CallerClass2.java:7 )";
    private static final String TRACE2_LINE9 = "   at org.realityforge.CallerClass2.callerMethod8( CallerClass2.java:8 )";
    private static final String TRACE2 =
        TRACE2_LINE1 + NL + TRACE2_LINE2 + NL + TRACE2_LINE3 + NL + TRACE2_LINE4 + NL +
        TRACE2_LINE5 + NL + TRACE2_LINE6 + NL + TRACE2_LINE7 + NL + TRACE2_LINE8 + NL +
        TRACE2_LINE9;

    private static final String TRACE3_LINE1 = "org.realityforge.salt.lang.MockThrowable: s2";
    private static final String TRACE3_LINE2 = "   at org.realityforge.CallerClass3.callerMethod1( CallerClass3.java:1 )";
    private static final String TRACE3_LINE3 = "   at org.realityforge.CallerClass3.callerMethod2( CallerClass3.java:2 )";
    private static final String TRACE3_LINE4 = "   at org.realityforge.CallerClass3.callerMethod3( CallerClass3.java:3 )";
    private static final String TRACE3_LINE5 = "   at org.realityforge.CallerClass3.callerMethod4( CallerClass3.java:4 )";
    private static final String TRACE3_LINE6 = "   at org.realityforge.CallerClass3.callerMethod5( CallerClass3.java:5 )";
    private static final String TRACE3_LINE7 = "   at org.realityforge.CallerClass3.callerMethod6( CallerClass3.java:6 )";
    private static final String TRACE3_LINE8 = "   at org.realityforge.CallerClass3.callerMethod7( CallerClass3.java:7 )";
    private static final String TRACE3_LINE9 = "   at org.realityforge.CallerClass3.callerMethod8( CallerClass3.java:8 )";
    private static final String TRACE3 =
        TRACE3_LINE1 + NL + TRACE3_LINE2 + NL + TRACE3_LINE3 + NL + TRACE3_LINE4 + NL +
        TRACE3_LINE5 + NL + TRACE3_LINE6 + NL + TRACE3_LINE7 + NL + TRACE3_LINE8 + NL +
        TRACE3_LINE9;

    public ExceptionUtilTestCase( final String name )
    {
        super( name );
    }

    public void testRetrievalOfNullCause()
    {
        final MockThrowable throwable = new MockThrowable( "s1", null, TRACE1 );
        assertNull( "getCause", ExceptionUtil.getCause( throwable ) );
    }

    public void testRetrievalOfCauseOneLevelDeep()
    {
        final MockThrowable throwable2 = new MockThrowable( "s2", null, TRACE2 );
        final MockThrowable throwable = new MockThrowable( "s1", throwable2, TRACE1 );
        assertEquals( "getCause", throwable2, ExceptionUtil.getCause( throwable ) );
    }

    public void testRetrievalOfCauseManyLevelsDeep()
    {
        final MockThrowable throwable3 = new MockThrowable( "s3", null, TRACE3 );
        final MockThrowable throwable2 = new MockThrowable( "s2", throwable3, TRACE2 );
        final MockThrowable throwable = new MockThrowable( "s1", throwable2, TRACE1 );
        assertEquals( "getCause", throwable2, ExceptionUtil.getCause( throwable ) );
    }

    public void testRetrievalOfRootCauseOnLeafException()
    {
        final MockThrowable throwable = new MockThrowable( "s1", null, TRACE1 );
        assertEquals( "getRootCause", throwable, ExceptionUtil.getRootCause( throwable ) );
    }

    public void testRetrievalOfRootCauseOnException1Deep()
    {
        final MockThrowable throwable2 = new MockThrowable( "s2", null, TRACE2 );
        final MockThrowable throwable = new MockThrowable( "s1", throwable2, TRACE1 );
        assertEquals( "getRootCause", throwable2, ExceptionUtil.getRootCause( throwable ) );
    }

    public void testRetrievalOfRootCauseOnExceptionManyDeep()
    {
        final MockThrowable throwable3 = new MockThrowable( "s3", null, TRACE3 );
        final MockThrowable throwable2 = new MockThrowable( "s2", throwable3, TRACE2 );
        final MockThrowable throwable = new MockThrowable( "s1", throwable2, TRACE1 );
        assertEquals( "getRootCause", throwable3, ExceptionUtil.getRootCause( throwable ) );
    }

    public void testCaptureStackTraceOnLeafException()
    {
        final MockThrowable throwable = new MockThrowable( "s1", null, TRACE1 );
        final String[] trace = ExceptionUtil.captureStackTrace( throwable );
        assertEquals( "trace.length", 9, trace.length );
        assertEquals( "trace[0]", TRACE1_LINE1, trace[ 0 ] );
        assertEquals( "trace[1]", TRACE1_LINE2, trace[ 1 ] );
        assertEquals( "trace[2]", TRACE1_LINE3, trace[ 2 ] );
        assertEquals( "trace[3]", TRACE1_LINE4, trace[ 3 ] );
        assertEquals( "trace[4]", TRACE1_LINE5, trace[ 4 ] );
        assertEquals( "trace[5]", TRACE1_LINE6, trace[ 5 ] );
        assertEquals( "trace[6]", TRACE1_LINE7, trace[ 6 ] );
        assertEquals( "trace[7]", TRACE1_LINE8, trace[ 7 ] );
        assertEquals( "trace[8]", TRACE1_LINE9, trace[ 8 ] );
    }

    public void testCaptureStackTraceOnOneLevelDeepException()
    {
        final MockThrowable throwable2 = new MockThrowable( "s2", null, TRACE2 );
        final MockThrowable throwable = new MockThrowable( "s1", throwable2, TRACE1 );
        final String[] trace = ExceptionUtil.captureStackTrace( throwable );
        assertEquals( "trace.length", 9, trace.length );
        assertEquals( "trace[0]", TRACE1_LINE1, trace[ 0 ] );
        assertEquals( "trace[1]", TRACE1_LINE2, trace[ 1 ] );
        assertEquals( "trace[2]", TRACE1_LINE3, trace[ 2 ] );
        assertEquals( "trace[3]", TRACE1_LINE4, trace[ 3 ] );
        assertEquals( "trace[4]", TRACE1_LINE5, trace[ 4 ] );
        assertEquals( "trace[5]", TRACE1_LINE6, trace[ 5 ] );
        assertEquals( "trace[6]", TRACE1_LINE7, trace[ 6 ] );
        assertEquals( "trace[7]", TRACE1_LINE8, trace[ 7 ] );
        assertEquals( "trace[8]", TRACE1_LINE9, trace[ 8 ] );
    }

    public void testCaptureStackTraceOnMultiLevelDeepException()
    {
        final MockThrowable throwable3 = new MockThrowable( "s3", null, TRACE3 );
        final MockThrowable throwable2 = new MockThrowable( "s2", throwable3, TRACE2 );
        final MockThrowable throwable = new MockThrowable( "s1", throwable2, TRACE1 );
        final String[] trace = ExceptionUtil.captureStackTrace( throwable );
        assertEquals( "trace.length", 9, trace.length );
        assertEquals( "trace[0]", TRACE1_LINE1, trace[ 0 ] );
        assertEquals( "trace[1]", TRACE1_LINE2, trace[ 1 ] );
        assertEquals( "trace[2]", TRACE1_LINE3, trace[ 2 ] );
        assertEquals( "trace[3]", TRACE1_LINE4, trace[ 3 ] );
        assertEquals( "trace[4]", TRACE1_LINE5, trace[ 4 ] );
        assertEquals( "trace[5]", TRACE1_LINE6, trace[ 5 ] );
        assertEquals( "trace[6]", TRACE1_LINE7, trace[ 6 ] );
        assertEquals( "trace[7]", TRACE1_LINE8, trace[ 7 ] );
        assertEquals( "trace[8]", TRACE1_LINE9, trace[ 8 ] );
    }

    public void testCaptureStackTraceOnOneLevelDeepExceptionssss()
    {
        //Caused by: ja
    }
}
