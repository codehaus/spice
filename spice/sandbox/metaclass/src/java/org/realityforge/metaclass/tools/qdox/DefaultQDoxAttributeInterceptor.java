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
 * A base implementation of QDoxAttributeInterceptor that
 * just returns original values parsed from source files.
 *
 * @author <a href="mailto:peter at realityforge.org">Peter Donald</a>
 * @version $Revision: 1.1 $ $Date: 2003-08-24 04:13:15 $
 */
public class DefaultQDoxAttributeInterceptor
    implements QDoxAttributeInterceptor
{
    /**
     * Return unaltered attributes for artefact.
     *
     * @param clazz the JavaClass
     * @param attribute the attribute
     * @return the original attribute
     */
    public Attribute processClassAttribute( final JavaClass clazz,
                                            final Attribute attribute )
    {
        return attribute;
    }

    /**
     * Return unaltered attribute for artefact.
     *
     * @param field the JavaField
     * @param attribute the attribute
     * @return the original attribute
     */
    public Attribute processFieldAttribute( final JavaField field,
                                            final Attribute attribute )
    {
        return attribute;
    }

    /**
     * Return unaltered attribute for artefact.
     *
     * @param method the JavaMethod
     * @param attribute the attribute
     * @return the original attribute
     */
    public Attribute processMethodAttribute( final JavaMethod method,
                                             final Attribute attribute )
    {
        return attribute;
    }

    /**
     * Return unaltered attributes for artefact.
     *
     * @param clazz the JavaClass
     * @param attributes the attributes
     * @return the original attributes
     */
    public Attribute[] processClassAttributes( final JavaClass clazz,
                                               final Attribute[] attributes )
    {
        return attributes;
    }

    /**
     * Return unaltered attributes for artefact.
     *
     * @param field the JavaField
     * @param attributes the attributes
     * @return the original attributes
     */
    public Attribute[] processFieldAttributes( final JavaField field,
                                               final Attribute[] attributes )
    {
        return attributes;
    }

    /**
     * Return unaltered attributes for artefact.
     *
     * @param method the JavaMethod
     * @param attributes the attributes
     * @return the original attributes
     */
    public Attribute[] processMethodAttributes( final JavaMethod method,
                                                final Attribute[] attributes )
    {
        return attributes;
    }
}
