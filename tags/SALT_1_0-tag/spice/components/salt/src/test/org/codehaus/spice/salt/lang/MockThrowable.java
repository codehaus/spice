/*
 * Copyright (C) The Spice Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */
package org.codehaus.spice.salt.lang;

import java.io.PrintWriter;

/**
 * @author Peter Donald
 * @version $Revision: 1.1 $ $Date: 2003-12-02 02:15:05 $
 */
class MockThrowable
    extends Throwable
{
    private final String m_stackTrace;
    private final MockThrowable m_cause;

    public MockThrowable( final String message,
                          final MockThrowable cause,
                          final String stackTrace )
    {
        super( message );
        m_cause = cause;
        m_stackTrace = stackTrace;
    }

    public void printStackTrace( PrintWriter s )
    {
        doPrintStackTrace( s );
    }

    private void doPrintStackTrace( PrintWriter s )
    {
        s.write( m_stackTrace );
        if( null != m_cause )
        {
            s.write( ExceptionUtil.SEPARATOR );
            m_cause.doPrintStackTrace( s );
        }
    }

    public Throwable getCause()
    {
        return m_cause;
    }
}
