/*
 * Copyright (C) The Spice Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */
package org.realityforge.jndikit;

import javax.naming.Binding;
import javax.naming.Context;
import javax.naming.Name;
import javax.naming.NameClassPair;
import javax.naming.NameParser;
import javax.naming.NamingException;

/**
 * The underlying communication interface for remote contexts.
 *
 * @author <a href="mailto:peter at realityforge.org">Peter Donald</a>
 * @version $Revision: 1.2 $
 */
public interface NamingProvider
{
    NameParser getNameParser()
        throws NamingException, Exception;

    void bind( Name name, String className, Object object )
        throws NamingException, Exception;

    void rebind( Name name, String className, Object object )
        throws NamingException, Exception;

    Context createSubcontext( Name name )
        throws NamingException, Exception;

    void destroySubcontext( Name name )
        throws NamingException, Exception;

    NameClassPair[] list( Name name )
        throws NamingException, Exception;

    Binding[] listBindings( Name name )
        throws NamingException, Exception;

    Object lookup( Name name )
        throws NamingException, Exception;

    void unbind( Name name )
        throws NamingException, Exception;
}
