/*
 * Copyright (C) The Spice Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 *
 * This product includes software developed by the
 * Apache Software Foundation (http://www.apache.org/).
 */
package org.realityforge.cli;

/**
 * Token handles tokenizing the CLI arguments
 *
 * @author <a href="mailto:peter at apache.org">Peter Donald</a>
 * @author <a href="mailto:bloritsch@apache.org">Berin Loritsch</a>
 * @version $Revision: 1.2 $ $Date: 2003-03-17 18:39:43 $
 * @since 4.0
 */
class Token
{
    /** Type for a separator token */
    public static final int TOKEN_SEPARATOR = 0;
    /** Type for a text token */
    public static final int TOKEN_STRING = 1;

    private final int m_type;
    private final String m_value;

    /**
     * New Token object with a type and value
     */
    Token( final int type, final String value )
    {
        m_type = type;
        m_value = value;
    }

    /**
     * Get the value of the token
     */
    final String getValue()
    {
        return m_value;
    }

    /**
     * Get the type of the token
     */
    final int getType()
    {
        return m_type;
    }

    /**
     * Convert to a string
     */
    public final String toString()
    {
        final StringBuffer sb = new StringBuffer();
        sb.append( m_type );
        sb.append( ":" );
        sb.append( m_value );
        return sb.toString();
    }
}
