/*
 * Copyright (C) The Spice Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */
package org.jcomponent.loggerstore;

import org.jcomponent.loggerstore.stores.AbstractLoggerStore;
import org.jcontainer.dna.Logger;

/**
 *
 * @author <a href="mailto:peter at realityforge.org">Peter Donald</a>
 * @version $Revision: 1.3 $ $Date: 2003-10-18 11:52:21 $
 */
class MalformedLoggerStore
    extends AbstractLoggerStore
{
    protected Logger createLogger( String name )
    {
        return null;
    }

    public void close()
    {
    }
}
