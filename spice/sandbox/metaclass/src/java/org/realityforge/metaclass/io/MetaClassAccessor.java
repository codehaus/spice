/*
 * Copyright (C) The Spice Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */
package org.realityforge.metaclass.io;

import org.realityforge.metaclass.MetaClassException;
import org.realityforge.metaclass.model.ClassDescriptor;

/**
 * This is the interface used to access MetaClass
 * {@link ClassDescriptor}s for a particular class.
 *
 * @author <a href="mailto:peter at realityforge.org">Peter Donald</a>
 * @version $Revision: 1.5 $ $Date: 2003-10-03 13:33:54 $
 */
public interface MetaClassAccessor
{
    /**
     * Return a {@link ClassDescriptor} for specified class.
     *
     * @param classname the classname to get {@link ClassDescriptor} for
     * @param classLoader the classLoader to use
     * @return the newly created {@link ClassDescriptor}
     * @throws MetaClassException if unable to create {@link ClassDescriptor}
     */
    ClassDescriptor getClassDescriptor( String classname,
                                        ClassLoader classLoader )
        throws MetaClassException;
}
