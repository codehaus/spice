/*
 * Copyright (C) The Spice Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */
package org.realityforge.metaclass.io;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Properties;
import org.realityforge.metaclass.model.Attribute;
import org.realityforge.metaclass.model.ClassDescriptor;
import org.realityforge.metaclass.model.FieldDescriptor;
import org.realityforge.metaclass.model.MethodDescriptor;
import org.realityforge.metaclass.model.PackageDescriptor;
import org.realityforge.metaclass.model.ParameterDescriptor;

/**
 * This is a utility class that writes out a Attributes object
 * to a stream using binary format outlined in documentation.
 *
 * @author <a href="mailto:peter at realityforge.org">Peter Donald</a>
 * @author <a href="mailto:doug at doug@stocksoftware.com.au">Doug Hagan</a>
 * @version $Revision: 1.12 $ $Date: 2003-08-22 03:36:29 $
 */
public class MetaClassIOBinary
    implements MetaClassIO
{
    /**
     * The current version of Attributes object.
     */
    private static final int VERSION = 100;

    /**
     * Read a PackageDescriptor from an input stream.
     *
     * @param input the input stream
     * @return the PackageDescriptor
     * @throws IOException if unable ot read class descriptor
     */
    public PackageDescriptor deserializePackage( InputStream input )
        throws IOException
    {
        final DataInputStream data = new DataInputStream( input );
        checkVersionHeader( data );
        final String name = data.readUTF();
        final Attribute[] attributes = readAttributes( data );

        return new PackageDescriptor( name, attributes );
    }

    /**
     * Write a PackageDescriptor to an output stream.
     *
     * @param output the stream to write class descriptor out to
     * @param info the PackageDescriptor to write out
     * @throws IOException if unable ot write class descriptor
     */
    public void serializePackage( final OutputStream output,
                                  final PackageDescriptor info )
        throws IOException
    {
        final DataOutputStream data = new DataOutputStream( output );
        try
        {
            data.writeInt( VERSION );
            data.writeUTF( info.getName() );
            writeAttributes( data, info.getAttributes() );
        }
        finally
        {
            data.flush();
        }
    }

    /**
     * Read a ClassDescriptor from an input stream.
     *
     * @param input the input stream
     * @return the ClassDescriptor
     * @throws IOException if unable ot read class descriptor
     */
    public ClassDescriptor deserializeClass( final InputStream input )
        throws IOException
    {
        final DataInputStream data = new DataInputStream( input );
        checkVersionHeader( data );
        final String classname = data.readUTF();
        final int classModifiers = data.readInt();
        final Attribute[] classAttributes = readAttributes( data );

        final FieldDescriptor[] fields = readFields( data );
        final MethodDescriptor[] methods = readMethods( data );

        return
            new ClassDescriptor( classname, classModifiers,
                                 classAttributes, fields,
                                 methods );
    }

    /**
     * Write a ClassDescriptor to an output stream.
     *
     * @param output the stream to write class descriptor out to
     * @param info the ClassDescriptor to write out
     * @throws IOException if unable ot write class descriptor
     */
    public void serializeClass( final OutputStream output, final ClassDescriptor info )
        throws IOException
    {
        final DataOutputStream data = new DataOutputStream( output );
        try
        {
            data.writeInt( VERSION );
            data.writeUTF( info.getName() );
            data.writeInt( info.getModifiers() );
            writeAttributes( data, info.getAttributes() );
            writeFields( data, info.getFields() );
            writeMethods( data, info.getMethods() );
        }
        finally
        {
            data.flush();
        }
    }

    /**
     * Write out a set of fields.
     *
     * @param data the output stream
     * @param fields the fields
     * @throws IOException if unable to write fields
     */
    void writeFields( final DataOutputStream data,
                      final FieldDescriptor[] fields )
        throws IOException
    {
        data.writeInt( fields.length );
        for( int i = 0; i < fields.length; i++ )
        {
            final FieldDescriptor field = fields[ i ];
            writeField( data, field );
        }
    }

    /**
     * Write out a field.
     *
     * @param data the output stream
     * @param field the field
     * @throws IOException if unable to write field
     */
    private void writeField( final DataOutputStream data,
                             final FieldDescriptor field )
        throws IOException
    {
        data.writeUTF( field.getName() );
        data.writeUTF( field.getType() );
        data.writeInt( field.getModifiers() );
        writeAttributes( data, field.getAttributes() );
    }

    /**
     * Write out a set of methods.
     *
     * @param data the output stream
     * @param methods the methods
     * @throws IOException if unable to write methods
     */
    void writeMethods( final DataOutputStream data,
                       final MethodDescriptor[] methods )
        throws IOException
    {
        data.writeInt( methods.length );
        for( int i = 0; i < methods.length; i++ )
        {
            final MethodDescriptor method = methods[ i ];
            writeMethod( data, method );
        }
    }

    /**
     * Write out a method.
     *
     * @param data the output stream
     * @param method the method
     * @throws IOException if unable to write method
     */
    private void writeMethod( final DataOutputStream data,
                              final MethodDescriptor method )
        throws IOException
    {
        data.writeUTF( method.getName() );
        data.writeUTF( method.getReturnType() );
        writeParameters( data, method.getParameters() );
        data.writeInt( method.getModifiers() );
        writeAttributes( data, method.getAttributes() );
    }

    /**
     * Read in a set of methods.
     *
     * @param data the input
     * @return the methods
     * @throws IOException if unable to read methods
     */
    MethodDescriptor[] readMethods( final DataInputStream data )
        throws IOException
    {
        final int methodCount = data.readInt();
        final ArrayList methodSet = new ArrayList();
        for( int i = 0; i < methodCount; i++ )
        {
            methodSet.add( readMethod( data ) );
        }

        return (MethodDescriptor[])methodSet.
            toArray( new MethodDescriptor[ methodSet.size() ] );
    }

    /**
     * Read in a method.
     *
     * @param data the input
     * @return the method
     * @throws IOException if unable to read method
     */
    private MethodDescriptor readMethod( final DataInputStream data )
        throws IOException
    {
        final String name = data.readUTF();
        final String type = data.readUTF();
        final ParameterDescriptor[] parameters = readParameters( data );
        final int modifiers = data.readInt();
        final Attribute[] attributes = readAttributes( data );
        return
            new MethodDescriptor( name, type,
                                  modifiers, parameters,
                                  attributes );
    }

    /**
     * Read in a set of fields.
     *
     * @param data the input
     * @return the fields
     * @throws IOException if unable to read fields
     */
    FieldDescriptor[] readFields( final DataInputStream data ) throws IOException
    {
        final int fieldCount = data.readInt();
        final ArrayList fieldSet = new ArrayList();
        for( int i = 0; i < fieldCount; i++ )
        {
            fieldSet.add( readField( data ) );
        }
        return (FieldDescriptor[])fieldSet.
            toArray( new FieldDescriptor[ fieldSet.size() ] );
    }

    /**
     * Read in a field.
     *
     * @param data the input
     * @return the field
     * @throws IOException if unable to read field
     */
    private FieldDescriptor readField( final DataInputStream data )
        throws IOException
    {
        final String name = data.readUTF();
        final String type = data.readUTF();
        final int modifiers = data.readInt();
        final Attribute[] attributes = readAttributes( data );
        return new FieldDescriptor( name, type, modifiers, attributes );
    }

    /**
     * Read in a set of method parameters.
     *
     * @param data the input
     * @return the method parameters
     * @throws IOException if unable to read parameters
     */
    ParameterDescriptor[] readParameters( final DataInputStream data )
        throws IOException
    {
        final ArrayList parameters = new ArrayList();
        final int count = data.readInt();
        for( int i = 0; i < count; i++ )
        {
            parameters.add( readParameter( data ) );
        }
        final ParameterDescriptor[] parameterDescriptorArray =
            new ParameterDescriptor[ parameters.size() ];
        return (ParameterDescriptor[])parameters.toArray( parameterDescriptorArray );
    }

    /**
     * Read in a method parameter.
     *
     * @param data the input
     * @return the method parameter
     * @throws IOException if unable to read parameter
     */
    private ParameterDescriptor readParameter( final DataInputStream data )
        throws IOException
    {
        final String name = data.readUTF();
        final String type = data.readUTF();
        return new ParameterDescriptor( name, type );
    }

    /**
     * Write out a set of method parameters.
     *
     * @param data the output stream
     * @param parameters the method parameters
     * @throws IOException if unable to write parameters
     */
    void writeParameters( final DataOutputStream data,
                          final ParameterDescriptor[] parameters )
        throws IOException
    {
        data.writeInt( parameters.length );
        for( int i = 0; i < parameters.length; i++ )
        {
            final ParameterDescriptor parameter = parameters[ i ];
            writeParameter( data, parameter );
        }
    }

    /**
     * Write out a method parameter.
     *
     * @param data the output stream
     * @param parameter the method parameter
     * @throws IOException if unable to write parameter
     */
    private void writeParameter( final DataOutputStream data,
                                 final ParameterDescriptor parameter )
        throws IOException
    {
        data.writeUTF( parameter.getName() );
        data.writeUTF( parameter.getType() );
    }

    /**
     * Read in a set of attributes.
     *
     * @param data the input stream
     * @return the attributes
     * @throws IOException if unable to read attributes
     */
    Attribute[] readAttributes( final DataInputStream data )
        throws IOException
    {
        final int count = data.readInt();
        final ArrayList attributeSet = new ArrayList();
        for( int i = 0; i < count; i++ )
        {
            final String name = data.readUTF();
            final String value = data.readUTF();
            final Properties properties = readAttributeParameters( data );
            Attribute attribute = null;

            final boolean valuePresent = null != value && value.length() > 0;
            if( valuePresent &&
                properties.size() > 0 )
            {
                final String message =
                    "Cannot read attributes containing both " +
                    "text and parameters.";
                throw new IOException( message );
            }

            if( valuePresent )
            {
                attribute = new Attribute( name, value );
            }
            else
            {
                attribute = new Attribute( name, properties );
            }
            attributeSet.add( attribute );
        }

        return (Attribute[])attributeSet.toArray( new Attribute[ attributeSet.size() ] );
    }

    /**
     * Read in a set of attribute parameters.
     *
     * @param data the input
     * @return the parameters
     * @throws IOException if unable to read attribute parameters
     */
    Properties readAttributeParameters( final DataInputStream data )
        throws IOException
    {
        final Properties parameters = new Properties();

        final int count = data.readInt();
        for( int i = 0; i < count; i++ )
        {
            final String name = data.readUTF();
            final String value = data.readUTF();
            parameters.setProperty( name, value );
        }

        return parameters;
    }

    /**
     * Write out the specified attributes.
     *
     * @param data the output
     * @param attributes the attributes
     * @throws IOException if unable to write attributes
     */
    void writeAttributes( final DataOutputStream data,
                          final Attribute[] attributes )
        throws IOException
    {
        data.writeInt( attributes.length );
        for( int i = 0; i < attributes.length; i++ )
        {
            final Attribute attribute = attributes[ i ];
            data.writeUTF( attribute.getName() );

            final String value = attribute.getValue();
            if( null != value )
            {
                data.writeUTF( value );
                writeAttributeParameters( data, null );
            }
            else
            {
                data.writeUTF( "" );
                writeAttributeParameters( data, attribute );
            }
        }
    }

    /**
     * Write out the parameters of an attribute.
     *
     * @param data the output
     * @param attribute the attribute
     * @throws IOException if unable to write attribute parameters
     */
    void writeAttributeParameters( final DataOutputStream data,
                                   final Attribute attribute )
        throws IOException
    {
        if( null == attribute )
        {
            data.writeInt( 0 );
        }
        else
        {
            final String[] names = attribute.getParameterNames();
            data.writeInt( names.length );
            for( int i = 0; i < names.length; i++ )
            {
                final String name = names[ i ];
                final String value = attribute.getParameter( name );
                data.writeUTF( name );
                data.writeUTF( value );
            }
        }
    }

    /**
     * Read version header of descriptor to make sure it is something
     * we can handle and if not throw an exception.
     *
     * @param data the input stream
     * @throws IOException if unable to handle version
     */
    private void checkVersionHeader( final DataInputStream data )
        throws IOException
    {
        final int version = data.readInt();
        if( VERSION != version )
        {
            final String message =
                "Version mismatch." +
                " Expected: " + VERSION +
                " Actual: " + version;
            throw new IOException( message );
        }
    }
}
