/*
 * Copyright (C) The Spice Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */
package org.realityforge.metaclass.jmx;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.MethodDescriptor;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import javax.management.Descriptor;
import javax.management.MBeanParameterInfo;
import javax.management.modelmbean.ModelMBeanAttributeInfo;
import javax.management.modelmbean.ModelMBeanConstructorInfo;
import javax.management.modelmbean.ModelMBeanInfo;
import javax.management.modelmbean.ModelMBeanOperationInfo;
import org.realityforge.metaclass.Attributes;
import org.realityforge.metaclass.introspector.MetaClassException;
import org.realityforge.metaclass.model.Attribute;
import org.realityforge.metaclass.model.ParameterDescriptor;

public class MBeanBuilder
{
    private static final String MX_COMPONENT_CONSTANT = "mx.component";

    private static final String MX_CONSTRUCTOR_CONSTANT = "mx.constructor";
    private static final String MX_OPERATION_CONSTANT = "mx.operation";
    private static final String MX_ATTRIBUTE_CONSTANT = "mx.attribute";

    private static final String MX_PARAMETER_CONSTANT = "mx.parameter";

    private static final String DESCRIPTION_KEY_CONSTANT = "description";
    private static final String IMPACT_KEY_CONSTANT = "impact";
    private static final String NAME_KEY_CONSTANT = "name";

    private static final String EMPTY_STRING = "";

    private static final String IMPACT_INFO = "INFO";
    private static final String IMPACT_ACTION = "ACTION";
    private static final String IMPACT_ACTION_INFO = "ACTION_INFO";

    public Object buildMBean( final Object value )
        throws Exception
    {
        final Class type = value.getClass();
        final ModelMBeanInfo mBeanInfo = buildMBeanInfo( type );
        if( null == mBeanInfo )
        {
            return null;
        }
        return new WrapperModelMBean( mBeanInfo, value );
    }

    public ModelMBeanInfo buildMBeanInfo( final Class type )
        throws Exception
    {
        final Attribute attribute =
            Attributes.getAttribute( type, MX_COMPONENT_CONSTANT );
        if( null == attribute )
        {
            return null;
        }
        final String description = getTypeDescription( type );

        final ModelInfoCreationHelper helper = new ModelInfoCreationHelper();
        helper.setClassname( type.getName() );
        helper.setDescription( description );

        final BeanInfo beanInfo = getBeanInfo( type );

        extractConstructors( beanInfo, helper );
        extractAttributes( beanInfo, helper );
        extractOperations( beanInfo, helper );

        return helper.toModelMBeanInfo();
    }

    private void extractConstructors( final BeanInfo beanInfo,
                                      final ModelInfoCreationHelper helper )
    {
        final Constructor[] constructors = beanInfo.getBeanDescriptor().getBeanClass().getConstructors();
        for( int i = 0; i < constructors.length; i++ )
        {
            final ModelMBeanConstructorInfo info =
                extractConstructor( constructors[ i ] );
            if( null != info )
            {
                helper.addConstructor( info );
            }
        }
    }

    private ModelMBeanConstructorInfo extractConstructor( final Constructor constructor )
    {
        final Attribute attribute =
            Attributes.getAttribute( constructor, MX_CONSTRUCTOR_CONSTANT );
        if( null == attribute )
        {
            return null;
        }
        final String description =
            attribute.getParameter( DESCRIPTION_KEY_CONSTANT, EMPTY_STRING );

        final MBeanParameterInfo[] infos = parseParameterInfos( constructor );

        final ModelMBeanConstructorInfo info =
            new ModelMBeanConstructorInfo( constructor.getName(),
                                           description,
                                           infos );
        //MX4J caches operation results on MBeans, this disables cache
        final Descriptor descriptor = info.getDescriptor();
        descriptor.setField( "currencyTimeLimit", new Integer( 0 ) );
        info.setDescriptor( descriptor );

        return info;
    }

    private String getTypeDescription( final Class type )
    {
        final Attribute desc =
            Attributes.getAttribute( type, MX_COMPONENT_CONSTANT );
        String description = EMPTY_STRING;
        if( null != desc )
        {
            description = desc.getParameter( DESCRIPTION_KEY_CONSTANT, EMPTY_STRING );
        }
        return description;
    }

    private void extractAttributes( final BeanInfo beanInfo,
                                    final ModelInfoCreationHelper helper )
    {
        final PropertyDescriptor[] propertys = beanInfo.getPropertyDescriptors();

        for( int i = 0; i < propertys.length; i++ )
        {
            final ModelMBeanAttributeInfo info =
                extractAttribute( propertys[ i ] );
            if( null != info )
            {
                helper.addAttribute( info );
            }
        }
    }

    private ModelMBeanAttributeInfo extractAttribute( final PropertyDescriptor property )
    {
        Method readMethod = property.getReadMethod();
        Method writeMethod = property.getWriteMethod();
        String description = null;

        //If attributes dont have attribute markup then
        //dont allow user to read/write property
        if( null != readMethod )
        {
            final Attribute attribute =
                Attributes.getAttribute( readMethod, MX_ATTRIBUTE_CONSTANT );
            if( null == attribute )
            {
                readMethod = null;
            }
            else
            {
                description =
                    attribute.getParameter( DESCRIPTION_KEY_CONSTANT, EMPTY_STRING );
            }
        }

        if( null != writeMethod )
        {
            final Attribute attribute =
                Attributes.getAttribute( writeMethod, MX_ATTRIBUTE_CONSTANT );
            if( null == attribute )
            {
                writeMethod = null;
            }
            else if( null == description )
            {
                description =
                    attribute.getParameter( DESCRIPTION_KEY_CONSTANT, EMPTY_STRING );
            }
        }

        final boolean isReadable = null != readMethod;
        final boolean isIs = isReadable && readMethod.getName().startsWith( "is" );
        final boolean isWritable = null != writeMethod;

        if( !isReadable && !isWritable )
        {
            return null;
        }

        final String name = property.getName();
        final ModelMBeanAttributeInfo info =
            new ModelMBeanAttributeInfo( name,
                                         property.getPropertyType().getName(),
                                         description,
                                         isReadable,
                                         isWritable,
                                         isIs );
        //MX4J caches operation results on MBeans, this disables cache
        final Descriptor descriptor = info.getDescriptor();
        descriptor.setField( "currencyTimeLimit", new Integer( 1 ) );
        if( isReadable )
        {
            descriptor.setField( "getMethod", readMethod.getName() );
        }
        if( isWritable )
        {
            descriptor.setField( "setMethod", writeMethod.getName() );
        }
        info.setDescriptor( descriptor );
        return info;
    }

    private void extractOperations( final BeanInfo beanInfo,
                                    final ModelInfoCreationHelper helper )
    {
        final MethodDescriptor[] methods = beanInfo.getMethodDescriptors();
        for( int i = 0; i < methods.length; i++ )
        {
            final ModelMBeanOperationInfo info = extractOperation( methods[ i ] );
            if( null != info )
            {
                helper.addOperation( info );
            }
        }
    }

    private ModelMBeanOperationInfo extractOperation( final MethodDescriptor method )
    {
        final Attribute attribute =
            Attributes.getAttribute( method.getMethod(), MX_OPERATION_CONSTANT );
        if( null == attribute )
        {
            return null;
        }
        final String description =
            attribute.getParameter( DESCRIPTION_KEY_CONSTANT, EMPTY_STRING );
        final int impactCode = parseImpact( attribute );

        final MBeanParameterInfo[] infos = parseParameterInfos( method.getMethod() );

        final String returnType = method.getMethod().getReturnType().getName();
        final ModelMBeanOperationInfo info =
            new ModelMBeanOperationInfo( method.getName(),
                                         description,
                                         infos,
                                         returnType,
                                         impactCode );
        //MX4J caches operation results on MBeans, this disables cache
        final Descriptor descriptor = info.getDescriptor();
        descriptor.setField( "currencyTimeLimit", new Integer( 0 ) );
        info.setDescriptor( descriptor );

        return info;
    }

    private MBeanParameterInfo[] parseParameterInfos( final Constructor constructor )
    {
        try
        {
            final Attribute[] attributes =
                Attributes.getConstructor( constructor ).getAttributes();
            final ParameterDescriptor[] parameters =
                Attributes.getConstructor( constructor ).getParameters();
            return buildParametersFromMetaData( attributes, parameters );
        }
        catch( final MetaClassException mce )
        {
            return buildParametersViaReflection( constructor.getParameterTypes() );
        }
    }

    private MBeanParameterInfo[] parseParameterInfos( final Method method )
    {
        try
        {
            final Attribute[] attributes =
                Attributes.getMethod( method ).getAttributes();
            final ParameterDescriptor[] parameters =
                Attributes.getMethod( method ).getParameters();
            return buildParametersFromMetaData( attributes, parameters );
        }
        catch( final MetaClassException mce )
        {
            return buildParametersViaReflection( method.getParameterTypes() );
        }
    }

    private MBeanParameterInfo[] buildParametersFromMetaData( final Attribute[] attributes,
                                                              final ParameterDescriptor[] parameters )
    {
        final MBeanParameterInfo[] infos = new MBeanParameterInfo[ parameters.length ];
        for( int i = 0; i < infos.length; i++ )
        {
            final ParameterDescriptor parameter = parameters[ i ];
            final String name = parameter.getName();
            final String type = parameter.getType();
            final String description = parseParameterDescription( attributes, name );
            infos[ i ] = new MBeanParameterInfo( name, type, description );
        }
        return infos;
    }

    private MBeanParameterInfo[] buildParametersViaReflection( final Class[] types )
    {
        final MBeanParameterInfo[] infos = new MBeanParameterInfo[ types.length ];
        for( int i = 0; i < types.length; i++ )
        {
            infos[ i ] =
                new MBeanParameterInfo( EMPTY_STRING,
                                        types[ i ].getName(),
                                        EMPTY_STRING );
        }
        return infos;
    }

    private String parseParameterDescription( final Attribute[] attributes,
                                              final String name )
    {
        final Attribute[] params =
            Attributes.getAttributesByName( attributes, MX_PARAMETER_CONSTANT );
        for( int i = 0; i < params.length; i++ )
        {
            final Attribute paramAttribute = params[ i ];
            final String key = paramAttribute.getParameter( NAME_KEY_CONSTANT );
            if( name.equals( key ) )
            {
                return paramAttribute.getParameter( DESCRIPTION_KEY_CONSTANT, EMPTY_STRING );
            }
        }
        return EMPTY_STRING;
    }

    private int parseImpact( final Attribute attribute )
    {
        final String impact =
            attribute.getParameter( IMPACT_KEY_CONSTANT, EMPTY_STRING );
        final int impactCode;
        if( IMPACT_INFO.equals( impact ) )
        {
            impactCode = ModelMBeanOperationInfo.INFO;
        }
        else if( IMPACT_ACTION.equals( impact ) )
        {
            impactCode = ModelMBeanOperationInfo.ACTION;
        }
        else if( IMPACT_ACTION_INFO.equals( impact ) )
        {
            impactCode = ModelMBeanOperationInfo.ACTION_INFO;
        }
        else
        {
            impactCode = ModelMBeanOperationInfo.UNKNOWN;
        }
        return impactCode;
    }

    private BeanInfo getBeanInfo( final Class clazz )
        throws Exception
    {
        try
        {
            return Introspector.getBeanInfo( clazz );
        }
        catch( final Exception e )
        {
            final String message = "Unable to Introspect " + clazz.getName();
            throw new Exception( message, e );
        }
    }
}
