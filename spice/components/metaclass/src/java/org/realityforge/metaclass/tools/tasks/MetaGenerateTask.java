/*
 * Copyright (C) The Spice Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */
package org.realityforge.metaclass.tools.tasks;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Modifier;
import java.util.Properties;

import com.thoughtworks.qdox.ant.AbstractQdoxTask;
import com.thoughtworks.qdox.model.DocletTag;
import com.thoughtworks.qdox.model.JavaClass;
import com.thoughtworks.qdox.model.JavaField;
import com.thoughtworks.qdox.model.JavaMethod;
import com.thoughtworks.qdox.model.JavaParameter;
import org.apache.tools.ant.BuildException;
import org.realityforge.metaclass.io.MetaClassIO;
import org.realityforge.metaclass.io.MetaClassIOBinary;
import org.realityforge.metaclass.model.Attribute;
import org.realityforge.metaclass.model.ClassDescriptor;
import org.realityforge.metaclass.model.FieldDescriptor;
import org.realityforge.metaclass.model.MethodDescriptor;
import org.realityforge.metaclass.model.ParameterDescriptor;

/**
 * A Task to generate MetaClass descriptors from source files. 
 *
 * @author <a href="mailto:peter at realityforge.org">Peter Donald</a>
 * @author <a href="mailto:doug at doug@stocksoftware.com.au">Doug Hagan</a>
 * @version $Revision: 1.1 $ $Date: 2003-01-29 00:14:36 $
 */
public class MetaGenerateTask
    extends AbstractQdoxTask
{
    /**
     * Constant indicating should write out binary descriptors.
     */
    public static final int BINARY_TYPE = 0;

    /**
     * Constant indicating should write out serialized object descriptors.
     */
    public static final int SER_TYPE = 1;

    /**
     * Constant indicating should write out serialized xml descriptors.
     */
    public static final int XML_TYPE = 2;

    /**
     * The class to output ClassDescriptors in binary format.
     */
    private static final MetaClassIO c_metaClassIO = new MetaClassIOBinary();

    /**
     * Destination directory
     */
    private File m_destDir;

    /**
     * Variable that indicates the output type. See above constants.
     */
    private int m_format;

    /**
     * Set the destination directory for generated files.
     *
     * @param destDir the destination directory for generated files.
     */
    public void setDestDir( final File destDir )
    {
        m_destDir = destDir;
    }

    /**
     * Specify the output format. Must be one of xml or serialized.
     *
     * @param format the output format
     */
    public void setFormat( final FormatEnum format )
    {
        m_format = format.getTypeCode();
    }

    /**
     * Generate classes and output.
     */
    public void execute()
    {
        final String message =
            "Writing MetaClass descriptors as " + getOutputDescription() + ".";
        log( message );

        validate();

        super.execute();
        try
        {
            writeClassDescriptors();
            System.out.println( "MetaData generation done." );
        }
        catch( final IOException ioe )
        {
            ioe.printStackTrace();
            throw new BuildException( "IOException " + ioe.getMessage(), ioe );
        }
    }

    /**
     * Validate that the parameters are valid.
     */
    private void validate()
    {
        if( null == m_destDir )
        {
            final String message = "DestDir not specified";
            throw new BuildException( message );
        }
        if( !m_destDir.isDirectory() )
        {
            final String message =
                "DestDir (" + m_destDir + ") is not a directory.";
            throw new BuildException( message );
        }

        if( !m_destDir.exists() && !m_destDir.mkdirs() )
        {
            final String message =
                "DestDir (" + m_destDir + ") could not be created.";
            throw new BuildException( message );
        }
    }

    /**
     * Return a description of output format to print as debug message.
     *
     * @return the output formats descriptive name
     */
    private String getOutputDescription()
    {
        if( XML_TYPE == m_format )
        {
            return "xml";
        }
        else if( SER_TYPE == m_format )
        {
            return "serialized objects";
        }
        else
        {
            return "binary";
        }
    }

    /**
     * Return the correct info writer depending on
     * what format the info will be output as
     *
     * @return the MetaClassIO to output ClassDescriptor with
     */
    private MetaClassIO getMetaClassIO()
    {
        if( BINARY_TYPE == m_format )
        {
            return c_metaClassIO;
        }
        else
        {
            final String message =
                getDescription() + "Not a supported format at this time.";
            throw new BuildException( message );
        }
    }

    /**
     * Determine the file for specified class.
     *
     * @param classname the fully qualified name of file to generate
     * @return the file for info
     * @throws IOException if unable to determine base file
     */
    private File getOutputFileForClass( final String classname )
        throws IOException
    {
        String filename =
            classname.replace( '.', File.separatorChar );
        if( BINARY_TYPE == m_format )
        {
            filename += ".mad";
        }
        else
        {
            final String message =
                getDescription() + "Not a supported format at this time.";
            throw new BuildException( message );
        }
        return new File( m_destDir, filename ).getCanonicalFile();
    }

    /**
     * Write ClassDescriptor out into a file.
     *
     * @param descriptor the ClassDescriptor object
     * @throws IOException if unable to write descriptor out
     */
    private void writeClassDescriptor( final ClassDescriptor descriptor )
        throws IOException
    {
        final String fqn = descriptor.getName();
        final File file = getOutputFileForClass( fqn );
        final OutputStream outputStream = new FileOutputStream( file );
        try
        {
            getMetaClassIO().serialize( outputStream, descriptor );
        }
        catch( final Exception e )
        {
            log( "Error writing " + file + ". Cause: " + e );
        }
        finally
        {
            shutdownStream( outputStream );
        }
    }

    /**
     * Close the specified output stream and swallow any exceptions.
     *
     * @param outputStream the output stream
     */
    private void shutdownStream( final OutputStream outputStream )
    {
        if( null != outputStream )
        {
            try
            {
                outputStream.close();
            }
            catch( IOException e )
            {
            }
        }
    }

    /**
     * Output the ClassDescriptors for all the classes.
     *
     * @throws IOException If there is a problem writing output
     */
    protected void writeClassDescriptors()
        throws IOException
    {
        final int classCount = allClasses.size();
        for( int i = 0; i < classCount; i++ )
        {
            final JavaClass candidate = (JavaClass) allClasses.get( i );
            final JavaClass javaClass = filterClass( candidate );
            if( null == javaClass )
            {
                continue;
            }

            final ClassDescriptor classDescriptor = extractClassDescriptor( javaClass );
            writeClassDescriptor( classDescriptor );
        }
    }

    private ClassDescriptor extractClassDescriptor( final JavaClass javaClass )
    {
        final String classname = javaClass.getFullyQualifiedName();

        // get class modifiers
        final int classModifiers =
            convertModifiersToInt( javaClass.getModifiers() );

        // get class attributes
        final Attribute[] classAttributes =
            convertTagsToAttributes( javaClass.getTags() );

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
        for( int i = 0; i < methods.length; i++ )
        {
            final JavaMethod method = methods[ i ];
            if( null != method )
            {
                final String name = method.getName();
                final String type = method.getReturns().getValue();

                // get method classModifiers
                final int methodModifiers =
                    convertModifiersToInt( method.getModifiers() );


                // get method parameters
                final ParameterDescriptor[] methodParameters =
                    parametersToDescriptors( method.getParameters() );

                // get method attributes
                final Attribute[] methodAttributes =
                    convertTagsToAttributes( method.getTags() );
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
        for( int i = 0; i < fields.length; i++ )
        {
            final JavaField field = fields[ i ];
            if( null != field )
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

    /**
     * This method provides an access point for subclasses to use custom filters
     * on the list of classes parsed, i.e. to return null if the class has been filtered.
     * @param javaClass
     * @return javaClass or null
     */
    public JavaClass filterClass( final JavaClass javaClass )
    {
        // do nothing
        return javaClass;
    }

    private Attribute[] convertTagsToAttributes( final DocletTag[] tags )
    {
        final Attribute[] attributes = new Attribute[ tags.length ];
        for( int i = 0; i < tags.length; i++ )
        {
            final DocletTag tag = tags[ i ];
            final String name = tag.getName();
            final String value = tag.getValue();
            final String[] parameters = tag.getParameters();

            final Properties properties = new Properties();
            for( int j = 0; j < parameters.length; j++ )
            {
                final String parameter = parameters[ j ];
                final String[] contents = parameter.split( "=" );
                if( contents.length == 2 )
                {
                    properties.setProperty( contents[ 0 ], contents[ 1 ] );
                }
            }
            attributes[ i ] = new Attribute( name, value, properties );
        }
        return attributes;
    }

    private int convertModifiersToInt( final String[] modifiers )
    {
        int result = 0;
        for( int k = 0; k < modifiers.length; k++ )
        {
            final String s = modifiers[ k ].toLowerCase().trim();
            if( s.equals( "public" ) )
            {
                result |= Modifier.PUBLIC;
            }
            else if( s.equals( "protected" ) )
            {
                result |= Modifier.PROTECTED;
            }
            else if( s.equals( "private" ) )
            {
                result |= Modifier.PRIVATE;
            }
            else if( s.equals( "abstract" ) )
            {
                result |= Modifier.ABSTRACT;
            }
            else if( s.equals( "static" ) )
            {
                result |= Modifier.STATIC;
            }
            else if( s.equals( "final" ) )
            {
                result |= Modifier.FINAL;
            }
            else if( s.equals( "transient" ) )
            {
                result |= Modifier.TRANSIENT;
            }
            else if( s.equals( "volatile" ) )
            {
                result |= Modifier.VOLATILE;
            }
            else if( s.equals( "synchronized" ) )
            {
                result |= Modifier.SYNCHRONIZED;
            }
            else if( s.equals( "native" ) )
            {
                result |= Modifier.NATIVE;
            }
            else if( s.equals( "strictfp" ) )
            {
                result |= Modifier.STRICT;
            }
            else if( s.equals( "interface" ) )
            {
                result |= Modifier.INTERFACE;
            }
        }
        return result;
    }

    public static ParameterDescriptor[] parametersToDescriptors( final JavaParameter[] parameters )
    {
        final ParameterDescriptor[] parameterDescriptors =
            new ParameterDescriptor[ parameters.length ];
        for( int i = 0; i < parameters.length; i++ )
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
