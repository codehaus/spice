/*
 * Copyright (C) The Spice Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */
package org.realityforge.metaclass.io;

import org.realityforge.metaclass.model.Attribute;
import org.realityforge.metaclass.model.ClassDescriptor;
import org.realityforge.metaclass.model.FieldDescriptor;
import org.realityforge.metaclass.model.MethodDescriptor;
import org.realityforge.metaclass.model.ParameterDescriptor;
import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributesImpl;

/**
 * Utility class that serializes a ClassDescriptor object
 * to a SAX2 compliant ContentHandler.
 *
 * @author <a href="mailto:peter at realityforge.org">Peter Donald</a>
 * @version $Revision: 1.5 $ $Date: 2003-10-28 05:20:52 $
 */
public class SAXMetaClassSerializer
{
   /**
    * Constant for CDATA type in attributes.
    */
   static final String CDATA_TYPE = "CDATA";

   /**
    * Constant for empty namespace in attributes.
    */
   static final String EMPTY_NAMESPACE = "";

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
    * Constant for name of method parameters element.
    */
   static final String PARAMETERS_ELEMENT = "parameters";

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
    * Serialize the ClassDescriptor to as a Document to the
    * specified ContentHandler.
    *
    * @param handler the handler
    * @param descriptor the ClassDescriptor
    * @throws SAXException if error during serilization
    */
   public void serialize( final ContentHandler handler,
                          final ClassDescriptor descriptor )
      throws SAXException
   {
      handler.startDocument();
      serializeClass( handler, descriptor );
      handler.endDocument();
   }

   /**
    * Serialize the ClassDescriptor to as an Element.
    *
    * @param handler the handler
    * @param descriptor the ClassDescriptor
    * @throws SAXException if error during serilization
    */
   void serializeClass( final ContentHandler handler,
                        final ClassDescriptor descriptor)
      throws SAXException
   {
      final AttributesImpl atts = new AttributesImpl();
      add( atts, TYPE_ATTRIBUTE, descriptor.getName() );
      add( atts, VERSION_ATTRIBUTE, MetaClassIOXml.VERSION );
      start( handler, CLASS_ELEMENT, atts );
      serializeFields( handler, descriptor.getFields() );
      serializeMethods( handler, descriptor.getMethods() );
      serializeAttributes( handler, descriptor.getAttributes() );
      end( handler, CLASS_ELEMENT );
   }

   /**
    * Serialize Fields.
    *
    * @param handler the handler
    * @param descriptors the descriptors
    * @throws SAXException if error during serilization
    */
   void serializeFields( final ContentHandler handler,
                         final FieldDescriptor[] descriptors )
      throws SAXException
   {
      if ( 0 == descriptors.length )
      {
         return;
      }
      start( handler, FIELDS_ELEMENT, new AttributesImpl() );
      for ( int i = 0; i < descriptors.length; i++ )
      {
         serializeField( handler, descriptors[ i ] );
      }
      end( handler, FIELDS_ELEMENT );
   }

   /**
    * Serialize Field.
    *
    * @param handler the handler
    * @param descriptor the descriptor
    * @throws SAXException if error during serilization
    */
   void serializeField( final ContentHandler handler,
                        final FieldDescriptor descriptor )
      throws SAXException
   {
      final AttributesImpl atts = new AttributesImpl();
      add( atts, NAME_ATTRIBUTE, descriptor.getName() );
      add( atts, TYPE_ATTRIBUTE, descriptor.getType() );
      start( handler, FIELD_ELEMENT, atts );
      serializeAttributes( handler, descriptor.getAttributes() );
      end( handler, FIELD_ELEMENT );
   }

   /**
    * Serialize Method.
    *
    * @param handler the ContentHandler
    * @param descriptors the descriptors
    * @throws SAXException if error during serilization
    */
   void serializeMethods( final ContentHandler handler,
                          final MethodDescriptor[] descriptors )
      throws SAXException
   {
      if ( 0 == descriptors.length )
      {
         return;
      }
      start( handler, METHODS_ELEMENT, new AttributesImpl() );
      for ( int i = 0; i < descriptors.length; i++ )
      {
         serializeMethod( handler, descriptors[ i ] );
      }
      end( handler, METHODS_ELEMENT );
   }

   /**
    * Serialize Method.
    *
    * @param handler the ContentHandler
    * @param descriptor the descriptor
    * @throws SAXException if error during serilization
    */
   void serializeMethod( final ContentHandler handler,
                         final MethodDescriptor descriptor )
      throws SAXException
   {
      final AttributesImpl atts = new AttributesImpl();
      add( atts, NAME_ATTRIBUTE, descriptor.getName() );
      add( atts, TYPE_ATTRIBUTE, descriptor.getReturnType() );
      start( handler, METHOD_ELEMENT, atts );
      serializeParameters( handler, descriptor.getParameters() );
      serializeAttributes( handler, descriptor.getAttributes() );
      end( handler, METHOD_ELEMENT );
   }

   /**
    * Serialize methods parameters.
    *
    * @param handler the handler
    * @param parameters the parameters
    * @throws SAXException if error during serilization
    */
   void serializeParameters( final ContentHandler handler,
                             final ParameterDescriptor[] parameters )
      throws SAXException
   {
      for ( int i = 0; i < parameters.length; i++ )
      {
         serializeParameter( handler, parameters[ i ] );
      }
   }

   /**
    * Serialize method parameter.
    *
    * @param handler the handler
    * @param parameter the parameter
    * @throws SAXException if error during serilization
    */
   void serializeParameter( final ContentHandler handler,
                            final ParameterDescriptor parameter )
      throws SAXException
   {
      final AttributesImpl atts = new AttributesImpl();
      add( atts, NAME_ATTRIBUTE, parameter.getName() );
      add( atts, TYPE_ATTRIBUTE, parameter.getType() );
      start( handler, PARAMETERS_ELEMENT, atts );
      end( handler, PARAMETERS_ELEMENT );
   }

   /**
    * Serialize attributes.
    *
    * @param handler the handler
    * @param attributes the attributes
    * @throws SAXException if error during serilization
    */
   void serializeAttributes( final ContentHandler handler,
                             final Attribute[] attributes )
      throws SAXException
   {
      if ( 0 == attributes.length )
      {
         return;
      }
      start( handler, ATTRIBUTES_ELEMENT, new AttributesImpl() );
      for ( int i = 0; i < attributes.length; i++ )
      {
         serializeAttribute( handler, attributes[ i ] );
      }
      end( handler, ATTRIBUTES_ELEMENT );
   }


   /**
    * Serialize an attribute.
    *
    * @param handler the handler
    * @param attribute the attribute
    * @throws SAXException if error during serilization
    */
   void serializeAttribute( final ContentHandler handler,
                            final Attribute attribute )
      throws SAXException
   {
      final AttributesImpl atts = new AttributesImpl();
      add( atts, NAME_ATTRIBUTE, attribute.getName() );

      start( handler, ATTRIBUTE_ELEMENT, atts );

      text( handler, attribute.getValue() );
      serializeAttributeParams( handler, attribute );

      end( handler, ATTRIBUTE_ELEMENT );
   }

   /**
    * Serialize an attributes parameters.
    *
    * @param handler the handler
    * @param attribute the attribute
    * @throws SAXException if error during serilization
    */
   void serializeAttributeParams( final ContentHandler handler,
                                  final Attribute attribute )
      throws SAXException
   {
      final String[] names = attribute.getParameterNames();
      for ( int i = 0; i < names.length; i++ )
      {
         final String name = names[ i ];
         final String value = attribute.getParameter( name );
         serializeAttributeParam( handler, name, value );
      }
   }

   /**
    * Serialize a single attribute parameter.
    *
    * @param handler the handler
    * @param name the name of parameter
    * @param value the value of parameter
    * @throws SAXException if error during serilization
    */
   void serializeAttributeParam( final ContentHandler handler,
                                 final String name,
                                 final String value )
      throws SAXException
   {
      final AttributesImpl atts = new AttributesImpl();
      add( atts, NAME_ATTRIBUTE, name );
      add( atts, VALUE_ATTRIBUTE, value );
      start( handler, PARAM_ELEMENT, atts );
      end( handler, PARAM_ELEMENT );
   }

   /**
    * Helper metho to add an attribute to a set.
    *
    * @param atts the attributes
    * @param name the attribute name
    * @param value the attribute value
    */
   void add( final AttributesImpl atts,
             final String name,
             final String value )
   {
      atts.addAttribute( EMPTY_NAMESPACE, name, name, CDATA_TYPE, value );
   }

   /**
    * Helper method to output a start element.
    *
    * @param handler the handler
    * @param name the element name
    * @param atts the attributes
    * @throws SAXException if error during serilization
    */
   void start( final ContentHandler handler,
               final String name,
               final AttributesImpl atts )
      throws SAXException
   {
      handler.startElement( EMPTY_NAMESPACE, name, name, atts );
   }

   /**
    * Helper method to output some text.
    *
    * @param handler the handler
    * @param text the text
    * @throws SAXException if error during serilization
    */
   void text( final ContentHandler handler,
              final String text )
      throws SAXException
   {
      if ( null != text )
      {
         final char[] ch = text.toCharArray();
         handler.characters( ch, 0, ch.length );
      }
   }

   /**
    * Helper method to output an end element.
    *
    * @param handler the handler
    * @param name the element name
    * @throws SAXException if error during serilization
    */
   void end( final ContentHandler handler,
             final String name )
      throws SAXException
   {
      handler.endElement( EMPTY_NAMESPACE, name, name );
   }
}
