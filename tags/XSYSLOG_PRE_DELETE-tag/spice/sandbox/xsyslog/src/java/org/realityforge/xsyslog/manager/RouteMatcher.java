/*
 * Copyright (C) The Spice Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */
package org.realityforge.xsyslog.manager;

import org.apache.oro.text.regex.MalformedPatternException;
import org.apache.oro.text.regex.Pattern;
import org.apache.oro.text.regex.Perl5Compiler;
import org.apache.oro.text.regex.Perl5Matcher;

/**
 * Utility class for matching a particular set of
 * include and exclude patterns against a channel.
 *
 * @author <a href="mailto:peter at apache.org">Peter Donald</a>
 * @version $Revision: 1.1 $ $Date: 2003-06-27 03:27:51 $
 */
class RouteMatcher
{
    /**
     * The set of patterns that indicate paths to include.
     */
    private final Pattern[] m_includes;

    /**
     * The set of patterns that indicate paths to exclude.
     * Takes precedence over includes.
     */
    private final Pattern[] m_excludes;

    /**
     * The object that is capable of performing the matching.
     */
    private final Perl5Matcher m_matcher = new Perl5Matcher();

    /**
     * Construct a matcher from ant style includes/excludes.
     *
     * @param includes the set of includes
     * @param excludes the set of excludes
     */
    public RouteMatcher( final String[] includes,
                         final String[] excludes )
    {
        if( null == includes )
        {
            throw new NullPointerException( "includes" );
        }
        if( null == excludes )
        {
            throw new NullPointerException( "excludes" );
        }

        m_includes = toPatterns( includes );
        m_excludes = toPatterns( excludes );
    }

    /**
     * Test if a channel matches any of the ant style
     * patterns specified in this objects constructor.
     *
     * @param channel the channel name
     * @return true if matches, false otherwise
     */
    public boolean match( final String channel )
    {
        if( isExcluded( channel ) )
        {
            return false;
        }
        else if( isIncluded( channel ) )
        {
            return true;
        }
        else
        {
            return false;
        }
    }

    /**
     * Determine if specified channel is excluded by patterns.
     *
     * @param channel the channel
     * @return true if excluded
     */
    private boolean isExcluded( final String channel )
    {
        return testMatch( m_excludes, channel );
    }

    /**
     * Determine if specified channel is included by patterns.
     *
     * @param channel the channel
     * @return true if included
     */
    private boolean isIncluded( final String channel )
    {
        return testMatch( m_includes, channel );
    }

    /**
     * Determine whether the channel matches
     * specified patterns.
     *
     * @param patterns the patterns
     * @param channel the channel
     * @return true if matches, false otherwise
     */
    private boolean testMatch( final Pattern[] patterns,
                               final String channel )
    {
        for( int i = 0; i < patterns.length; i++ )
        {
            if( m_matcher.matches( channel, patterns[ i ] ) )
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
        final Perl5Compiler compiler = new Perl5Compiler();
        final Pattern[] patterns = new Pattern[ strs.length ];
        for( int i = 0; i < patterns.length; i++ )
        {
            final String perlPatternStr = toPerlPatternStr( strs[ i ] );
            try
            {
                patterns[ i ] = compiler.compile( perlPatternStr );
            }
            catch( final MalformedPatternException mpe )
            {
                throw new IllegalArgumentException( mpe.toString() );
            }
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
            if( '.' == ch || '/' == ch || '\\' == ch )
            {
                sb.append( '\\' );
                sb.append( ch );
            }
            else if( '*' == ch )
            {
                if( ( i + 2 ) < size &&
                    '*' == str.charAt( i + 1 ) &&
                    '.' == str.charAt( i + 2 ) )
                {
                    sb.append( "([^\\.]*\\.)*" );
                    i += 2;
                }
                else
                {
                    sb.append( "[^\\.]*" );
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
