/*
 * Copyright (C) The Spice Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */
package org.realityforge.metaclass.tools.tasks;

import com.thoughtworks.qdox.model.JavaClass;
import org.realityforge.metaclass.tools.compiler.JavaClassFilter;

/**
 *
 * @author <a href="mailto:peter at realityforge.org">Peter Donald</a>
 * @version $Revision: 1.1 $ $Date: 2003-10-04 10:12:39 $
 */
public class PassThroughFilter
    implements JavaClassFilter
{
    public JavaClass filterClass( JavaClass javaClass )
    {
        return javaClass;
    }
}
