/*
 * Copyright (C) The Spice Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */
package org.realityforge.metaclass.io;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Properties;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.sax.SAXTransformerFactory;
import javax.xml.transform.sax.TransformerHandler;
import javax.xml.transform.stream.StreamResult;

import org.realityforge.metaclass.model.ClassDescriptor;
import org.xml.sax.SAXException;

/**
 * This is a utility class that writes out the ClassDescriptor
 * to a stream using the xml format outlined in documentation.
 *
 * @author <a href="mailto:peter at realityforge.org">Peter Donald</a>
 * @version $Revision: 1.1 $ $Date: 2003-10-28 02:18:46 $
 */
public class MetaClassIOXml
   implements MetaClassIO
{
   /**
    * The current version of ClassDescriptor XML format.
    */
   static final String VERSION = "1.0";

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
      throw new IOException( "Not supported yet!" );
   }

   /**
    * @see MetaClassIO#serializeClass
    */
   public void serializeClass( final OutputStream output,
                               final ClassDescriptor descriptor )
      throws IOException
   {
      final StreamResult result = new StreamResult( output );
      final SAXTransformerFactory factory =
         (SAXTransformerFactory) TransformerFactory.newInstance();
      final TransformerHandler handler;
      try
      {
         handler = factory.newTransformerHandler();
      }
      catch ( final Exception e )
      {
         throw new IOException( e.toString() );
      }

      final Properties format = new Properties();
      format.put( OutputKeys.METHOD, "xml" );
      format.put( OutputKeys.INDENT, "yes" );
      handler.setResult( result );
      handler.getTransformer().setOutputProperties( format );

      final SAXMetaClassSerializer serializer = new SAXMetaClassSerializer();
      try
      {
         serializer.serialize( descriptor, handler );
      }
      catch( final SAXException se )
      {
         throw new IOException( se.getMessage() );
      }
      finally
      {
         output.flush();
      }
   }
}
