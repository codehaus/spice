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
 * @version $Revision: 1.7 $ $Date: 2003-10-29 10:34:13 $
 */
public class MetaClassIOXml
    implements MetaClassIO
{
    /**
     * The current version of ClassDescriptor XML format.
     */
    static final String VERSION = "1.0";

    /**
     * Constant for name of class element.
     */
    static final String CLASS_ELEMENT = "class";

    /**
     * Constant for name of fields element.
     */
    static final String FIELDS_ELEMENT = "fields";

    /**
     * Constant for name of field element.
     */
    static final String FIELD_ELEMENT = "field";

    /**
     * Constant for name of methods element.
     */
    static final String METHODS_ELEMENT = "methods";

    /**
     * Constant for name of method element.
     */
    static final String METHOD_ELEMENT = "method";

    /**
     * Constant for name of method parameters group element.
     */
    static final String PARAMETERS_ELEMENT = "parameters";

    /**
     * Constant for name of method parameters element.
     */
    static final String PARAMETER_ELEMENT = "parameter";

    /**
     * Constant for name of attributes element.
     */
    static final String ATTRIBUTES_ELEMENT = "attributes";

    /**
     * Constant for name of attribute element.
     */
    static final String ATTRIBUTE_ELEMENT = "attribute";

    /**
     * Constant for name of attribute parameter element.
     */
    static final String PARAM_ELEMENT = "param";

    /**
     * Constant for name of name attribute.
     */
    static final String NAME_ATTRIBUTE = "name";

    /**
     * Constant for name of type attribute.
     */
    static final String TYPE_ATTRIBUTE = "type";

    /**
     * Constant for name of value attribute.
     */
    static final String VALUE_ATTRIBUTE = "value";

    /**
     * Constant for name of version attribute.
     */
    static final String VERSION_ATTRIBUTE = "version";

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
