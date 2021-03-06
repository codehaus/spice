/*
 * Copyright (C) The Spice Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */
package org.codehaus.spice.jndikit;

import javax.naming.NameParser;
import javax.naming.NamingException;
import javax.naming.spi.ObjectFactory;
import javax.naming.spi.StateFactory;

/**
 * Interface representing Namespace/NamingSystem.
 * Associated with each namespace is a name parser,
 * object factories and state factories.
 *
 * @author Peter Donald
 * @version $Revision: 1.1 $
 */
public interface Namespace
    extends ObjectFactory, StateFactory
{
    NameParser getNameParser()
        throws NamingException;
}
