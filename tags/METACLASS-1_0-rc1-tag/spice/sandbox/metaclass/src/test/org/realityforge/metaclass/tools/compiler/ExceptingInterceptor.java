/*
 * Copyright (C) The Spice Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */
package org.realityforge.metaclass.tools.compiler;

import org.realityforge.metaclass.tools.qdox.DefaultQDoxAttributeInterceptor;
import org.realityforge.metaclass.model.Attribute;
import com.thoughtworks.qdox.model.JavaClass;

/**
 *
 * @author <a href="mailto:peter at realityforge.org">Peter Donald</a>
 * @version $Revision: 1.1 $ $Date: 2003-10-16 06:53:13 $
 */
class ExceptingInterceptor
    extends DefaultQDoxAttributeInterceptor
{
    static final IllegalStateException EXCEPTION = new IllegalStateException( "Blah!" );

    public Attribute processClassAttribute( JavaClass clazz,
                                            Attribute attribute )
    {
        throw EXCEPTION;
    }
}
