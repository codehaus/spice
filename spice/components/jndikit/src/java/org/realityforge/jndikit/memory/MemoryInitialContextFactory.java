/*
 * Copyright (C) The Spice Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */
package org.realityforge.jndikit.memory;

import java.util.Hashtable;
import javax.naming.Context;
import javax.naming.NamingException;
import javax.naming.spi.InitialContextFactory;
import org.realityforge.jndikit.DefaultNameParser;
import org.realityforge.jndikit.DefaultNamespace;

/**
 * Initial context factory for memorycontext.
 *
 * <p><b>WARNING:</b> This class should never be use used in a real
 * system. It is is just a class that demonstrates how to write a
 * basic <code>InitialContextFactory</code> for MemeoryContext.
 * However this factory creates a new Context every time which is
 * rarely desired behaviour.</p>
 *
 * <p>In a real application you may want the policy of Context
 * creation to be specific application. Some strategies include.</p>
 * <ul>
 *   <li>ClassLoader-wide. ie Every user who is in same ClassLoader
 *       or loaded from a Child ClassLoader will see same JNDI tree.
 *       In this case the InitialContextFactory should cache root
 *       context in a static variable.</li>
 *   <li>Thread-specific. ie Give out initial context based on which
 *       thread the caller is in. In this case the InitialContextFactory
 *       should cache root context in a [Inheritable]ThreadLocal
 *       variable.</li>
 *   <li>Parameter-specific. ie Give out initial context based on
 *       a parameter passed in. The parameter could be passed in as
 *       PROVIDER_URL or another standard context property. In this
 *       case the InitialContextFactory should cache root context(s)
 *       in a static map variable that maps between parameter and
 *       context.</li>
 * </ul>
 *
 * @author <a href="mailto:peter at realityforge.org">Peter Donald</a>
 * @version $Revision: 1.2 $
 */
public class MemoryInitialContextFactory
    implements InitialContextFactory
{
    public Context getInitialContext( final Hashtable environment )
        throws NamingException
    {
        final DefaultNameParser parser = new DefaultNameParser();
        final DefaultNamespace namespace = new DefaultNamespace( parser );
        return new MemoryContext( namespace, environment, null );
    }
}

