/*
 * Copyright (C) The Spice Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */
package org.realityforge.metaclass.tools.compiler;

import org.realityforge.metaclass.model.ClassDescriptor;
import java.io.File;

/**
 *
 * @author <a href="mailto:peter at realityforge.org">Peter Donald</a>
 * @version $Revision: 1.1 $ $Date: 2003-10-04 06:58:43 $
 */
class MockMonitor
    extends DefaultCompilerMonitor
{
    private boolean m_error;

    public void errorWritingDescriptor( ClassDescriptor descriptor,
                                        Exception e )
    {
        m_error = true;
    }

    public void missingSourceFile( File file )
    {
        m_error = true;
    }

    public boolean isError()
    {
        return m_error;
    }
}
