/*
 * Copyright (C) The Spice Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */
package org.realityforge.metaclass.model;

import java.io.Serializable;

/**
 * This class contains the meta information about a Class. It contains
 * the name of the class, access modifiers, attributes, the classes fields and
 * the classes methods.
 *
 * @author <a href="mailto:peter at realityforge.org">Peter Donald</a>
 * @version $Revision: 1.8 $ $Date: 2003-10-28 11:02:02 $
 */
public class ClassDescriptor
    extends FeatureDescriptor
    implements Serializable
{
    /**
     * The name of class.
     */
    private final String m_name;

    /**
     * The fields of the class.
     */
    private final FieldDescriptor[] m_fields;

    /**
     * The methods in the class.
     */
    private final MethodDescriptor[] m_methods;

    /**
     * Create a ClassDescriptor with metadata about a class.
     * The descriptor usually represents a corrresponding
     * java class but this is not always the case.
     *
     * @param classname the name of class
     * @param attributes the top level attribute metadata
     * @param fields the field descriptors for class
     * @param methods the method descriptors for class
     */
    public ClassDescriptor( final String classname,
                            final Attribute[] attributes,
                            final FieldDescriptor[] fields,
                            final MethodDescriptor[] methods )
    {
        super( attributes, attributes );
        if( null == classname )
        {
            throw new NullPointerException( "classname" );
        }
        if( null == fields )
        {
            throw new NullPointerException( "fields" );
        }
        for( int i = 0; i < fields.length; i++ )
        {
            if( null == fields[ i ] )
            {
                throw new NullPointerException( "fields[" + i + "]" );
            }
        }
        if( null == methods )
        {
            throw new NullPointerException( "methods" );
        }
        for( int i = 0; i < methods.length; i++ )
        {
            if( null == methods[ i ] )
            {
                throw new NullPointerException( "methods[" + i + "]" );
            }
        }
        m_name = classname;
        m_fields = fields;
        m_methods = methods;
    }

    /**
     * Return the name of the class.
     *
     * @return the name of the class.
     */
    public String getName()
    {
        return m_name;
    }

    /**
     * Return the FieldDescriptors for class.
     * Note that it is not necessary that all fields in the
     * class have corresponding FieldDescriptors.
     *
     * @return the FieldDescriptors for class.
     */
    public FieldDescriptor[] getFields()
    {
        return m_fields;
    }

    /**
     * Return the MethodDescriptors for class.
     * Note that it is not necessary that all methods in the
     * class have corresponding MethodDescriptors.
     *
     * @return the MethodDescriptors for class.
     */
    public MethodDescriptor[] getMethods()
    {
        return m_methods;
    }
}
