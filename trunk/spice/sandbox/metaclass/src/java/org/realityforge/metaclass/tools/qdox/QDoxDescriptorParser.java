package org.realityforge.metaclass.tools.qdox;

import java.util.Properties;
import java.lang.reflect.Modifier;

import org.realityforge.metaclass.model.ClassDescriptor;
import org.realityforge.metaclass.model.Attribute;
import org.realityforge.metaclass.model.FieldDescriptor;
import org.realityforge.metaclass.model.MethodDescriptor;
import org.realityforge.metaclass.model.ParameterDescriptor;
import com.thoughtworks.qdox.model.JavaClass;
import com.thoughtworks.qdox.model.JavaMethod;
import com.thoughtworks.qdox.model.Type;
import com.thoughtworks.qdox.model.JavaField;
import com.thoughtworks.qdox.model.DocletTag;
import com.thoughtworks.qdox.model.JavaParameter;

public class QDoxDescriptorParser
{
   public ClassDescriptor buildClassDescriptor( final JavaClass javaClass )
   {
      final String classname = javaClass.getFullyQualifiedName();

      // get class modifiers
      final int classModifiers = convertModifiersToInt( javaClass.getModifiers() );

      // get class attributes
      final Attribute[] classAttributes = convertTagsToAttributes( javaClass.getTags() );

      // get fields
      final FieldDescriptor[] fieldDescriptors = getFields( javaClass );

      // get methods
      final MethodDescriptor[] methodDescriptors = getMethods( javaClass );

      return new ClassDescriptor( classname,
                                  classModifiers,
                                  classAttributes,
                                  fieldDescriptors,
                                  methodDescriptors );
   }

   private MethodDescriptor[] getMethods( final JavaClass javaClass )
   {
      final JavaMethod[] methods = javaClass.getMethods();
      final MethodDescriptor[] methodDescriptors = new MethodDescriptor[ methods.length ];
      for ( int i = 0; i < methods.length; i++ )
      {
         final JavaMethod method = methods[ i ];
         if ( null != method )
         {
            final String name = method.getName();
            final Type returns = method.getReturns();
            final String type;
            if ( null != returns )
            {
               type = returns.getValue();
            }
            else
            {
               type = "";
            }

            // get method classModifiers
            final int methodModifiers =
               convertModifiersToInt( method.getModifiers() );

            // get method parameters
            final ParameterDescriptor[] methodParameters =
               parametersToDescriptors( method.getParameters() );

            // get method attributes
            final Attribute[] methodAttributes = convertTagsToAttributes( method.getTags() );
            methodDescriptors[ i ] = new MethodDescriptor( name,
                                                           type,
                                                           methodModifiers,
                                                           methodParameters,
                                                           methodAttributes );
         }
      }
      return methodDescriptors;
   }

   private FieldDescriptor[] getFields( final JavaClass javaClass )
   {
      final JavaField[] fields = javaClass.getFields();
      final FieldDescriptor[] fieldDescriptors = new FieldDescriptor[ fields.length ];
      for ( int i = 0; i < fields.length; i++ )
      {
         final JavaField field = fields[ i ];
         if ( null != field )
         {
            final String name = field.getName();
            final String type = field.getType().getValue();

            // get field classModifiers
            final int fieldModifiers = convertModifiersToInt( field.getModifiers() );

            // get field attributes
            final Attribute[] fieldTags = convertTagsToAttributes( field.getTags() );
            fieldDescriptors[ i ] = new FieldDescriptor( name,
                                                         type,
                                                         fieldModifiers,
                                                         fieldTags );
         }
      }
      return fieldDescriptors;
   }

   private Attribute[] convertTagsToAttributes( final DocletTag[] tags )
   {
      final Attribute[] attributes = new Attribute[ tags.length ];
      for ( int i = 0; i < tags.length; i++ )
      {
         final DocletTag tag = tags[ i ];
         final String name = tag.getName();

         final Properties properties = new Properties();
         boolean validProperties = false;

         final String value = tag.getValue();
         String validParameter = getValidParameter( value );
         if ( null != validParameter )
         {
            final String[] contents = value.split( "=" );
            final String tagName = contents[ 0 ];
            properties.setProperty( tagName, validParameter );
            validProperties = true;
         }

         final String[] parameters = tag.getParameters();
         for ( int j = 0; j < parameters.length && validProperties == true; j++ )
         {
            final String parameter = parameters[ j ];
            validParameter = getValidParameter( parameter );
            if ( null != validParameter )
            {
               final String[] contents = parameter.split( "=" );
               final String tagName = contents[ 0 ];
               properties.setProperty( tagName, validParameter );
               validProperties = true;
            }
         }

         if ( validProperties )
         {
            attributes[ i ] = new Attribute( name, properties );
         }
         else
         {
            attributes[ i ] = new Attribute( name, value );
         }
      }
      return attributes;
   }

   private String getValidParameter( final String string )
   {
      String[] contents = string.split( "=" );
      if ( contents.length == 2 )
      {
         final String value = contents[ 1 ];
         if ( value.length() > 2 &&
            value.startsWith( "\"" ) &&
            value.endsWith( "\"" ) )
         {
            return value.substring( 1, value.length() - 1 );
         }
      }

      return null;
   }

   private int convertModifiersToInt( final String[] modifiers )
   {
      int result = 0;
      for ( int k = 0; k < modifiers.length; k++ )
      {
         final String s = modifiers[ k ].toLowerCase().trim();
         if ( s.equals( "public" ) )
         {
            result |= Modifier.PUBLIC;
         }
         else if ( s.equals( "protected" ) )
         {
            result |= Modifier.PROTECTED;
         }
         else if ( s.equals( "private" ) )
         {
            result |= Modifier.PRIVATE;
         }
         else if ( s.equals( "abstract" ) )
         {
            result |= Modifier.ABSTRACT;
         }
         else if ( s.equals( "static" ) )
         {
            result |= Modifier.STATIC;
         }
         else if ( s.equals( "final" ) )
         {
            result |= Modifier.FINAL;
         }
         else if ( s.equals( "transient" ) )
         {
            result |= Modifier.TRANSIENT;
         }
         else if ( s.equals( "volatile" ) )
         {
            result |= Modifier.VOLATILE;
         }
         else if ( s.equals( "synchronized" ) )
         {
            result |= Modifier.SYNCHRONIZED;
         }
         else if ( s.equals( "native" ) )
         {
            result |= Modifier.NATIVE;
         }
         else if ( s.equals( "strictfp" ) )
         {
            result |= Modifier.STRICT;
         }
         else if ( s.equals( "interface" ) )
         {
            result |= Modifier.INTERFACE;
         }
      }
      return result;
   }

   private ParameterDescriptor[] parametersToDescriptors( final JavaParameter[] parameters )
   {
      final ParameterDescriptor[] parameterDescriptors =
         new ParameterDescriptor[ parameters.length ];
      for ( int i = 0; i < parameters.length; i++ )
      {
         final JavaParameter parameter = parameters[ i ];
         final String name = parameter.getName();
         final String value = parameter.getType().getValue();
         final ParameterDescriptor parameterDescriptor = new ParameterDescriptor( name, value );
         parameterDescriptors[ i ] = parameterDescriptor;
      }
      return parameterDescriptors;
   }
}
