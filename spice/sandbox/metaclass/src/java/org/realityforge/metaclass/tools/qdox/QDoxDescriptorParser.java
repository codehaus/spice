/*
 * Copyright (C) The Spice Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */
package org.realityforge.metaclass.tools.qdox;

import com.thoughtworks.qdox.model.DocletTag;
import com.thoughtworks.qdox.model.JavaClass;
import com.thoughtworks.qdox.model.JavaField;
import com.thoughtworks.qdox.model.JavaMethod;
import com.thoughtworks.qdox.model.JavaParameter;
import com.thoughtworks.qdox.model.Type;
import java.lang.reflect.Modifier;
import java.util.Properties;
import org.realityforge.metaclass.model.Attribute;
import org.realityforge.metaclass.model.ClassDescriptor;
import org.realityforge.metaclass.model.FieldDescriptor;
import org.realityforge.metaclass.model.MethodDescriptor;
import org.realityforge.metaclass.model.ParameterDescriptor;

/**
 * This class is responsible for parsing a JavaClass object
 * and building a ClassDescriptor to correspond to the JavaClass
 * object.
 *
 * @version $Revision: 1.10 $ $Date: 2003-08-22 06:08:33 $
 */
public class QDoxDescriptorParser
{
    /**
     * Constant indicating parse state is
     * before the start of key.
     */
    private static final int PARSE_KEY_START = 0;
    /**
     * Constant indicating parse state is
     * parsing the key.
     */
    private static final int PARSE_KEY = 1;

    /**
     * Constant indicating parse state is
     * before the start of value and expecting ".
     */
    private static final int PARSE_VALUE_START = 2;

    /**
     * Constant indicating parse state is
     * parsing value string.
     */
    private static final int PARSE_VALUE = 3;

    /**
     * Constant indicating parse state is
     * after value closed.
     */
    private static final int PARSE_END = 4;

    /**
     * Build a ClassDescriptor for a JavaClass.
     *
     * @param javaClass the JavaClass
     * @return the ClassDescriptor
     */
    public ClassDescriptor buildClassDescriptor( final JavaClass javaClass )
    {
        final String classname = javaClass.getFullyQualifiedName();
        final int modifiers = parseModifiers( javaClass.getModifiers() );
        final Attribute[] attributes = buildAttributes( javaClass.getTags() );
        final FieldDescriptor[] fields = buildFields( javaClass );
        final MethodDescriptor[] methods = buildMethods( javaClass );

        return new ClassDescriptor( classname,
                                    modifiers,
                                    attributes,
                                    fields,
                                    methods );
    }

    /**
     * Build a set of MethodDescriptor instances for a JavaClass.
     *
     * @param javaClass the JavaClass
     * @return the MethodDescriptors
     */
    private MethodDescriptor[] buildMethods( final JavaClass javaClass )
    {
        final JavaMethod[] methods = javaClass.getMethods();
        final MethodDescriptor[] methodDescriptors = new MethodDescriptor[ methods.length ];
        for( int i = 0; i < methods.length; i++ )
        {
            methodDescriptors[ i ] = buildMethod( methods[ i ] );
        }
        return methodDescriptors;
    }

    /**
     * Build a MethodDescriptor for a JavaMethod.
     *
     * @param method the JavaMethod
     * @return the MethodDescriptor
     */
    MethodDescriptor buildMethod( final JavaMethod method )
    {
        final String name = method.getName();
        final Type returns = method.getReturns();
        final String type;
        if( null != returns )
        {
            type = returns.getValue();
        }
        else
        {
            type = "";
        }

        final int modifiers = parseModifiers( method.getModifiers() );
        final Attribute[] attributes = buildAttributes( method.getTags() );
        final ParameterDescriptor[] parameters =
            buildParameters( method.getParameters() );

        return new MethodDescriptor( name,
                                     type,
                                     modifiers,
                                     parameters,
                                     attributes );
    }

    /**
     * Build a set of ParameterDescriptor for JavaParameters.
     *
     * @param parameters the JavaParameters
     * @return the ParameterDescriptors
     */
    private ParameterDescriptor[] buildParameters( final JavaParameter[] parameters )
    {
        final ParameterDescriptor[] descriptors =
            new ParameterDescriptor[ parameters.length ];
        for( int i = 0; i < parameters.length; i++ )
        {
            descriptors[ i ] = buildParameter( parameters[ i ] );
        }
        return descriptors;
    }

    /**
     * Build a ParameterDescriptor for a JavaParameter.
     *
     * @param parameter the JavaParameter
     * @return the ParameterDescriptor
     */
    ParameterDescriptor buildParameter( final JavaParameter parameter )
    {
        final String name = parameter.getName();
        final String value = parameter.getType().getValue();
        return new ParameterDescriptor( name, value );
    }

    /**
     * Build a set of FieldDescriptor instances for a JavaClass.
     *
     * @param javaClass the JavaClass
     * @return the FieldDescriptors
     */
    private FieldDescriptor[] buildFields( final JavaClass javaClass )
    {
        final JavaField[] fields = javaClass.getFields();
        final FieldDescriptor[] fieldDescriptors = new FieldDescriptor[ fields.length ];
        for( int i = 0; i < fields.length; i++ )
        {
            fieldDescriptors[ i ] = buildField( fields[ i ] );
        }
        return fieldDescriptors;
    }

    /**
     * Build a set of FieldDescriptor instances for a JavaField.
     *
     * @param field the JavaField
     * @return the FieldDescriptor
     */
    FieldDescriptor buildField( final JavaField field )
    {
        final String name = field.getName();
        final String type = field.getType().getValue();
        final int modifiers = parseModifiers( field.getModifiers() );
        final Attribute[] attributes = buildAttributes( field.getTags() );
        return new FieldDescriptor( name,
                                    type,
                                    modifiers,
                                    attributes );
    }

    /**
     * Build a set of Attribute instances from a
     * set QDox DocletTag instances.
     *
     * @param tags the DocletTag instances
     * @return the Attributes
     */
    private Attribute[] buildAttributes( final DocletTag[] tags )
    {
        final Attribute[] attributes = new Attribute[ tags.length ];
        for( int i = 0; i < tags.length; i++ )
        {
            attributes[ i ] = buildAttribute( tags[ i ] );
        }
        return attributes;
    }

    /**
     * Build an Attribute object from a DocletTag.
     *
     * @param tag the DocletTag instance.
     * @return the Attribute
     */
    Attribute buildAttribute( final DocletTag tag )
    {
        final String name = tag.getName();

        final String value = tag.getValue();
        if( null == value || "".equals( value.trim() ) )
        {
            return new Attribute( name );
        }

        final Properties parameters = parseValueIntoParameters( value );
        if( null == parameters )
        {
            return new Attribute( name, value );
        }
        else
        {
            return new Attribute( name, parameters );
        }
    }

    /**
     * Extract a Modifier bit-fields from an array of qualifiers.
     * The qualifiers are strings such as "public", "private",
     * "abstract", "final" etc and these are translated into their
     * equivelents constants in {@link Modifier}. These constants
     * are then |'ed together and returned.
     *
     * @param qualifiers the qualifier strings
     * @return the modifier bit array
     */
    int parseModifiers( final String[] qualifiers )
    {
        int modifiers = 0;
        for( int i = 0; i < qualifiers.length; i++ )
        {
            final String qualifier =
                qualifiers[ i ].toLowerCase().trim();
            if( qualifier.equals( "public" ) )
            {
                modifiers |= Modifier.PUBLIC;
            }
            else if( qualifier.equals( "protected" ) )
            {
                modifiers |= Modifier.PROTECTED;
            }
            else if( qualifier.equals( "private" ) )
            {
                modifiers |= Modifier.PRIVATE;
            }
            else if( qualifier.equals( "abstract" ) )
            {
                modifiers |= Modifier.ABSTRACT;
            }
            else if( qualifier.equals( "static" ) )
            {
                modifiers |= Modifier.STATIC;
            }
            else if( qualifier.equals( "final" ) )
            {
                modifiers |= Modifier.FINAL;
            }
            else if( qualifier.equals( "transient" ) )
            {
                modifiers |= Modifier.TRANSIENT;
            }
            else if( qualifier.equals( "volatile" ) )
            {
                modifiers |= Modifier.VOLATILE;
            }
            else if( qualifier.equals( "synchronized" ) )
            {
                modifiers |= Modifier.SYNCHRONIZED;
            }
            else if( qualifier.equals( "native" ) )
            {
                modifiers |= Modifier.NATIVE;
            }
            else if( qualifier.equals( "strictfp" ) )
            {
                modifiers |= Modifier.STRICT;
            }
            else if( qualifier.equals( "interface" ) )
            {
                modifiers |= Modifier.INTERFACE;
            }
        }
        return modifiers;
    }

    /**
     * Parse the value string into a set of parameters.
     * The parameters must match the pattern
     *
     * <pre>
     * ^[ \t\r\n]*([a-zA-Z\_][a-zA-Z0-9\_]*=\"[^\"]*\"[ \t\r\n]+)+
     * </pre>
     *
     * <p>If the value does not match this pattern then null is returned
     * other wise the key=value pairs are parsed out and placed in
     * a properties object.</p>
     *
     * @param input the input value
     * @return the parameters if matches patterns, else null
     */
    Properties parseValueIntoParameters( final String input )
    {
        final Properties parameters = new Properties();

        final StringBuffer key = new StringBuffer();
        final StringBuffer value = new StringBuffer();

        int state = PARSE_KEY_START;
        final int length = input.length();
        for( int i = 0; i < length; i++ )
        {
            final char ch = input.charAt( i );
            switch( state )
            {
                case PARSE_KEY_START:
                    if( Character.isWhitespace( ch ) )
                    {
                        continue;
                    }
                    else if( Character.isJavaIdentifierStart( ch ) )
                    {
                        key.append( ch );
                        state = PARSE_KEY;
                    }
                    else
                    {
                        return null;
                    }
                    break;

                case PARSE_KEY:
                    if( '=' == ch )
                    {
                        state = PARSE_VALUE_START;
                    }
                    else if( Character.isJavaIdentifierPart( ch ) )
                    {
                        key.append( ch );
                    }
                    else
                    {
                        return null;
                    }
                    break;

                case PARSE_VALUE_START:
                    if( '\"' != ch )
                    {
                        return null;
                    }
                    else
                    {
                        state = PARSE_VALUE;
                    }
                    break;

                case PARSE_VALUE:
                    if( '\"' == ch )
                    {
                        state = PARSE_END;
                        parameters.setProperty( key.toString(), value.toString() );
                        key.setLength( 0 );
                        value.setLength( 0 );
                    }
                    else
                    {
                        value.append( ch );
                    }
                    break;

                case PARSE_END:
                    if( Character.isWhitespace( ch ) )
                    {
                        state = PARSE_KEY_START;
                    }
                    else
                    {
                        return null;
                    }
                    break;
            }
        }

        if( PARSE_KEY_START != state &&
            PARSE_END != state )
        {
            return null;
        }

        return parameters;
    }
}
