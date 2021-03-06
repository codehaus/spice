/*
 * Copyright (C) The Spice Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */
package org.codehaus.spice.salt.io;

import java.util.regex.Pattern;

/**
 * Utility class for scanning a filesystem and matching a particular set of
 * include and exclude patterns ala ant.
 *
 * @author Peter Donald
 * @version $Revision: 1.2 $ $Date: 2004-07-11 23:42:28 $
 */
public class PathMatcher
{
    /** The set of patterns that indicate paths to include. */
    private final Pattern[] m_includes;

    /**
     * The set of patterns that indicate paths to exclude. Takes precedence over
     * includes.
     */
    private final Pattern[] m_excludes;

    /** The character that separates elements in VPaths. */
    private final char m_separator;

    /**
     * Construct a matcher from ant style includes/excludes.
     *
     * @param includes the set of includes
     * @param excludes the set of excludes
     */
    public PathMatcher( final String[] includes,
                        final String[] excludes )
    {
        this( includes, excludes, '/' );
    }

    /**
     * Construct a matcher from ant style includes/excludes.
     *
     * @param includes the set of includes
     * @param excludes the set of excludes
     * @param separator the separator used to mark end of elements
     */
    public PathMatcher( final String[] includes,
                        final String[] excludes,
                        final char separator )
    {
        if( null == includes )
        {
            throw new NullPointerException( "includes" );
        }
        if( null == excludes )
        {
            throw new NullPointerException( "excludes" );
        }

        m_separator = separator;
        m_includes = toPatterns( includes );
        m_excludes = toPatterns( excludes );
    }

    /**
     * Test if a virtual path matches any of the ant style patterns specified in
     * this objects constructor.
     *
     * @param vPath the virtual path string
     * @return true if matches, false otherwise
     */
    public boolean match( final String vPath )
    {
        if( isExcluded( vPath ) )
        {
            return false;
        }
        else if( isIncluded( vPath ) )
        {
            return true;
        }
        else
        {
            return false;
        }
    }

    /**
     * Determine if specified path is excludes by patterns.
     *
     * @param vPath the virtual path
     * @return true if excluded
     */
    private boolean isExcluded( final String vPath )
    {
        return testMatch( m_excludes, vPath );
    }

    /**
     * Determine if specified path is includes by patterns.
     *
     * @param vPath the virtual path
     * @return true if included
     */
    private boolean isIncluded( final String vPath )
    {
        return testMatch( m_includes, vPath );
    }

    /**
     * Determine whether the virtual path matches specified patterns.
     *
     * @param patterns the patterns
     * @param vPath the virtual path
     * @return true if matches, false otherwise
     */
    private boolean testMatch( final Pattern[] patterns,
                               final String vPath )
    {
        for( int i = 0; i < patterns.length; i++ )
        {
            if( patterns[ i ].matcher( vPath ).matches() )
            {
                return true;
            }
        }
        return false;
    }

    /**
     * Convert a set of ant patterns into ORO Pattern objects.
     *
     * @param strs the ant style patterns
     * @return the ORO Pattern objects
     */
    private Pattern[] toPatterns( final String[] strs )
    {
        final Pattern[] patterns = new Pattern[ strs.length ];
        for( int i = 0; i < patterns.length; i++ )
        {
            final String perlPatternStr = toPerlPatternStr( strs[ i ] );
            patterns[ i ] = Pattern.compile( perlPatternStr );
        }
        return patterns;
    }

    /**
     * Convert an ant style fileset pattern to a Perl pattern.
     *
     * @param str the ant style pattern
     * @return the perl pattern
     */
    private String toPerlPatternStr( String str )
    {
        final StringBuffer sb = new StringBuffer();
        final int size = str.length();

        for( int i = 0; i < size; i++ )
        {
            final char ch = str.charAt( i );
            if( '.' == ch || m_separator == ch || '\\' == ch )
            {
                sb.append( '\\' );
                sb.append( ch );
            }
            else if( '*' == ch )
            {
                if( ( i + 2 ) < size &&
                    '*' == str.charAt( i + 1 ) &&
                    m_separator == str.charAt( i + 2 ) )
                {
                    sb.append( "([^\\" );
                    sb.append( m_separator );
                    sb.append( "]*\\" );
                    sb.append( m_separator );
                    sb.append( ")*" );
                    i += 2;
                }
                else if( ( i + 2 ) == size &&
                    '*' == str.charAt( i + 1 ) )
                {

                    sb.append( "([^\\" );
                    sb.append( m_separator );
                    sb.append( "]*\\" );
                    sb.append( m_separator );
                    sb.append( ")*" );
                    i += 1;
                }
                else
                {
                    sb.append( "[^\\" );
                    sb.append( m_separator );
                    sb.append( "]+" );
                }
            }
            else
            {
                sb.append( ch );
            }
        }

        return sb.toString();
    }
}
