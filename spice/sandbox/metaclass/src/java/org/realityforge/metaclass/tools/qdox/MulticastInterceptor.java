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
 * Thhis interceptor passes each attribute or set of attributes
 * through a chain of interceptors. When processing a single
 * attribute the chain will terminate when the first interceptor
 * returns null.
 *
 * @author <a href="mailto:peter at realityforge.org">Peter Donald</a>
 * @version $Revision: 1.1 $ $Date: 2003-08-31 07:49:51 $
 */
public class MulticastInterceptor
    implements QDoxAttributeInterceptor
{
    /**
     * The interceptor chain to delegate to.
     */
    private final QDoxAttributeInterceptor[] m_interceptors;

    /**
     * Create a interceptor that delegates to specified interceptors.
     *
     * @param interceptors the interceptors
     */
    public MulticastInterceptor( QDoxAttributeInterceptor[] interceptors )
    {
        if( null == interceptors )
        {
            throw new NullPointerException( "interceptors" );
        }
        for( int i = 0; i < interceptors.length; i++ )
        {
            if( null == interceptors[ i ] )
            {
                throw new NullPointerException( "interceptors[" + i + "]" );
            }
        }
        m_interceptors = interceptors;
    }

    /**
     * Process a single attribute at the Class level.
     * The implementation may return a new attribute
     * instance, the old attribute instance or null to
     * ignore attribute.
     *
     * @param clazz the corresponding JavaClass instance
     * @param attribute the attribute
     * @return the resulting attribute or null
     */
    public Attribute processClassAttribute( final JavaClass clazz,
                                            Attribute attribute )
    {
        for( int i = 0; i < m_interceptors.length; i++ )
        {
            attribute = m_interceptors[ i ].processClassAttribute( clazz, attribute );
            if( null == attribute )
            {
                return null;
            }
        }
        return attribute;
    }

    /**
     * Process a single attribute at the Field level.
     * The implementation may return a new attribute
     * instance, the old attribute instance or null to
     * ignore attribute.
     *
     * @param field the corresponding JavaField instance
     * @param attribute the attribute
     * @return the resulting attribute or null
     */
    public Attribute processFieldAttribute( final JavaField field,
                                            Attribute attribute )
    {
        for( int i = 0; i < m_interceptors.length; i++ )
        {
            attribute = m_interceptors[ i ].processFieldAttribute( field, attribute );
            if( null == attribute )
            {
                return null;
            }
        }
        return attribute;
    }

    /**
     * Process a single attribute at the Method level.
     * The implementation may return a new attribute
     * instance, the old attribute instance or null to
     * ignore attribute.
     *
     * @param method the corresponding JavaMethod instance
     * @param attribute the attribute
     * @return the resulting attribute or null
     */
    public Attribute processMethodAttribute( final JavaMethod method,
                                             Attribute attribute )
    {
        for( int i = 0; i < m_interceptors.length; i++ )
        {
            attribute = m_interceptors[ i ].processMethodAttribute( method, attribute );
            if( null == attribute )
            {
                return null;
            }
        }
        return attribute;
    }

    /**
     * Process the set of attributes for a specific Class.
     * The implementation must return an array of attributes
     * with no null entrys.
     *
     * @param clazz the corresponding JavaClass instance
     * @param attributes the attributes
     * @return the resulting attribute array
     */
    public Attribute[] processClassAttributes( final JavaClass clazz,
                                               Attribute[] attributes )
    {
        for( int i = 0; i < m_interceptors.length; i++ )
        {
            attributes = m_interceptors[ i ].processClassAttributes( clazz, attributes );
        }
        return attributes;
    }

    /**
     * Process the set of attributes for a specific Field.
     * The implementation must return an array of attributes
     * with no null entrys.
     *
     * @param field the corresponding JavaField instance
     * @param attributes the attributes
     * @return the resulting attribute array
     */
    public Attribute[] processFieldAttributes( JavaField field, Attribute[] attributes )
    {
        for( int i = 0; i < m_interceptors.length; i++ )
        {
            attributes = m_interceptors[ i ].processFieldAttributes( field, attributes );
        }
        return attributes;
    }

    /**
     * Process the set of attributes for a specific Method.
     * The implementation must return an array of attributes
     * with no null entrys.
     *
     * @param method the corresponding JavaMethod instance
     * @param attributes the attributes
     * @return the resulting attribute array
     */
    public Attribute[] processMethodAttributes( final JavaMethod method,
                                                Attribute[] attributes )
    {
        for( int i = 0; i < m_interceptors.length; i++ )
        {
            attributes = m_interceptors[ i ].processMethodAttributes( method, attributes );
        }
        return attributes;
    }
}
