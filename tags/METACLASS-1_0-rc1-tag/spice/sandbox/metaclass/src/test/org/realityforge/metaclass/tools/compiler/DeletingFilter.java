/*
 * Copyright (C) The Spice Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */
package org.realityforge.metaclass.tools.compiler;

import com.thoughtworks.qdox.model.JavaClass;
import org.realityforge.metaclass.tools.compiler.JavaClassFilter;

/**
 *
 * @author <a href="mailto:peter at realityforge.org">Peter Donald</a>
 * @version $Revision: 1.1 $ $Date: 2003-10-04 06:59:14 $
 */
class DeletingFilter
    implements JavaClassFilter
{
    public JavaClass filterClass( JavaClass javaClass )
    {
        return null;
    }
}
