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
package org.realityforge.nativekit;

import java.io.File;

/**
 * Utility methods for dealing with native paths.
 *
 * @author <a href="mailto:peter at realityforge.org">Peter Donald</a>
 * @author <a href="mailto:adammurdoch@apache.org">Adam Murdoch</a>
 * @version $Revision: 1.1 $ $Date: 2003-01-29 00:14:35 $
 */
public final class PathUtil
{
    /**
     * Formats a path into its native representation.
     */
    public static String formatPath( final String[] path )
    {
        // empty path return empty string
        if( path == null || path.length == 0 )
        {
            return "";
        }

        // path containing one or more elements
        final StringBuffer result = new StringBuffer( path[ 0 ].toString() );
        for( int i = 1; i < path.length; i++ )
        {
            result.append( File.pathSeparatorChar );
            result.append( path[ i ] );
        }

        return result.toString();
    }

    /**
     * Formats a path into its native representation.
     */
    public static String formatPath( final File[] path )
    {
        // empty path return empty string
        if( path == null || path.length == 0 )
        {
            return "";
        }

        // path containing one or more elements
        final StringBuffer result = new StringBuffer( path[ 0 ].toString() );
        for( int i = 1; i < path.length; i++ )
        {
            result.append( File.pathSeparatorChar );
            result.append( path[ i ].getAbsolutePath() );
        }

        return result.toString();
    }
}
