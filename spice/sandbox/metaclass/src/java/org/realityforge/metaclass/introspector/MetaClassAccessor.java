/*
 * Copyright (C) The Spice Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */
package org.realityforge.metaclass.introspector;

import org.realityforge.metaclass.model.ClassDescriptor;

/**
 * This is the interface used to access MetaClass
 * {@link org.realityforge.metaclass.model.ClassDescriptor}s for a particular class.
 *
 * @author <a href="mailto:peter at realityforge.org">Peter Donald</a>
 * @version $Revision: 1.1 $ $Date: 2003-10-04 00:47:48 $
 */
public interface MetaClassAccessor
{
    /**
     * Return a {@link org.realityforge.metaclass.model.ClassDescriptor} for specified class.
     *
     * @param classname the classname to get {@link org.realityforge.metaclass.model.ClassDescriptor} for
     * @param classLoader the classLoader to use
     * @return the newly created {@link org.realityforge.metaclass.model.ClassDescriptor}
     * @throws org.realityforge.metaclass.introspector.MetaClassException if unable to create {@link org.realityforge.metaclass.model.ClassDescriptor}
     */
    ClassDescriptor getClassDescriptor( String classname,
                                        ClassLoader classLoader )
        throws MetaClassException;
}
