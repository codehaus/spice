/*
 * Copyright (C) The Spice Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */
package org.realityforge.xinvoke.spi.impl;

import org.apache.oro.text.perl.Perl5Util;

/**
 *
 *
 * @author <a href="mailto:peter at www.stocksoftware.com.au">Peter Donald</a>
 * @version $Revision: 1.1 $ $Date: 2003-01-29 00:14:37 $
 */
public class MatcherUtil
{
    public static final Perl5Util m_perl5Util = new Perl5Util();

    /**
     * Utility method that performs pattern matching.
     *
     * @param pattern the pattern
     * @param name the input to check
     * @return true if matches, false otherwise
     */
    public static boolean match( final String pattern,
                                 final String name )
    {
        return m_perl5Util.match( pattern, name );
    }
}
