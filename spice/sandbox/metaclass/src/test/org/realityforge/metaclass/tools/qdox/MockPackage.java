/*
 * Copyright (C) The Spice Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */
package org.realityforge.metaclass.tools.qdox;

import com.thoughtworks.qdox.model.JavaClassParent;
import com.thoughtworks.qdox.model.JavaSource;
import com.thoughtworks.qdox.model.JavaClass;

/**
 *
 * @author <a href="mailto:peter at realityforge.org">Peter Donald</a>
 * @version $Revision: 1.1 $ $Date: 2003-08-22 06:26:11 $
 */
class MockPackage implements JavaClassParent
{
    public String asClassNamespace()
    {
        return "com.biz";
    }

    public JavaSource getParentSource()
    {
        return null;
    }

    public void addClass( JavaClass javaClass )
    {
    }

    public JavaClass[] getClasses()
    {
        return new JavaClass[ 0 ];
    }
}
