/*
 * Copyright (C) The Spice Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */
package org.realityforge.metaclass.tools.compiler;

import com.thoughtworks.qdox.model.JavaClass;
import org.realityforge.metaclass.model.Attribute;
import org.realityforge.metaclass.tools.qdox.DefaultQDoxAttributeInterceptor;

/**
 * @author Peter Donald
 * @version $Revision: 1.2 $ $Date: 2003-11-28 11:14:54 $
 */
class ExceptingInterceptor
    extends DefaultQDoxAttributeInterceptor
{
    static final IllegalStateException EXCEPTION = new IllegalStateException(
        "Blah!" );

    public Attribute processClassAttribute( JavaClass clazz,
                                            Attribute attribute )
    {
        throw EXCEPTION;
    }
}
