/*
 * Copyright (C) The Spice Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */
package org.realityforge.jndikit;

import java.io.Serializable;
import java.util.Properties;
import javax.naming.CompoundName;
import javax.naming.Name;
import javax.naming.NameParser;
import javax.naming.NamingException;

/**
 * A simple cas sensitive name parser that reads left
 * to right with '/' as separator.
 *
 * @author <a href="mailto:peter@apache.org">Peter Donald</a>
 * @version $Revision: 1.2 $ $Date: 2003-02-12 11:00:06 $
 */ 
public class DefaultNameParser
    implements Serializable, NameParser
{
    private static Properties c_syntax = new Properties();

    static
    {
        c_syntax.put( "jndi.syntax.direction", "left_to_right" );
        c_syntax.put( "jndi.syntax.ignorecase", "false" );
        c_syntax.put( "jndi.syntax.separator", "/" );
    }

    public Name parse( final String name )
        throws NamingException
    {
        return new CompoundName( name, c_syntax );
    }
}
