/*
 * Copyright (C) The Spice Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */
package org.realityforge.salt.io;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 *
 * @author <a href="mailto:peter at realityforge.org">Peter Donald</a>
 * @version $Revision: 1.1 $ $Date: 2003-06-13 04:05:21 $
 */
class MockOutputStream
    extends ByteArrayOutputStream
{
    private boolean m_closed;

    public boolean isClosed()
    {
        return m_closed;
    }

    public void close() throws IOException
    {
        m_closed = true;
    }
}
