/*
 * Copyright (C) The Spice Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */
package org.realityforge.metaclass.tools.tasks;

import com.thoughtworks.qdox.ant.AbstractQdoxTask;
import com.thoughtworks.qdox.model.JavaClass;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.apache.tools.ant.AntClassLoader;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.types.Path;
import org.realityforge.metaclass.io.DefaultMetaClassAccessor;
import org.realityforge.metaclass.io.MetaClassIO;
import org.realityforge.metaclass.io.MetaClassIOBinary;
import org.realityforge.metaclass.model.ClassDescriptor;
import org.realityforge.metaclass.tools.qdox.QDoxAttributeInterceptor;
import org.realityforge.metaclass.tools.qdox.QDoxDescriptorParser;

/**
 * A Task to generate Attributes descriptors from source files.
 *
 * @author <a href="mailto:peter at realityforge.org">Peter Donald</a>
 * @author <a href="mailto:doug at doug@stocksoftware.com.au">Doug Hagan</a>
 * @version $Revision: 1.1 $ $Date: 2003-09-07 10:21:54 $
 */
public class GenerateClassDescriptorsTask
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
     * Destination directory
     */
    private File m_destDir;

    /**
     * Variable that indicates the output type. See above constants.
     */
    private int m_format = BINARY_TYPE;

    /**
     * The class to output ClassDescriptors in binary format.
     */
    private static final MetaClassIO c_metaClassIO = new MetaClassIOBinary();

    /**
     * The utility class used to generate MetaClass object.
     */
    private static final QDoxDescriptorParser c_infoBuilder = new QDoxDescriptorParser();

    /**
     * The interceptors used to process source files.
     */
    private QDoxAttributeInterceptor[] m_interceptors;

    /**
     * The filter to filter out JavaClass prior to processing.
     */
    private JavaClassFilter m_filter;

    /**
     * Internal list of filter elements added by user.
     */
    private final List m_filters = new ArrayList();

    /**
     * Internal list of interceptor elements added by user.
     */
    private final List m_elements = new ArrayList();

    /**
     * Add an filter definition that will create filter to process metadata.
     *
     * @param element the filter definition
     */
    public void addFilter( final PluginElement element )
    {
        if( null == element.getName() )
        {
            throw new BuildException( "Filter must have a name" );
        }
        m_filters.add( element );
    }

    /**
     * Add an interceptor definition that will create interceptor to process metadata.
     *
     * @param element the interceptor definition
     */
    public void addInterceptor( final PluginElement element )
    {
        if( null == element.getName() )
        {
            throw new BuildException( "Interceptor must have a name" );
        }
        m_elements.add( element );
    }

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
        validate();

        m_interceptors = buildInterceptors();
        m_filter = buildFilters();

        super.execute();

        processSourceFiles();
    }

    /**
     * Build a MultiCastFilter containgin all added by the user.
     *
     * @return a MultiCastFilter containgin all filters
     */
    private JavaClassFilter buildFilters()
    {
        final ArrayList instances = new ArrayList();
        final Iterator iterator = m_filters.iterator();
        while( iterator.hasNext() )
        {
            final PluginElement element = (PluginElement)iterator.next();
            final Object object =
                createInstance( element,
                                JavaClassFilter.class,
                                "filter" );
            instances.add( object );
        }
        final JavaClassFilter[] filters = (JavaClassFilter[])instances.toArray( new JavaClassFilter[ instances.size() ] );
        return new MulticastJavaClassFilter( filters );
    }

    /**
     * Build an array of interceptors from the InterceptorElement
     * objects added by the user.
     *
     * @return an array of interceptors
     */
    private QDoxAttributeInterceptor[] buildInterceptors()
    {
        final ArrayList instances = new ArrayList();
        final Iterator iterator = m_elements.iterator();
        while( iterator.hasNext() )
        {
            final PluginElement element = (PluginElement)iterator.next();
            final Object object =
                createInstance( element,
                                QDoxAttributeInterceptor.class,
                                "interceptor" );
            instances.add( object );
        }
        return (QDoxAttributeInterceptor[])instances.toArray( new QDoxAttributeInterceptor[ instances.size() ] );
    }

    /**
     * Create an instance of a plugin object.
     *
     * @param element the plugin def
     * @param type the expected type
     * @param description the description of type
     * @return the instance of type
     */
    private Object createInstance( final PluginElement element,
                                   final Class type,
                                   final String description )
    {
        final String name = element.getName();
        final AntClassLoader loader = createLoader( element );

        try
        {
            final Object object = loader.loadClass( name ).newInstance();
            if( !type.isInstance( object ) )
            {
                final String message =
                    "Error creating " + description + " " + name +
                    " as it does not implement " + type.getName() + ".";
                log( message );
                throw new BuildException( message );
            }
            return object;
        }
        catch( final Exception e )
        {
            final String message = "Error creating " + description + " " + name;
            log( message );
            throw new BuildException( message, e );
        }
    }

    /**
     * Create Loader for PLuginElement.
     *
     * @param element the element
     * @return the loader
     */
    private AntClassLoader createLoader( final PluginElement element )
    {
        Path path = element.getPath();
        if( null == path )
        {
            path = new Path( getProject() );
        }

        return new AntClassLoader( getClass().getClassLoader(), getProject(), path, true );
    }

    /**
     * Output the ClassDescriptors that are not filtered out.
     */
    protected void processSourceFiles()
    {
        final List classes = collectClassesToSerialize();
        log( "MetaClass Attributes Compiler compiling " + classes.size() +
             " classes and writing as " + getOutputDescription() + "." );

        final List descriptors = buildClassDescriptors( classes );

        try
        {
            writeClassDescriptors( descriptors );
        }
        catch( final IOException ioe )
        {
            final String message = "IOException " + ioe.getMessage();
            log( message );
            throw new BuildException( message, ioe );
        }
    }

    /**
     * Build class descriptors from input JavaCLass objects.
     */
    private List buildClassDescriptors( final List classes )
    {
        final ArrayList descriptors = new ArrayList();
        final Iterator iterator = classes.iterator();
        while( iterator.hasNext() )
        {
            final JavaClass javaClass = (JavaClass)iterator.next();
            final ClassDescriptor descriptor =
                c_infoBuilder.buildClassDescriptor( javaClass, m_interceptors );
            descriptors.add( descriptor );
        }
        return descriptors;
    }

    /**
     * Output the specified ClassDescriptors.
     *
     * @throws IOException If there is a problem writing output
     */
    private void writeClassDescriptors( final List classes )
        throws IOException
    {
        final Iterator iterator = classes.iterator();
        while( iterator.hasNext() )
        {
            final ClassDescriptor descriptor = (ClassDescriptor)iterator.next();
            writeClassDescriptor( descriptor );
        }
    }

    /**
     * Return the set of classes that will actually be serialized
     * and have not been filtered out.
     */
    private List collectClassesToSerialize()
    {
        final List classes = new ArrayList();
        final int classCount = allClasses.size();
        for( int i = 0; i < classCount; i++ )
        {
            final JavaClass candidate = (JavaClass)allClasses.get( i );
            final JavaClass javaClass = m_filter.filterClass( candidate );
            if( null == javaClass )
            {
                continue;
            }
            classes.add( javaClass );
        }
        return classes;
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
        if( m_destDir.exists() && !m_destDir.isDirectory() )
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
     * Write ClassDescriptor out into a file.
     *
     * @param descriptor the ClassDescriptor object
     * @throws java.io.IOException if unable to write descriptor out
     */
    private void writeClassDescriptor( final ClassDescriptor descriptor )
        throws IOException
    {
        final String fqn = descriptor.getName();
        final File file = getOutputFileForClass( fqn );
        file.getParentFile().mkdirs();
        final OutputStream outputStream = new FileOutputStream( file );
        try
        {
            getMetaClassIO().serializeClass( outputStream, descriptor );
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
            final String message = getOutputDescription() + " Not a supported format at this time.";
            throw new BuildException( message );
        }
    }

    /**
     * Close the specified output stream and swallow any exceptions.
     *
     * @param outputStream the output stream
     */
    private static void shutdownStream( final OutputStream outputStream )
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
            filename += DefaultMetaClassAccessor.BINARY_EXT;
        }
        else
        {
            final String message =
                getOutputDescription() + " Not a supported format at this time.";
            throw new BuildException( message );
        }
        return new File( m_destDir, filename ).getCanonicalFile();
    }

    /**
     * Return a description of output format to print as debug message.
     *
     * @return the output formats descriptive name
     */
    private final String getOutputDescription()
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
}
