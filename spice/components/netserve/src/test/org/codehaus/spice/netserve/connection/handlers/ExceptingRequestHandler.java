/*
 * Copyright (C) The Spice Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */
package org.codehaus.spice.netserve.connection.handlers;

import java.net.Socket;

/**
 *
 * @author Peter Donald
 * @version $Revision: 1.2 $ $Date: 2004-03-21 23:42:59 $
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

