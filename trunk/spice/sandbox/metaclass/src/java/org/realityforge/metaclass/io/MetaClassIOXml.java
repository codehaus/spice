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
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import org.realityforge.metaclass.model.ClassDescriptor;
import org.xml.sax.SAXException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * This is a utility class that writes out the ClassDescriptor
 * to a stream using the xml format outlined in documentation.
 *
 * @author <a href="mailto:peter at realityforge.org">Peter Donald</a>
 * @version $Revision: 1.6 $ $Date: 2003-10-29 10:28:02 $
 */
public class MetaClassIOXml
    implements MetaClassIO
{
    /**
     * The current version of ClassDescriptor XML format.
     */
    static final String VERSION = "1.0";

    /**
     * @see MetaClassIO#deserializeClass
     */
    public ClassDescriptor deserializeClass( final InputStream input )
        throws IOException
    {
        final DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        try
        {
            final DocumentBuilder builder = factory.newDocumentBuilder();
            final Document document = builder.parse( input );
            final DOMMetaClassDeserializer deserializer = new DOMMetaClassDeserializer();
            final Element element = document.getDocumentElement();
            return deserializer.buildClassDescriptor( element );
        }
        catch( final Exception e )
        {
            throw new IOException( e.getMessage() );
        }
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
            (SAXTransformerFactory)TransformerFactory.newInstance();
        final TransformerHandler handler;
        try
        {
            handler = factory.newTransformerHandler();
        }
        catch( final Exception e )
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
            serializer.serialize( handler, descriptor );
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
