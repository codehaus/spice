/*
 * Copyright (C) The Spice Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */
package org.realityforge.jndikit;

import java.util.Hashtable;
import javax.naming.Context;
import javax.naming.Name;
import javax.naming.NameParser;
import javax.naming.NamingException;
import javax.naming.spi.NamingManager;

/**
 * Namespace that directly uses NamingManager.
 *
 * @author <a href="mailto:peter@apache.org">Peter Donald</a>
 * @version $Revision: 1.1 $
 */
public class StandardNamespace
    implements Namespace
{
    private NameParser m_nameParser;

    /**
     * Construct a Namespace with specified NameParser.
     *
     * @param nameParser the NameParser for Namespace
     */
    public StandardNamespace( final NameParser nameParser )
    {
        m_nameParser = nameParser;
    }

    public NameParser getNameParser()
    {
        return m_nameParser;
    }

    public Object getStateToBind( final Object object,
                                  final Name name,
                                  final Context parent,
                                  final Hashtable environment )
        throws NamingException
    {
        return NamingManager.getStateToBind( object, name, parent, environment );
    }

    public Object getObjectInstance( final Object object,
                                     final Name name,
                                     final Context parent,
                                     final Hashtable environment )
        throws Exception
    {
        return NamingManager.getObjectInstance( object, name, parent, environment );
    }
}
