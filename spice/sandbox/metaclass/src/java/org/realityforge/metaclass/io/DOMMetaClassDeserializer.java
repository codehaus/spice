/*
 * Copyright (C) The Spice Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */
package org.realityforge.metaclass.io;

import java.util.ArrayList;
import java.util.Properties;
import org.realityforge.metaclass.model.Attribute;
import org.realityforge.metaclass.model.ClassDescriptor;
import org.realityforge.metaclass.model.FieldDescriptor;
import org.realityforge.metaclass.model.MethodDescriptor;
import org.realityforge.metaclass.model.ParameterDescriptor;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 *
 * @author <a href="mailto:peter at realityforge.org">Peter Donald</a>
 * @version $Revision: 1.3 $ $Date: 2003-10-29 10:34:12 $
 */
public class DOMMetaClassDeserializer
{
    public ClassDescriptor buildClassDescriptor( final Element element )
        throws Exception
    {
        expectElement( element, MetaClassIOXml.CLASS_ELEMENT );
        final String type =
            expectAttribute( element, MetaClassIOXml.TYPE_ATTRIBUTE );
        Attribute[] attributes = Attribute.EMPTY_SET;
        MethodDescriptor[] methods = MethodDescriptor.EMPTY_SET;
        FieldDescriptor[] fields = FieldDescriptor.EMPTY_SET;
        final NodeList nodes = element.getChildNodes();
        final int length = nodes.getLength();
        for( int i = 0; i < length; i++ )
        {
            final Node node = nodes.item( i );
            final short nodeType = node.getNodeType();
            if( nodeType == Node.ELEMENT_NODE )
            {
                final Element child = (Element)node;
                final String childName = child.getNodeName();
                if( childName.equals( MetaClassIOXml.METHODS_ELEMENT ) )
                {
                    methods = buildMethods( child );
                }
                else if( childName.equals( MetaClassIOXml.FIELDS_ELEMENT ) )
                {
                    fields = buildFields( child );
                }
                else
                {
                    attributes = buildAttributes( child );
                }
            }
        }
        return new ClassDescriptor( type,
                                    attributes,
                                    attributes,
                                    fields,
                                    methods );
    }

    MethodDescriptor[] buildMethods( final Element element )
        throws Exception
    {
        expectElement( element, MetaClassIOXml.METHODS_ELEMENT );

        final ArrayList methods = new ArrayList();
        final NodeList nodes = element.getChildNodes();
        final int length = nodes.getLength();
        for( int i = 0; i < length; i++ )
        {
            final Node node = nodes.item( i );
            final short nodeType = node.getNodeType();
            if( nodeType == Node.ELEMENT_NODE )
            {
                final MethodDescriptor field = buildMethod( (Element)node );
                methods.add( field );
            }
        }

        return (MethodDescriptor[])methods.
            toArray( new MethodDescriptor[ methods.size() ] );
    }

    MethodDescriptor buildMethod( final Element element )
        throws Exception
    {
        expectElement( element, MetaClassIOXml.METHOD_ELEMENT );
        final String name =
            expectAttribute( element, MetaClassIOXml.NAME_ATTRIBUTE );
        final String type =
            expectAttribute( element, MetaClassIOXml.TYPE_ATTRIBUTE );
        Attribute[] attributes = Attribute.EMPTY_SET;
        ParameterDescriptor[] parameters = ParameterDescriptor.EMPTY_SET;
        final NodeList nodes = element.getChildNodes();
        final int length = nodes.getLength();
        for( int i = 0; i < length; i++ )
        {
            final Node node = nodes.item( i );
            final short nodeType = node.getNodeType();
            if( nodeType == Node.ELEMENT_NODE )
            {
                final Element child = (Element)node;
                    final String childName = child.getNodeName();
                if( childName.equals( MetaClassIOXml.PARAMETERS_ELEMENT ) )
                {
                    parameters = buildParameters( child );
                }
                else
                {
                    attributes = buildAttributes( child );
                }
            }
        }
        return new MethodDescriptor( name, type, parameters, attributes, attributes );
    }

    ParameterDescriptor[] buildParameters( Element element )
        throws Exception
    {
        expectElement( element, MetaClassIOXml.PARAMETERS_ELEMENT );

        final ArrayList parameters = new ArrayList();
        final NodeList nodes = element.getChildNodes();
        final int length = nodes.getLength();
        for( int i = 0; i < length; i++ )
        {
            final Node node = nodes.item( i );
            final short nodeType = node.getNodeType();
            if( nodeType == Node.ELEMENT_NODE )
            {
                final ParameterDescriptor parameter =
                    buildParameter( (Element)node );
                parameters.add( parameter );
            }
        }

        return (ParameterDescriptor[])parameters.
            toArray( new ParameterDescriptor[ parameters.size() ] );
    }

    ParameterDescriptor buildParameter( final Element element )
        throws Exception
    {
        expectElement( element, MetaClassIOXml.PARAMETER_ELEMENT );
        final String name =
            expectAttribute( element, MetaClassIOXml.NAME_ATTRIBUTE );
        final String type =
            expectAttribute( element, MetaClassIOXml.TYPE_ATTRIBUTE );

        return new ParameterDescriptor( name, type );
    }

    FieldDescriptor[] buildFields( final Element element )
        throws Exception
    {
        expectElement( element, MetaClassIOXml.FIELDS_ELEMENT );

        final ArrayList fields = new ArrayList();
        final NodeList nodes = element.getChildNodes();
        final int length = nodes.getLength();
        for( int i = 0; i < length; i++ )
        {
            final Node node = nodes.item( i );
            final short nodeType = node.getNodeType();
            if( nodeType == Node.ELEMENT_NODE )
            {
                final FieldDescriptor field = buildField( (Element)node );
                fields.add( field );
            }
        }

        return (FieldDescriptor[])fields.
            toArray( new FieldDescriptor[ fields.size() ] );
    }

    FieldDescriptor buildField( final Element element )
        throws Exception
    {
        expectElement( element, MetaClassIOXml.FIELD_ELEMENT );
        final String name =
            expectAttribute( element, MetaClassIOXml.NAME_ATTRIBUTE );
        final String type =
            expectAttribute( element, MetaClassIOXml.TYPE_ATTRIBUTE );
        Attribute[] attributes = Attribute.EMPTY_SET;
        final NodeList nodes = element.getChildNodes();
        final int length = nodes.getLength();
        for( int i = 0; i < length; i++ )
        {
            final Node node = nodes.item( i );
            final short nodeType = node.getNodeType();
            if( nodeType == Node.ELEMENT_NODE )
            {
                attributes = buildAttributes( (Element)node );
            }
        }
        return new FieldDescriptor( name, type, attributes, attributes );
    }

    Attribute[] buildAttributes( final Element element )
        throws Exception
    {
        expectElement( element, MetaClassIOXml.ATTRIBUTES_ELEMENT );

        final ArrayList attributes = new ArrayList();
        final NodeList nodes = element.getChildNodes();
        final int length = nodes.getLength();
        for( int i = 0; i < length; i++ )
        {
            final Node node = nodes.item( i );
            final short nodeType = node.getNodeType();
            if( nodeType == Node.ELEMENT_NODE )
            {
                final Attribute attribute = buildAttribute( (Element)node );
                attributes.add( attribute );
            }
        }

        return (Attribute[])attributes.
            toArray( new Attribute[ attributes.size() ] );
    }

    Attribute buildAttribute( final Element element )
        throws Exception
    {
        expectElement( element, MetaClassIOXml.ATTRIBUTE_ELEMENT );
        final String name =
            expectAttribute( element, MetaClassIOXml.NAME_ATTRIBUTE );

        final StringBuffer sb = new StringBuffer();
        final Properties parameters = new Properties();
        final NodeList nodes = element.getChildNodes();
        final int length = nodes.getLength();
        for( int i = 0; i < length; i++ )
        {
            final Node node = nodes.item( i );
            final short nodeType = node.getNodeType();
            if( nodeType == Node.ELEMENT_NODE )
            {
                buildParam( (Element)node, parameters );
            }
            else if( nodeType == Node.TEXT_NODE ||
                nodeType == Node.CDATA_SECTION_NODE )
            {
                final String value = node.getNodeValue();
                sb.append( value );
            }
        }

        final String value = sb.toString().trim();
        if( 0 != value.length() &&
            0 < parameters.size() )
        {
            final String message =
                "Attribute named " + name +
                "specified both a value (" + value + ") " +
                "and parameters (" + parameters + ").";
            throw new Exception( message );
        }
        if( null == value )
        {
            return new Attribute( name, parameters );
        }
        else
        {
            return new Attribute( name, value );
        }
    }

    void buildParam( final Element element,
                     final Properties parameters )
        throws Exception
    {
        expectElement( element, MetaClassIOXml.PARAM_ELEMENT );
        final String name =
            expectAttribute( element, MetaClassIOXml.NAME_ATTRIBUTE );
        final String value =
            expectAttribute( element, MetaClassIOXml.VALUE_ATTRIBUTE );
        parameters.setProperty( name, value );
    }

    void expectElement( final Element element,
                        final String expected )
        throws Exception
    {
        final String actual = element.getTagName();
        if( !actual.equals( expected ) )
        {
            final String message = "Unexpected element. " +
                "Expected: " + expected + ". Actual: " + actual +
                " @ " + getPathDescription( element ) + ".";
            throw new Exception( message );
        }
    }

    String expectAttribute( final Element element,
                            final String expected )
        throws Exception
    {
        final String actual = element.getAttribute( expected );
        if( null == actual )
        {
            final String message =
                "Element named " + element.getTagName() +
                " missing attribute named " + expected +
                " @ " + getPathDescription( element ) + ".";
            throw new Exception( message );
        }
        return actual;
    }

    String getPathDescription( final Element cause )
    {
        final StringBuffer sb = new StringBuffer();

        Element element = cause;
        while( true )
        {
            sb.insert( 0, element.getNodeName() + "/" );
            final Node parentNode = element.getParentNode();
            if( parentNode instanceof Element )
            {
                element = (Element)parentNode;
            }
            else
            {
                break;
            }
        }

        return sb.toString();
    }
}
