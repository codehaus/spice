/*
 * Copyright (C) The Spice Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */
package org.realityforge.jndikit.rmi;

import java.rmi.Remote;
import org.realityforge.jndikit.NamingProvider;

/**
 * The underlying communication interface for remote contexts.
 *
 * @author <a href="mailto:peter at realityforge.org">Peter Donald</a>
 * @version $Revision: 1.2 $
 */
public interface RMINamingProvider
    extends NamingProvider, Remote
{
}
