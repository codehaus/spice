/*
 * Copyright (C) The Spice Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */
package org.jcomponent.netserve.connection.handlers;

import java.net.Socket;

/**
 *
 * @author <a href="mailto:peter at realityforge.org">Peter Donald</a>
 * @version $Revision: 1.1 $ $Date: 2003-10-29 03:00:14 $
 */
class ExceptingRequestHandler
    extends MockRequestHandler
{
    static final Exception EXCEPTION = new Exception();

    protected void doPerformRequest( Socket socket )
        throws Exception
    {
        super.doPerformRequest(socket );
        throw EXCEPTION;
    }
}

