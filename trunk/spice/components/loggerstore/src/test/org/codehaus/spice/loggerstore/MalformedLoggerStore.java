/*
 * Copyright (C) The Spice Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */
package org.codehaus.spice.loggerstore;

import org.codehaus.spice.loggerstore.stores.AbstractLoggerStore;
import org.codehaus.dna.Logger;

/**
 * @author Peter Donald
 * @version $Revision: 1.3 $ $Date: 2004-04-18 22:59:38 $
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
