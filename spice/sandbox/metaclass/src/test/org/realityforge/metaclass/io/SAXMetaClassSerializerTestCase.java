package org.realityforge.metaclass.io;

import java.util.Properties;

import com.mockobjects.constraint.Constraint;
import com.mockobjects.dynamic.C;
import com.mockobjects.dynamic.Mock;
import com.mockobjects.dynamic.ConstraintMatcher;
import com.mockobjects.dynamic.FullConstraintMatcher;
import junit.framework.TestCase;
import org.xml.sax.ContentHandler;
import org.xml.sax.helpers.AttributesImpl;
import org.realityforge.metaclass.model.Attribute;
import org.realityforge.metaclass.model.ParameterDescriptor;
import org.realityforge.metaclass.model.MethodDescriptor;
import org.realityforge.metaclass.model.FieldDescriptor;
import org.realityforge.metaclass.model.ClassDescriptor;

public class SAXMetaClassSerializerTestCase
    extends TestCase
{
    public void testSerializeText()
        throws Exception
    {
        final Mock mockHandler = new Mock( ContentHandler.class );

        final String text = "blah";
        mockHandler.expect( "characters", C.args( C.IS_ANYTHING, C.eq( 0 ), C.eq( text.length() ) ) );

        final ContentHandler handler = (ContentHandler)mockHandler.proxy();
        final SAXMetaClassSerializer serializer = new SAXMetaClassSerializer();
        serializer.text( handler, text );
        mockHandler.verify();
    }

    public void testSerializeAttributeParams()
        throws Exception
    {
        final String name = "X";
        final String value = "Y";

        final Mock mockHandler = new Mock( ContentHandler.class );

        final AttributesImpl attrs = new AttributesImpl();
        add( attrs, SAXMetaClassSerializer.NAME_ATTRIBUTE, name );
        add( attrs, SAXMetaClassSerializer.VALUE_ATTRIBUTE, value );
        start( mockHandler, SAXMetaClassSerializer.PARAM_ELEMENT, attrs );
        end( mockHandler, SAXMetaClassSerializer.PARAM_ELEMENT );

        final ContentHandler handler = (ContentHandler)mockHandler.proxy();

        final SAXMetaClassSerializer serializer = new SAXMetaClassSerializer();

        final Properties parameters = new Properties();
        parameters.put( name, value );
        final Attribute attribute = new Attribute( "ignored", parameters );
        serializer.serializeAttributeParams( handler, attribute );

        mockHandler.verify();
    }

    public void testSerializeAttributes()
        throws Exception
    {
        final Mock mockHandler = new Mock( ContentHandler.class );

        start( mockHandler, SAXMetaClassSerializer.ATTRIBUTES_ELEMENT, new AttributesImpl() );
        final String attributeName = "blah";

        final AttributesImpl attributeAttrs = new AttributesImpl();
        add( attributeAttrs, SAXMetaClassSerializer.NAME_ATTRIBUTE, attributeName );

        start( mockHandler, SAXMetaClassSerializer.ATTRIBUTE_ELEMENT, attributeAttrs );
        end( mockHandler, SAXMetaClassSerializer.ATTRIBUTE_ELEMENT );
        end( mockHandler, SAXMetaClassSerializer.ATTRIBUTES_ELEMENT );

        final ContentHandler handler = (ContentHandler)mockHandler.proxy();

        final SAXMetaClassSerializer serializer = new SAXMetaClassSerializer();

        final Attribute attribute = new Attribute( attributeName );
        serializer.serializeAttributes( handler, new Attribute[]{attribute} );

        mockHandler.verify();
    }

    public void testSerializeMethodParameters()
        throws Exception
    {
        final String name = "X";
        final String value = "Y";

        final Mock mockHandler = new Mock( ContentHandler.class );

        final AttributesImpl attrs = new AttributesImpl();
        add( attrs, SAXMetaClassSerializer.NAME_ATTRIBUTE, name );
        add( attrs, SAXMetaClassSerializer.TYPE_ATTRIBUTE, value );
        start( mockHandler, SAXMetaClassSerializer.PARAMETERS_ELEMENT, attrs );
        end( mockHandler, SAXMetaClassSerializer.PARAMETERS_ELEMENT );

        final ContentHandler handler = (ContentHandler)mockHandler.proxy();

        final SAXMetaClassSerializer serializer = new SAXMetaClassSerializer();

        final ParameterDescriptor[] parameters = new ParameterDescriptor[]
        {
            new ParameterDescriptor( name, value )
        };
        serializer.serializeParameters( handler, parameters );

        mockHandler.verify();
    }

    public void testSerializeMethods()
        throws Exception
    {
        final String name = "X";
        final String type = "Y";

        final Mock mockHandler = new Mock( ContentHandler.class );

        start( mockHandler, SAXMetaClassSerializer.METHODS_ELEMENT, new AttributesImpl() );
        final AttributesImpl attrs = new AttributesImpl();
        add( attrs, SAXMetaClassSerializer.NAME_ATTRIBUTE, name );
        add( attrs, SAXMetaClassSerializer.TYPE_ATTRIBUTE, type );
        start( mockHandler, SAXMetaClassSerializer.METHOD_ELEMENT, attrs );
        end( mockHandler, SAXMetaClassSerializer.METHOD_ELEMENT );
        end( mockHandler, SAXMetaClassSerializer.METHODS_ELEMENT );

        final ContentHandler handler = (ContentHandler)mockHandler.proxy();

        final SAXMetaClassSerializer serializer = new SAXMetaClassSerializer();
        final MethodDescriptor[] methods = new MethodDescriptor[]
        {
            new MethodDescriptor( name,
                                  type,
                                  ParameterDescriptor.EMPTY_SET,
                                  Attribute.EMPTY_SET, 
                                  Attribute.EMPTY_SET )
        };
        serializer.serializeMethods( handler, methods );

        mockHandler.verify();
    }

    public void testSerializeFields()
        throws Exception
    {
        final String name = "X";
        final String type = "Y";

        final Mock mockHandler = new Mock( ContentHandler.class );

        start( mockHandler, SAXMetaClassSerializer.FIELDS_ELEMENT, new AttributesImpl() );
        final AttributesImpl attrs = new AttributesImpl();
        add( attrs, SAXMetaClassSerializer.NAME_ATTRIBUTE, name );
        add( attrs, SAXMetaClassSerializer.TYPE_ATTRIBUTE, type );
        start( mockHandler, SAXMetaClassSerializer.FIELD_ELEMENT, attrs );
        end( mockHandler, SAXMetaClassSerializer.FIELD_ELEMENT );
        end( mockHandler, SAXMetaClassSerializer.FIELDS_ELEMENT );

        final ContentHandler handler = (ContentHandler)mockHandler.proxy();

        final SAXMetaClassSerializer serializer = new SAXMetaClassSerializer();
        final FieldDescriptor[] fields = new FieldDescriptor[]
        {
            new FieldDescriptor( name, type, Attribute.EMPTY_SET, Attribute.EMPTY_SET )
        };
        serializer.serializeFields( handler, fields );

        mockHandler.verify();
    }

    public void testSerializeClass()
        throws Exception
    {
        final String type = "Y";

        final Mock mockHandler = new Mock( ContentHandler.class );

        final AttributesImpl attrs = new AttributesImpl();
        add( attrs, SAXMetaClassSerializer.TYPE_ATTRIBUTE, type );
        add( attrs, SAXMetaClassSerializer.VERSION_ATTRIBUTE, MetaClassIOXml.VERSION );
        start( mockHandler, SAXMetaClassSerializer.CLASS_ELEMENT, attrs );
        end( mockHandler, SAXMetaClassSerializer.CLASS_ELEMENT );

        final ContentHandler handler = (ContentHandler)mockHandler.proxy();

        final SAXMetaClassSerializer serializer = new SAXMetaClassSerializer();
        final ClassDescriptor descriptor =
            new ClassDescriptor( type,
                                 Attribute.EMPTY_SET,
                                 Attribute.EMPTY_SET,
                                 FieldDescriptor.EMPTY_SET,
                                 MethodDescriptor.EMPTY_SET );
        serializer.serializeClass( handler, descriptor );

        mockHandler.verify();
    }

    public void testSerializeClassAsDocument()
        throws Exception
    {
        final String type = "Y";

        final Mock mockHandler = new Mock( ContentHandler.class );

        final AttributesImpl attrs = new AttributesImpl();
        add( attrs, SAXMetaClassSerializer.TYPE_ATTRIBUTE, type );
        add( attrs, SAXMetaClassSerializer.VERSION_ATTRIBUTE, MetaClassIOXml.VERSION );
        mockHandler.expect( "startDocument", C.NO_ARGS );
        start( mockHandler, SAXMetaClassSerializer.CLASS_ELEMENT, attrs );
        end( mockHandler, SAXMetaClassSerializer.CLASS_ELEMENT );
        mockHandler.expect( "endDocument", C.NO_ARGS );

        final ContentHandler handler = (ContentHandler)mockHandler.proxy();

        final SAXMetaClassSerializer serializer = new SAXMetaClassSerializer();
        final ClassDescriptor descriptor =
            new ClassDescriptor( type,
                                 Attribute.EMPTY_SET,
                                 Attribute.EMPTY_SET,
                                 FieldDescriptor.EMPTY_SET,
                                 MethodDescriptor.EMPTY_SET );
        serializer.serialize( handler, descriptor );

        mockHandler.verify();
    }

    private void end( final Mock mockHandler, final String name )
    {
        mockHandler.expect( "endElement", END( name ) );
    }

    private void start( final Mock mockHandler,
                        final String name,
                        final AttributesImpl attrs )
    {
        mockHandler.expect( "startElement", ST( name, attrs ) );
    }

    private static ConstraintMatcher END( final String name )
    {
        final Constraint[] constraints = new Constraint[]
        {
            C.eq( "" ), C.eq( name ), C.eq( name )
        };
        return new FullConstraintMatcher( constraints );
    }

    private static ConstraintMatcher ST( final String name,
                                         final AttributesImpl attr )
    {
        final Constraint[] constraints = new Constraint[]
        {
            C.eq( "" ), C.eq( name ), C.eq( name ), AT( attr )
        };
        return new FullConstraintMatcher( constraints );
    }

    private static Constraint AT( final AttributesImpl attr )
    {
        return new EqAttributesConstraint( attr );
    }

    private void add( final AttributesImpl atts,
                      final String name,
                      final String value )
    {
        atts.addAttribute( "", name, name, "CDATA", value );
    }
}
