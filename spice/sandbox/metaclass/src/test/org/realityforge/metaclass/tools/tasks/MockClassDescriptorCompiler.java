/*
 * Copyright (C) The Spice Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */
package org.realityforge.metaclass.tools.tasks;

import org.realityforge.metaclass.tools.compiler.ClassDescriptorCompiler;
import org.realityforge.metaclass.io.MetaClassIO;

/**
 *
 * @author <a href="mailto:peter at realityforge.org">Peter Donald</a>
 * @version $Revision: 1.1 $ $Date: 2003-10-04 08:55:47 $
 */
class MockClassDescriptorCompiler
    extends ClassDescriptorCompiler
{
    public String getExtension()
    {
        return super.getExtension();
    }

    public MetaClassIO getMetaClassIO()
    {
        return super.getMetaClassIO();
    }
}
