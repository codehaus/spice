/*
 * Copyright (C) The Spice Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */
package org.codehaus.spice.salt.io;

import junit.framework.TestCase;

/**
 * @author Peter Donald
 * @version $Revision: 1.2 $ $Date: 2004-07-11 23:42:28 $
 */
public class PathMatcherTestCase
    extends TestCase
{
    private static final String FULL_PATTERN = "**/*";
    private static final String STAR_PATTERN = "*";
    private static final String STAR_END_PATTERN = "pre/*";
    private static final String STAR_START_PATTERN = "*/post";
    private static final String STAR_MID_PATTERN = "pre/*/post";

    private static final String DSTAR_PATTERN = "**";
    private static final String DSTAR_END_PATTERN = "pre/**";
    private static final String DSTAR_START_PATTERN = "**/post";
    private static final String DSTAR_MID_PATTERN = "pre/**/post";

    private static final String INVALID_PATTERN = "&(-!";
    private static final String INVALID_CAUSE = "org.apache.oro.text.regex.MalformedPatternException: Unmatched parentheses.";

    private static final String DATA1 = "X";
    private static final String DATA2 = "pre/X";
    private static final String DATA3 = "X/post";
    private static final String DATA4 = "pre/X/post";
    private static final String DATA5 = "pre/X/Y/post";
    private static final String DATA6 = "/";
    private static final String DATA7 = "pre/X/";
    private static final String DATA8 = "pre/";
    private static final String DATA9 = "post";
    private static final String DATA10 = "s/post";
    private static final String DATA11 = "pre/post";

    public PathMatcherTestCase( final String name )
    {
        super( name );
    }

    public void testIncludedAndExcluded()
    {
        doTestPattern( FULL_PATTERN, STAR_PATTERN, DATA1, false );
        doTestPattern( FULL_PATTERN, STAR_PATTERN, DATA2, true );
        doTestPattern( FULL_PATTERN, STAR_PATTERN, DATA3, true );
        doTestPattern( FULL_PATTERN, STAR_PATTERN, DATA4, true );
        doTestPattern( FULL_PATTERN, STAR_PATTERN, DATA5, true );
        doTestPattern( FULL_PATTERN, STAR_PATTERN, DATA6, false );
        doTestPattern( FULL_PATTERN, STAR_PATTERN, DATA7, false );
        doTestPattern( FULL_PATTERN, STAR_PATTERN, DATA8, false );
        doTestPattern( FULL_PATTERN, STAR_PATTERN, DATA9, false );
        doTestPattern( FULL_PATTERN, STAR_PATTERN, DATA10, true );
        doTestPattern( FULL_PATTERN, STAR_PATTERN, DATA11, true );

        doTestPattern( DSTAR_END_PATTERN, DSTAR_MID_PATTERN, DATA1, false );
        doTestPattern( DSTAR_END_PATTERN, DSTAR_MID_PATTERN, DATA2, false );
        doTestPattern( DSTAR_END_PATTERN, DSTAR_MID_PATTERN, DATA3, false );
        doTestPattern( DSTAR_END_PATTERN, DSTAR_MID_PATTERN, DATA4, false );
        doTestPattern( DSTAR_END_PATTERN, DSTAR_MID_PATTERN, DATA5, false );
        doTestPattern( DSTAR_END_PATTERN, DSTAR_MID_PATTERN, DATA6, false );
        doTestPattern( DSTAR_END_PATTERN, DSTAR_MID_PATTERN, DATA7, true );
        doTestPattern( DSTAR_END_PATTERN, DSTAR_MID_PATTERN, DATA8, true );
        doTestPattern( DSTAR_END_PATTERN, DSTAR_MID_PATTERN, DATA9, false );
        doTestPattern( DSTAR_END_PATTERN, DSTAR_MID_PATTERN, DATA10, false );
        doTestPattern( DSTAR_END_PATTERN, DSTAR_MID_PATTERN, DATA11, false );
    }

    public void testMatchers()
    {
        doTestIncludePattern( FULL_PATTERN, DATA1, true );
        doTestIncludePattern( FULL_PATTERN, DATA2, true );
        doTestIncludePattern( FULL_PATTERN, DATA3, true );
        doTestIncludePattern( FULL_PATTERN, DATA4, true );
        doTestIncludePattern( FULL_PATTERN, DATA5, true );
        doTestIncludePattern( FULL_PATTERN, DATA6, false );
        doTestIncludePattern( FULL_PATTERN, DATA7, false );
        doTestIncludePattern( FULL_PATTERN, DATA8, false );
        doTestIncludePattern( FULL_PATTERN, DATA9, true );
        doTestIncludePattern( FULL_PATTERN, DATA10, true );
        doTestIncludePattern( FULL_PATTERN, DATA11, true );

        doTestIncludePattern( STAR_PATTERN, DATA1, true );
        doTestIncludePattern( STAR_PATTERN, DATA2, false );
        doTestIncludePattern( STAR_PATTERN, DATA3, false );
        doTestIncludePattern( STAR_PATTERN, DATA4, false );
        doTestIncludePattern( STAR_PATTERN, DATA5, false );
        doTestIncludePattern( STAR_PATTERN, DATA6, false );
        doTestIncludePattern( STAR_PATTERN, DATA7, false );
        doTestIncludePattern( STAR_PATTERN, DATA8, false );
        doTestIncludePattern( STAR_PATTERN, DATA9, true );
        doTestIncludePattern( STAR_PATTERN, DATA10, false );
        doTestIncludePattern( STAR_PATTERN, DATA11, false );

        doTestIncludePattern( STAR_END_PATTERN, DATA1, false );
        doTestIncludePattern( STAR_END_PATTERN, DATA2, true );
        doTestIncludePattern( STAR_END_PATTERN, DATA3, false );
        doTestIncludePattern( STAR_END_PATTERN, DATA4, false );
        doTestIncludePattern( STAR_END_PATTERN, DATA5, false );
        doTestIncludePattern( STAR_END_PATTERN, DATA6, false );
        doTestIncludePattern( STAR_END_PATTERN, DATA7, false );
        doTestIncludePattern( STAR_END_PATTERN, DATA8, false );
        doTestIncludePattern( STAR_END_PATTERN, DATA9, false );
        doTestIncludePattern( STAR_END_PATTERN, DATA10, false );
        doTestIncludePattern( STAR_END_PATTERN, DATA11, true );

        doTestIncludePattern( STAR_START_PATTERN, DATA1, false );
        doTestIncludePattern( STAR_START_PATTERN, DATA2, false );
        doTestIncludePattern( STAR_START_PATTERN, DATA3, true );
        doTestIncludePattern( STAR_START_PATTERN, DATA4, false );
        doTestIncludePattern( STAR_START_PATTERN, DATA5, false );
        doTestIncludePattern( STAR_START_PATTERN, DATA6, false );
        doTestIncludePattern( STAR_START_PATTERN, DATA7, false );
        doTestIncludePattern( STAR_START_PATTERN, DATA8, false );
        doTestIncludePattern( STAR_START_PATTERN, DATA9, false );
        doTestIncludePattern( STAR_START_PATTERN, DATA10, true );
        doTestIncludePattern( STAR_START_PATTERN, DATA11, true );

        doTestIncludePattern( STAR_MID_PATTERN, DATA1, false );
        doTestIncludePattern( STAR_MID_PATTERN, DATA2, false );
        doTestIncludePattern( STAR_MID_PATTERN, DATA3, false );
        doTestIncludePattern( STAR_MID_PATTERN, DATA4, true );
        doTestIncludePattern( STAR_MID_PATTERN, DATA5, false );
        doTestIncludePattern( STAR_MID_PATTERN, DATA6, false );
        doTestIncludePattern( STAR_MID_PATTERN, DATA7, false );
        doTestIncludePattern( STAR_MID_PATTERN, DATA8, false );
        doTestIncludePattern( STAR_MID_PATTERN, DATA9, false );
        doTestIncludePattern( STAR_MID_PATTERN, DATA10, false );
        doTestIncludePattern( STAR_MID_PATTERN, DATA11, false );

        doTestIncludePattern( DSTAR_PATTERN, DATA1, false );
        doTestIncludePattern( DSTAR_PATTERN, DATA2, false );
        doTestIncludePattern( DSTAR_PATTERN, DATA3, false );
        doTestIncludePattern( DSTAR_PATTERN, DATA4, false );
        doTestIncludePattern( DSTAR_PATTERN, DATA5, false );
        doTestIncludePattern( DSTAR_PATTERN, DATA6, true );
        doTestIncludePattern( DSTAR_PATTERN, DATA7, true );
        doTestIncludePattern( DSTAR_PATTERN, DATA8, true );
        doTestIncludePattern( DSTAR_PATTERN, DATA9, false );
        doTestIncludePattern( DSTAR_PATTERN, DATA10, false );
        doTestIncludePattern( DSTAR_PATTERN, DATA11, false );

        doTestIncludePattern( DSTAR_END_PATTERN, DATA1, false );
        doTestIncludePattern( DSTAR_END_PATTERN, DATA2, false );
        doTestIncludePattern( DSTAR_END_PATTERN, DATA3, false );
        doTestIncludePattern( DSTAR_END_PATTERN, DATA4, false );
        doTestIncludePattern( DSTAR_END_PATTERN, DATA5, false );
        doTestIncludePattern( DSTAR_END_PATTERN, DATA6, false );
        doTestIncludePattern( DSTAR_END_PATTERN, DATA7, true );
        doTestIncludePattern( DSTAR_END_PATTERN, DATA8, true );
        doTestIncludePattern( DSTAR_END_PATTERN, DATA9, false );
        doTestIncludePattern( DSTAR_END_PATTERN, DATA10, false );
        doTestIncludePattern( DSTAR_END_PATTERN, DATA11, false );

        doTestIncludePattern( DSTAR_START_PATTERN, DATA1, false );
        doTestIncludePattern( DSTAR_START_PATTERN, DATA2, false );
        doTestIncludePattern( DSTAR_START_PATTERN, DATA3, true );
        doTestIncludePattern( DSTAR_START_PATTERN, DATA4, true );
        doTestIncludePattern( DSTAR_START_PATTERN, DATA5, true );
        doTestIncludePattern( DSTAR_START_PATTERN, DATA6, false );
        doTestIncludePattern( DSTAR_START_PATTERN, DATA7, false );
        doTestIncludePattern( DSTAR_START_PATTERN, DATA8, false );
        doTestIncludePattern( DSTAR_START_PATTERN, DATA9, true );
        doTestIncludePattern( DSTAR_START_PATTERN, DATA10, true );
        doTestIncludePattern( DSTAR_START_PATTERN, DATA11, true );

        doTestIncludePattern( DSTAR_MID_PATTERN, DATA1, false );
        doTestIncludePattern( DSTAR_MID_PATTERN, DATA2, false );
        doTestIncludePattern( DSTAR_MID_PATTERN, DATA3, false );
        doTestIncludePattern( DSTAR_MID_PATTERN, DATA4, true );
        doTestIncludePattern( DSTAR_MID_PATTERN, DATA5, true );
        doTestIncludePattern( DSTAR_MID_PATTERN, DATA6, false );
        doTestIncludePattern( DSTAR_MID_PATTERN, DATA7, false );
        doTestIncludePattern( DSTAR_MID_PATTERN, DATA8, false );
        doTestIncludePattern( DSTAR_MID_PATTERN, DATA9, false );
        doTestIncludePattern( DSTAR_MID_PATTERN, DATA10, false );
        doTestIncludePattern( DSTAR_MID_PATTERN, DATA11, true );
    }

    private void doTestPattern( final String includes,
                                final String excludes,
                                final String data,
                                final boolean result )
    {
        final PathMatcher pathMatcher =
            new PathMatcher( new String[]{includes},
                             new String[]{excludes} );
        assertEquals( "Pattern match on data " +
                      data +
                      " with includes " +
                      includes +
                      " and excludes " + excludes,
                      result,
                      pathMatcher.match( data ) );

        final String data2 = data.replace( '/', '.' );
        final String includes2 = includes.replace( '/', '.' );
        final String excludes2 = excludes.replace( '/', '.' );
        final PathMatcher pathMatcher2 =
            new PathMatcher( new String[]{includes2},
                             new String[]{excludes2},
                             '.' );
        assertEquals( "Pattern match on data " +
                      data2 +
                      " with includes " +
                      includes2 +
                      " and excludes " +
                      excludes2 +
                      " using '.' separator",
                      result,
                      pathMatcher2.match( data2 ) );
    }

    private void doTestIncludePattern( final String pattern,
                                       final String data,
                                       final boolean result )
    {
        final PathMatcher pathMatcher =
            new PathMatcher( new String[]{pattern}, new String[ 0 ] );
        assertEquals( "Pattern match of " + pattern + " on " + data,
                      result,
                      pathMatcher.match( data ) );
        final String data2 = data.replace( '/', '.' );
        final String pattern2 = pattern.replace( '/', '.' );
        final PathMatcher pathMatcher2 =
            new PathMatcher( new String[]{pattern2},
                             new String[ 0 ],
                             '.' );
        assertEquals( "Pattern match on data " +
                      data2 +
                      " with includes " +
                      pattern2 +
                      " using '.' separator",
                      result,
                      pathMatcher2.match( data2 ) );
    }

    public void testNullIncludes()
    {
        try
        {
            new PathMatcher( null, new String[ 0 ] );
            fail( "Expected NPE due to null includes" );
        }
        catch( NullPointerException npe )
        {
            assertEquals( "NPE message", "includes", npe.getMessage() );
        }
    }

    public void testNullExcludes()
    {
        try
        {
            new PathMatcher( new String[ 0 ], null );
            fail( "Expected NPE due to null excludes" );
        }
        catch( NullPointerException npe )
        {
            assertEquals( "NPE message", "excludes", npe.getMessage() );
        }
    }

    public void testInvalidIncludes()
    {
        try
        {
            new PathMatcher( new String[]{INVALID_PATTERN},
                             new String[ 0 ] );
            fail( "Expected Exception due to invalid includes" );
        }
        catch( IllegalArgumentException e )
        {
            return;
        }
    }

    public void testInvalidExcludes()
    {
        try
        {
            new PathMatcher( new String[ 0 ],
                             new String[]{INVALID_PATTERN} );
            fail( "Expected Exception due to invalid excludes" );
        }
        catch( IllegalArgumentException e )
        {
            return;
        }
    }

}
