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
import org.realityforge.metaclass.model.ParameterDescriptor;

/**
 * This is a utility class that writes out a Attributes object
 * to a stream using binary format outlined in documentation.
 *
 * @author <a href="mailto:peter at realityforge.org">Peter Donald</a>
 * @author <a href="mailto:doug at doug@stocksoftware.com.au">Doug Hagan</a>
 * @version $Revision: 1.4 $ $Date: 2003-08-15 06:53:33 $
 */
public class MetaClassIOBinary
   implements MetaClassIO
{
   /**
    * The current version of Attributes object.
    */
   private static final int VERSION = 100;

   /**
    * Read a ClassDescriptor from an input stream.
    *
    * @param input the input stream
    * @return the ClassDescriptor
    * @throws IOException if unable ot read class descriptor
    */
   public ClassDescriptor deserialize( final InputStream input )
      throws IOException
   {
      final DataInputStream data = new DataInputStream( input );

      final int version = data.readInt();
      if ( VERSION != version )
      {
         final String message =
            "Version mismatch." +
            " Expected: " + VERSION +
            " Actual: " + version;
         throw new IOException( message );
      }
      final String classname = data.readUTF();
      final int classModifiers = data.readInt();
      final Attribute[] classAttributes = readAttributes( data );

      final int fieldCount = data.readInt();
      final ArrayList fieldSet = new ArrayList();
      for ( int i = 0; i < fieldCount; i++ )
      {
         final String name = data.readUTF();
         final String type = data.readUTF();
         final int modifiers = data.readInt();
         final Attribute[] attributes = readAttributes( data );
         final FieldDescriptor field =
            new FieldDescriptor( name, type, modifiers, attributes );
         fieldSet.add( field );
      }

      final int methodCount = data.readInt();
      final ArrayList methodSet = new ArrayList();
      for ( int i = 0; i < methodCount; i++ )
      {
         final String name = data.readUTF();
         final String type = data.readUTF();
         final ParameterDescriptor[] parameters = readParameters( data );
         final int modifiers = data.readInt();
         final Attribute[] attributes = readAttributes( data );
         final MethodDescriptor method =
            new MethodDescriptor( name, type,
                                  modifiers, parameters,
                                  attributes );
         methodSet.add( method );
      }

      final FieldDescriptor[] fields =
         (FieldDescriptor[]) fieldSet.toArray( new FieldDescriptor[ fieldSet.size() ] );
      final MethodDescriptor[] methods =
         (MethodDescriptor[]) methodSet.toArray( new MethodDescriptor[ methodSet.size() ] );

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
   public void serialize( final OutputStream output, final ClassDescriptor info )
      throws IOException
   {
      final DataOutputStream data = new DataOutputStream( output );
      try
      {
         data.writeInt( VERSION );
         data.writeUTF( info.getName() );
         data.writeInt( info.getModifiers() );
         writeAttributes( data, info.getAttributes() );

         final FieldDescriptor[] fields = info.getFields();
         data.writeInt( fields.length );
         for ( int i = 0; i < fields.length; i++ )
         {
            final FieldDescriptor field = fields[ i ];
            data.writeUTF( field.getName() );
            data.writeUTF( field.getType() );
            data.writeInt( field.getModifiers() );
            writeAttributes( data, field.getAttributes() );
         }

         final MethodDescriptor[] methods = info.getMethods();
         data.writeInt( methods.length );
         for ( int i = 0; i < methods.length; i++ )
         {
            final MethodDescriptor method = methods[ i ];
            data.writeUTF( method.getName() );
            data.writeUTF( method.getReturnType() );
            writeParameters( data, method.getParameters() );
            data.writeInt( method.getModifiers() );
            writeAttributes( data, method.getAttributes() );
         }
      }
      finally
      {
         data.flush();
      }
   }

   /**
    * Read in a set of method parameters.
    *
    * @param data the input
    * @return the method parameters
    * @throws IOException if unable to read parameters
    */
   private ParameterDescriptor[] readParameters( final DataInputStream data )
      throws IOException
   {
      final ArrayList parameters = new ArrayList();
      final int count = data.readInt();
      for ( int i = 0; i < count; i++ )
      {
         final String name = data.readUTF();
         final String type = data.readUTF();
         final ParameterDescriptor parameter =
            new ParameterDescriptor( name, type );
         parameters.add( parameter );
      }
      final ParameterDescriptor[] parameterDescriptorArray =
         new ParameterDescriptor[ parameters.size() ];
      return (ParameterDescriptor[]) parameters.toArray( parameterDescriptorArray );
   }

   /**
    * Write out a set of method parameters.
    *
    * @param data the output stream
    * @param parameters the method parameters
    * @throws IOException if unable to write parameters
    */
   private void writeParameters( final DataOutputStream data,
                                 final ParameterDescriptor[] parameters )
      throws IOException
   {
      data.writeInt( parameters.length );
      for ( int i = 0; i < parameters.length; i++ )
      {
         final ParameterDescriptor parameter = parameters[ i ];
         data.writeUTF( parameter.getName() );
         data.writeUTF( parameter.getType() );
      }
   }

   /**
    * Read in a set of attributes.
    *
    * @param data the input stream
    * @return the attributes
    * @throws IOException if unable to read attributes
    */
   private Attribute[] readAttributes( final DataInputStream data )
      throws IOException
   {
      final int count = data.readInt();
      final ArrayList attributeSet = new ArrayList();
      for ( int i = 0; i < count; i++ )
      {
         final String name = data.readUTF();
         final String value = data.readUTF();
         final Properties properties = readAttributeParameters( data );
         Attribute attribute = null;

         final boolean valuePresent = null != value && value.length() > 0;
         if ( valuePresent &&
            properties.size() > 0 )
         {
            final String message =
               "Cannot read attributes containing both " +
               "text and parameters.";
            throw new IOException( message );
         }

         if ( valuePresent )
         {
            attribute = new Attribute( name, value );
         }
         else
         {
            attribute = new Attribute( name, properties );
         }
         attributeSet.add( attribute );
      }

      return (Attribute[]) attributeSet.toArray( new Attribute[ attributeSet.size() ] );
   }

   /**
    * Read in a set of attribute parameters.
    *
    * @param data the input
    * @return the parameters
    * @throws IOException if unable to read attribute parameters
    */
   private Properties readAttributeParameters( final DataInputStream data )
      throws IOException
   {
      final Properties parameters = new Properties();

      final int count = data.readInt();
      for ( int i = 0; i < count; i++ )
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
   private void writeAttributes( final DataOutputStream data,
                                 final Attribute[] attributes )
      throws IOException
   {
      data.writeInt( attributes.length );
      for ( int i = 0; i < attributes.length; i++ )
      {
         final Attribute attribute = attributes[ i ];
         data.writeUTF( attribute.getName() );

         final String value = attribute.getValue();
         if ( null != value &&
            null != attribute.getParameters() )
         {
            throw new IOException( "Cannot write attributes containing both " +
                                   "text and parameters." );
         }

         if ( null != value )
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
   private void writeAttributeParameters( final DataOutputStream data,
                                          final Attribute attribute )
      throws IOException
   {
      if ( null == attribute )
      {
         data.writeInt( 0 );
      }
      else
      {
         final String[] names = attribute.getParameterNames();
         data.writeInt( names.length );
         for ( int i = 0; i < names.length; i++ )
         {
            final String name = names[ i ];
            final String value = attribute.getParameter( name );
            data.writeUTF( name );
            data.writeUTF( value );
         }
      }
   }
}
