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
 * @version $Revision: 1.1 $ $Date: 2003-08-24 04:50:08 $
 */
public class ReplacingAttributeInterceptor
    extends DefaultQDoxAttributeInterceptor
{
    public Attribute[] processClassAttributes( JavaClass clazz,
                                               Attribute[] attributes )
    {
        return Attribute.EMPTY_SET;
    }

    public Attribute[] processFieldAttributes( JavaField field,
                                               Attribute[] attributes )
    {
        return Attribute.EMPTY_SET;
    }

    public Attribute[] processMethodAttributes( JavaMethod method,
                                                Attribute[] attributes )
    {
        return Attribute.EMPTY_SET;
    }
}
