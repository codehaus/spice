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
 * Implementations of this class are responsible for loading and
 * caching the {@link ClassDescriptor}
 * objects for corresponding java classes.
 *
 * @author <a href="mailto:peter at realityforge.org">Peter Donald</a>
 * @version $Revision: 1.2 $ $Date: 2003-10-28 06:19:06 $
 */
public interface MetaClassRegistry
{
   /**
    * Return a ClassDescriptor for specified class.
    *
    * @param classname the classname to get ClassDescriptor for
    * @param classLoader the classLoader to use
    * @return the newly created ClassDescriptor
    * @throws MetaClassException if unable to create ClassDescriptor
    */
   ClassDescriptor getClassDescriptor( String classname,
                                       ClassLoader classLoader )
      throws MetaClassException;
}
