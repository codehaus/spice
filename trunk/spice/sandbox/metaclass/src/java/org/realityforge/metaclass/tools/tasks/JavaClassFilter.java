/*
 * Copyright (C) The Spice Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */
package org.realityforge.metaclass.tools.tasks;

import com.thoughtworks.qdox.model.JavaClass;

/**
 * Interface used for filtering out JavaClass objects
 * prior to attempting to generate metadata from them.
 *
 * @author <a href="mailto:peter at realityforge.org">Peter Donald</a>
 * @version $Revision: 1.1 $ $Date: 2003-08-31 08:02:53 $
 */
public interface JavaClassFilter
{
    /**
     * This method provides an access point for subclasses to use custom filters
     * on the list of classes parsed, i.e. to return null if the class has been filtered.
     *
     * @param javaClass the JavaClass
     * @return javaClass or null
     */
    JavaClass filterClass( JavaClass javaClass );
}
