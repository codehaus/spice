/*
 * Copyright (C) The Spice Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */
package org.realityforge.metaclass.io;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.DataInputStream;
import java.lang.reflect.Modifier;
import java.util.Properties;
import junit.framework.TestCase;
import org.realityforge.metaclass.model.Attribute;
import org.realityforge.metaclass.model.ClassDescriptor;
import org.realityforge.metaclass.model.FieldDescriptor;
import org.realityforge.metaclass.model.MethodDescriptor;
import org.realityforge.metaclass.model.ParameterDescriptor;

/**
 *
 * @author <a href="mailto:peter at realityforge.org">Peter Donald</a>
 * @version $Revision: 1.5 $ $Date: 2003-08-22 02:53:48 $
 */
public class MetaClassIOBinaryTestCase
    extends TestCase
{
    private static final int STRING_HEADER_SIZE = 2;

    public void testBinaryIOWriteZeroAttributes()
        throws Exception
    {
        final MetaClassIOBinary io = new MetaClassIOBinary();
        final ByteArrayOutputStream out = new ByteArrayOutputStream();
        final DataOutputStream data = new DataOutputStream( out );
        io.writeAttributes( data, Attribute.EMPTY_SET );
        data.flush();
        final byte[] bytes = out.toByteArray();
        assertEquals( "length", 4, bytes.length );
        assertEquals( "bytes[0-4] = 0", 0, readInteger( bytes, 0 ) );
    }

    public void testBinaryIOReadZeroAttributes()
        throws Exception
    {
        final byte[] bytes = new byte[]
        {
            0, 0, 0, 0 //length
        };
        final MetaClassIOBinary io = new MetaClassIOBinary();
        final ByteArrayInputStream in = new ByteArrayInputStream( bytes );
        final DataInputStream data = new DataInputStream( in );
        final Attribute[] attributes = io.readAttributes( data );
        assertEquals( "attributes.length", 0, attributes.length );
    }

    public void testBinaryIOWriteAttributeWithoutValueOrParameters()
        throws Exception
    {
        final MetaClassIOBinary io = new MetaClassIOBinary();
        final ByteArrayOutputStream out = new ByteArrayOutputStream();
        final DataOutputStream data = new DataOutputStream( out );
        final String name = "mx.attribute";
        final Attribute attribute = new Attribute( name );
        io.writeAttributes( data, new Attribute[]{attribute} );
        data.flush();
        final byte[] bytes = out.toByteArray();
        assertEquals( "length", 24, bytes.length );
        int offset = 0;
        assertEquals( "bytes[" + offset + "] = 1", 1, readInteger( bytes, offset ) );
        offset = 4;
        assertEquals( "bytes[" + offset + "] = " + name, name, readString( bytes, offset ) );
        offset += STRING_HEADER_SIZE + name.length();
        assertEquals( "bytes[" + offset + "] = " + "''", "", readString( bytes, offset ) );
        offset += STRING_HEADER_SIZE;
        assertEquals( "bytes[" + offset + "] = 0", 0, readInteger( bytes, offset ) );
    }

    public void testBinaryIOReadAttributeWithoutValueOrParameters()
        throws Exception
    {
        final String name = "name";
        final String value = "";
        final int paramCount = 0;
        final byte[] bytes = new byte[]
        {
            0, 0, 0, 1, //length
            0, 4, //length of name
            'n', 'a', 'm', 'e',
            0, 0, //length of value
            0, 0, 0, 0 //count of params

        };
        final MetaClassIOBinary io = new MetaClassIOBinary();
        final ByteArrayInputStream in = new ByteArrayInputStream( bytes );
        final DataInputStream data = new DataInputStream( in );
        final Attribute[] attributes = io.readAttributes( data );
        assertEquals( "attributes.length", 1, attributes.length );
        assertEquals( "attributes[0].name", name, attributes[ 0 ].getName() );
        assertEquals( "attributes[0].value", null, attributes[ 0 ].getValue() );
        assertEquals( "attributes[0].parameterCount", 0, attributes[ 0 ].getParameterCount() );
    }

    public void testBinaryIOWriteAttributeWithValue()
        throws Exception
    {
        final MetaClassIOBinary io = new MetaClassIOBinary();
        final ByteArrayOutputStream out = new ByteArrayOutputStream();
        final DataOutputStream data = new DataOutputStream( out );
        final String name = "mx.attribute";
        final String value = "This is an attribute";
        final Attribute attribute = new Attribute( name, value );
        io.writeAttributes( data, new Attribute[]{attribute} );
        data.flush();
        final byte[] bytes = out.toByteArray();
        assertEquals( "length", 44, bytes.length );
        int offset = 0;
        assertEquals( "bytes[" + offset + "] = 1", 1, readInteger( bytes, offset ) );
        offset = 4;
        assertEquals( "bytes[" + offset + "] = " + name, name, readString( bytes, offset ) );
        offset += STRING_HEADER_SIZE + name.length();
        assertEquals( "bytes[" + offset + "] = " + value, value, readString( bytes, offset ) );
        offset += STRING_HEADER_SIZE + value.length();
        assertEquals( "bytes[" + offset + "] = 0", 0, readInteger( bytes, offset ) );
    }

    public void testBinaryIOWriteAttributeWithParameters()
        throws Exception
    {
        final MetaClassIOBinary io = new MetaClassIOBinary();
        final ByteArrayOutputStream out = new ByteArrayOutputStream();
        final DataOutputStream data = new DataOutputStream( out );
        final String name = "mx.attribute";

        final Properties parameters = new Properties();
        final String paramKey = "magic";
        final String paramValue = "lots";
        parameters.setProperty( paramKey, paramValue );
        final Attribute attribute = new Attribute( name, parameters );
        io.writeAttributes( data, new Attribute[]{attribute} );
        data.flush();
        final byte[] bytes = out.toByteArray();
        outputArray( bytes );
        assertEquals( "length", 37, bytes.length );
        int offset = 0;
        assertEquals( "bytes[" + offset + "] = 1", 1, readInteger( bytes, offset ) );
        offset = 4;
        assertEquals( "bytes[" + offset + "] = " + name, name, readString( bytes, offset ) );
        offset += STRING_HEADER_SIZE + name.length();
        assertEquals( "bytes[" + offset + "] = " + "''", "", readString( bytes, offset ) );
        offset += STRING_HEADER_SIZE;
        assertEquals( "bytes[" + offset + "] = 1", 1, readInteger( bytes, offset ) );
        offset += 4;
        assertEquals( "bytes[" + offset + "] = " + paramKey, paramKey, readString( bytes, offset ) );
        offset += STRING_HEADER_SIZE + paramKey.length();
        assertEquals( "bytes[" + offset + "] = " + paramValue, paramValue, readString( bytes, offset ) );
    }

    public void testBinaryIOWriteParameters()
        throws Exception
    {
        final MetaClassIOBinary io = new MetaClassIOBinary();
        final ByteArrayOutputStream out = new ByteArrayOutputStream();
        final DataOutputStream data = new DataOutputStream( out );
        final String name = "name";
        final String type = "aType";
        final ParameterDescriptor descriptor = new ParameterDescriptor( name, type );
        io.writeParameters( data, new ParameterDescriptor[]{descriptor} );
        data.flush();
        final byte[] bytes = out.toByteArray();
        assertEquals( "length", 17, bytes.length );
        int offset = 0;
        assertEquals( "bytes[" + offset + "] = 1", 1, readInteger( bytes, offset ) );
        offset = 4;
        assertEquals( "bytes[" + offset + "] = " + name, name, readString( bytes, offset ) );
        offset += STRING_HEADER_SIZE + name.length();
        assertEquals( "bytes[" + offset + "] = " + type, type, readString( bytes, offset ) );
        offset += STRING_HEADER_SIZE + type.length();
    }

    public void testBinaryIOWriteZeroParameters()
        throws Exception
    {
        final MetaClassIOBinary io = new MetaClassIOBinary();
        final ByteArrayOutputStream out = new ByteArrayOutputStream();
        final DataOutputStream data = new DataOutputStream( out );
        io.writeParameters( data, ParameterDescriptor.EMPTY_SET );
        data.flush();
        final byte[] bytes = out.toByteArray();
        assertEquals( "length", 4, bytes.length );
        assertEquals( "bytes[0-4] = 0", 0, readInteger( bytes, 0 ) );
    }

    public void testBinaryIOWriteRead()
        throws Exception
    {
        final Properties parameters = new Properties();
        parameters.setProperty( "column", "field" );
        final Attribute paramAttribute =
            new Attribute( "persist", parameters );
        final Attribute valueAttribute =
            new Attribute( "mx.attribute", "This is an attribute" );
        final Attribute[] attributes = new Attribute[]{valueAttribute, paramAttribute};
        final FieldDescriptor field =
            new FieldDescriptor( "m_field",
                                 "int",
                                 Modifier.PUBLIC,
                                 attributes );
        final MethodDescriptor method =
            new MethodDescriptor( "doMagic",
                                  "",
                                  Modifier.PROTECTED,
                                  ParameterDescriptor.EMPTY_SET,
                                  Attribute.EMPTY_SET );
        final ClassDescriptor descriptor =
            new ClassDescriptor( "com.biz.MyClass",
                                 Modifier.FINAL,
                                 Attribute.EMPTY_SET,
                                 new FieldDescriptor[]{field},
                                 new MethodDescriptor[]{method} );
        final MetaClassIOBinary io = new MetaClassIOBinary();
        final ByteArrayOutputStream out = new ByteArrayOutputStream();
        io.serializeClass( out, descriptor );

        final ByteArrayInputStream in = new ByteArrayInputStream( out.toByteArray() );
        io.deserializeClass( in );
    }

    private int readShort( final byte[] data, final int offset )
    {
        return (
            ( ( data[ offset + 1 ] & 0xff ) << 0 ) +
            ( ( data[ offset + 0 ] & 0xff ) << 8 ) );
    }

    private int readInteger( final byte[] data, final int offset )
    {
        return (
            ( ( data[ offset + 3 ] & 0xff ) << 0 ) +
            ( ( data[ offset + 2 ] & 0xff ) << 8 ) +
            ( ( data[ offset + 1 ] & 0xff ) << 16 ) +
            ( ( data[ offset + 0 ] & 0xff ) << 24 ) );
    }

    private String readString( final byte[] data, final int offset )
    {
        final int count = readShort( data, offset );
        return new String( data, offset + STRING_HEADER_SIZE, count );
    }

    /**
     * Method during test development to output array contents.
     *
     * @param bytes the byte array
     */
    protected final void outputArray( final byte[] bytes )
    {
        outputArrayAsInts( bytes );
        outputArrayAsChars( bytes );
        System.out.println( "Length = " + bytes.length );
    }

    private void outputArrayAsInts( final byte[] bytes )
    {
        for( int i = 0; i < bytes.length; i++ )
        {
            System.out.print( bytes[ i ] + ", " );
            if( 0 != i && i % 10 == 0 )
            {
                System.out.println();
            }
        }
        System.out.println();
    }

    private void outputArrayAsChars( final byte[] bytes )
    {
        for( int i = 0; i < bytes.length; i++ )
        {
            System.out.print( ( (char)bytes[ i ] ) + ", " );
            if( 0 != i && i % 10 == 0 )
            {
                System.out.println();
            }
        }
        System.out.println();
    }
}
