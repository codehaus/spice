/*
 * Copyright (C) The Spice Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */
package org.codehaus.spice.jndikit;

import javax.naming.NameParser;
import javax.naming.spi.ObjectFactory;
import javax.naming.spi.StateFactory;

/**
 * This is the default namespace implementation.
 *
 * @author Peter Donald
 * @version $Revision: 1.1 $
 */
public class DefaultNamespace
    extends AbstractNamespace
{
    private NameParser m_nameParser;

    public DefaultNamespace( final NameParser nameParser )
    {
        this( nameParser,
              new ObjectFactory[ 0 ],
              new StateFactory[ 0 ] );
    }

    public DefaultNamespace( final NameParser nameParser,
                             final ObjectFactory[] objectFactorySet,
                             final StateFactory[] stateFactorySet )
    {
        m_nameParser = nameParser;
        m_objectFactorySet = objectFactorySet;
        m_stateFactorySet = stateFactorySet;
    }

    public NameParser getNameParser()
    {
        return m_nameParser;
    }

    public synchronized void addStateFactory( final StateFactory stateFactory )
    {
        super.addStateFactory( stateFactory );
    }

    public synchronized void addObjectFactory( final ObjectFactory objectFactory )
    {
        super.addObjectFactory( objectFactory );
    }
}
