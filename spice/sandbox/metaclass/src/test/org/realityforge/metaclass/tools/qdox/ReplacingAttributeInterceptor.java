/*
 * Copyright (C) The Spice Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */
package org.realityforge.metaclass.tools.qdox;

import org.realityforge.metaclass.model.Attribute;
import com.thoughtworks.qdox.model.JavaClass;
import com.thoughtworks.qdox.model.JavaField;
import com.thoughtworks.qdox.model.JavaMethod;

/**
 *
 * @author <a href="mailto:peter at realityforge.org">Peter Donald</a>
 * @version $Revision: 1.3 $ $Date: 2003-08-31 05:26:22 $
 */
class ReplacingAttributeInterceptor
    extends DefaultQDoxAttributeInterceptor
{
    public Attribute[] processClassAttributes( final JavaClass clazz,
                                               final Attribute[] attributes )
    {
        return Attribute.EMPTY_SET;
    }

    public Attribute[] processFieldAttributes( final JavaField field,
                                               final Attribute[] attributes )
    {
        return Attribute.EMPTY_SET;
    }

    public Attribute[] processMethodAttributes( final JavaMethod method,
                                                final Attribute[] attributes )
    {
        return Attribute.EMPTY_SET;
    }
}
