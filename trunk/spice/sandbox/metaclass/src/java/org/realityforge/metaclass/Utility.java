/*
 * Copyright (C) The Spice Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */
package org.realityforge.metaclass;

import java.util.Vector;

/**
 *
 * @author <a href="mailto:doug at doug@stocksoftware.com.au">Doug Hagan</a>
 * @version $Revision: 1.1 $ $Date: 2003-04-16 10:40:45 $
 */
public final class Utility
{
    /**
     * Compares contents of an array with contents of another array.
     * Return true if collections are of equal size
     * and each member of original is found in other.
     * @param original
     * @param other
     * @return result
     */
    public static boolean areContentsEqual( final Object[] original,
                                            final Object[] other )
    {
        if( original.length != other.length )
        {
            return false;
        }
        boolean result = true;
        for( int i = 0; i < original.length &&
            result == true; i++ )
        {
            final Object originalElement = original[ i ];
            final Object otherElement = other[ i ];

            if( null == originalElement )
            {
                if( null != otherElement )
                {
                    result = false;
                }
            }
            else
            {
                if( !originalElement.equals( otherElement ) )
                {
                    result = false;
                }
            }
        }
        return result;
    }

    /**
     * Compares contents of an array with contents of a vector.
     * Returns true if collections are of equal size
     * and each member of original is contained by other.
     * @param original
     * @param other
     * @return result
     */
    public static boolean areContentsEqual( final Object[] original,
                                            final Vector other )
    {
        if( original.length != other.size() )
        {
            return false;
        }

        for( int i = 0; i < original.length; i++ )
        {
            final Object originalElement = original[ i ];
            if( !other.contains( originalElement ) )
            {
                return false;
            }
        }
        return true;
    }

}
