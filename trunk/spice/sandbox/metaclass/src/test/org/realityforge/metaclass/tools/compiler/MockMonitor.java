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
import java.util.List;

/**
 *
 * @author <a href="mailto:peter at realityforge.org">Peter Donald</a>
 * @version $Revision: 1.2 $ $Date: 2003-10-04 09:28:16 $
 */
class MockMonitor
    extends DefaultCompilerMonitor
{
    private boolean m_error;

    public void errorWritingDescriptor( ClassDescriptor descriptor,
                                        Exception e )
    {
        System.out.println( "errorWritingDescriptor(" + descriptor.getName() + "," + e + ")" );
        m_error = true;
    }

    public void missingSourceFile( File file )
    {
        System.out.println( "missingSourceFile(" + file + ")" );
        m_error = true;
    }

    public boolean isError()
    {
        return m_error;
    }

    public void javaClassObjectsLoaded( List classes )
    {
        System.out.println( "javaClassObjectsLoaded(" + classes + ")" );
    }

    public void postFilterJavaClassList( List classes )
    {
        System.out.println( "postFilterJavaClassList(" + classes + ")" );
    }
}
