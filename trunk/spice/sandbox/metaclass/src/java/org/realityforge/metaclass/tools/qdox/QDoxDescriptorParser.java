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
 * @version $Revision: 1.7 $ $Date: 2003-08-22 04:41:52 $
 */
public class QDoxDescriptorParser
{
    /**
     * The attribute parser.
     */
    private final AttributeParser m_attributeParser = createAttributeParser();

    /**
     * Create the AttributeParser.
     *
     * @return the AttributeParser
     */
    private AttributeParser createAttributeParser()
    {
        try
        {
            return new Jdk14AttributeParser();
        }
        catch( Exception e )
        {
            return new OROAttributeParser();
        }
    }

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
    private MethodDescriptor buildMethod( final JavaMethod method )
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
    private ParameterDescriptor buildParameter( final JavaParameter parameter )
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
    private FieldDescriptor buildField( final JavaField field )
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
    private Attribute buildAttribute( final DocletTag tag )
    {
        final String name = tag.getName();

        final String value = tag.getValue();
        if( null == value || "".equals( value.trim() ) )
        {
            return new Attribute( name );
        }

        final String[] paramSpans =
            m_attributeParser.parseValueIntoParameterSpans( value );
        if( null == paramSpans )
        {
            return new Attribute( name, value );
        }
        else
        {
            final Properties properties = new Properties();
            for( int i = 0; i < paramSpans.length; i++ )
            {
                final String param = paramSpans[ i ];

                //index should never be -1
                final int index = param.indexOf( "=" );
                final String paramKey = param.substring( 0, index );
                final String paramValue =
                    param.substring( index + 1 + 1, param.length() - 1 );
                properties.setProperty( paramKey, paramValue );
            }
            return new Attribute( name, properties );
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
    private int parseModifiers( final String[] qualifiers )
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
}
