/*
 * Copyright (C) The Spice Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */
package intercept;

import org.realityforge.metaclass.tools.qdox.DefaultQDoxAttributeInterceptor;
import org.realityforge.metaclass.model.Attribute;
import com.thoughtworks.qdox.model.JavaClass;
import com.thoughtworks.qdox.model.JavaField;
import com.thoughtworks.qdox.model.JavaMethod;

/**
 * An example Interceptor that strips out "standard" javadoc tags.
 *
 * @author <a href="mailto:peter at realityforge.org">Peter Donald</a>
 * @version $Revision: 1.1 $ $Date: 2003-08-31 07:14:09 $
 */
public class StandardJavadocStripper
    extends DefaultQDoxAttributeInterceptor
{
    public Attribute processClassAttribute( JavaClass clazz,
                                            Attribute attribute )
    {
        return processAttribute( attribute );
    }

    public Attribute processFieldAttribute( JavaField field,
                                            Attribute attribute )
    {
        return processAttribute( attribute );
    }

    public Attribute processMethodAttribute( JavaMethod method,
                                             Attribute attribute )
    {
        return processAttribute( attribute );
    }

    private Attribute processAttribute( final Attribute attribute )
    {
        final String name = attribute.getName();
        if( name.equals( "version" ) ||
            name.equals( "author" ) ||
            name.equals( "return" ) ||
            name.equals( "exception" ) ||
            name.equals( "throws" ) ||
            name.equals( "param" ) )
        {
            return null;
        }
        else
        {
            return attribute;
        }
    }
}
