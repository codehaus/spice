/*
 * Copyright (C) The Spice Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */
package org.realityforge.metaclass.tools.tasks;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import org.apache.tools.ant.AntClassLoader;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.DirectoryScanner;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.Task;
import org.apache.tools.ant.types.FileSet;
import org.apache.tools.ant.types.Path;
import org.realityforge.metaclass.introspector.DefaultMetaClassAccessor;
import org.realityforge.metaclass.io.MetaClassIO;
import org.realityforge.metaclass.io.MetaClassIOBinary;
import org.realityforge.metaclass.io.MetaClassIOXml;
import org.realityforge.metaclass.model.ClassDescriptor;
import org.realityforge.metaclass.tools.compiler.ClassDescriptorCompiler;
import org.realityforge.metaclass.tools.compiler.CompilerMonitor;
import org.realityforge.metaclass.tools.compiler.JavaClassFilter;
import org.realityforge.metaclass.tools.qdox.QDoxAttributeInterceptor;
import org.realityforge.metaclass.tools.qdox.NonNamespaceAttributeRemovingInterceptor;

/**
 * A Task to generate Attributes descriptors from source files.
 *
 * @author <a href="mailto:peter at realityforge.org">Peter Donald</a>
 * @version $Revision: 1.16 $ $Date: 2003-11-20 09:23:19 $
 */
public class GenerateClassDescriptorsTask
    extends Task
    implements CompilerMonitor
{
    /**
     * Constant indicating should write out binary descriptors.
     */
    public static final int BINARY_TYPE = 0;

    /**
     * Constant indicating should write out serialized xml descriptors.
     */
    public static final int XML_TYPE = 1;

    /**
     * Destination directory
     */
    private File m_destDir;

    /**
     * Variable that indicates the output type. See above constants.
     */
    private int m_format = BINARY_TYPE;

    /**
     * Flag indicating whether the compacter
     * should methods with no attributes.
     */
    private boolean m_keepEmptyMethods = false;

    /**
     * Variable that indicates whether non-namespaced tags
     * are filtered out.
     */
    private boolean m_namespaceTagsOnly = true;

    /**
     * The class to output ClassDescriptors in binary format.
     */
    private static final MetaClassIO c_binaryIO = new MetaClassIOBinary();

    /**
     * The class to output ClassDescriptors in xml format.
     */
    private static final MetaClassIO c_xmlIO = new MetaClassIOXml();

    /**
     * Internal list of filter elements added by user.
     */
    private final FilterSet m_filterSet = new FilterSet();

    /**
     * Internal list of interceptor elements added by user.
     */
    private final InterceptorSet m_interceptorSet = new InterceptorSet();

    /**
     * Flag set to true if writing a descriptor fails.
     */
    private boolean m_failed;

    /**
     * Compiler used to compile descriptors.
     */
    private final ClassDescriptorCompiler m_compiler = new ClassDescriptorCompiler();

    /**
     * List of filesets to process.
     */
    private final List m_filesets = new ArrayList();

    /**
     * Setup project for task.
     *
     * @param project the project
     */
    public void setProject( final Project project )
    {
        super.setProject( project );
        m_filterSet.setProject( project );
        m_interceptorSet.setProject( project );
    }

    /**
     * Add a filter definition that will create filter to process metadata.
     *
     * @param element the filter definition
     */
    public void addFilter( final PluginElement element )
    {
        m_filterSet.addFilter( element );
    }

    /**
     * Add a filter definition set.
     *
     * @param set a filter definition set.
     */
    public void addFilterSet( final FilterSet set )
    {
        m_filterSet.addFilterSet( set );
    }

    /**
     * Add an interceptor definition that will create interceptor to process metadata.
     *
     * @param element the interceptor definition
     */
    public void addInterceptor( final PluginElement element )
    {
        m_interceptorSet.addInterceptor( element );
    }

    /**
     * Add an interceptor definition set.
     *
     * @param set the interceptor set
     */
    public void addInterceptorSet( final InterceptorSet set )
    {
        m_interceptorSet.addInterceptorSet( set );
    }

    /**
     * Add fileset to list of files to be processed.
     *
     * @param fileSet fileset to list of files to be processed.
     */
    public void addFileset( final FileSet fileSet )
    {
        m_filesets.add( fileSet );
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
     * Set flag indicating whether Compacter should keep empty methods.
     *
     * @param keepEmptyMethods  the flag
     */
    public void setKeepEmptyMethods( final boolean keepEmptyMethods )
    {
        m_keepEmptyMethods = keepEmptyMethods;
    }

    /**
     * Set the flag whether non-namespaced tags are filtered out.
     *
     * @param namespaceTagsOnly true to filter out non-namespaced tags
     */
    public void setNamespaceTagsOnly( final boolean namespaceTagsOnly )
    {
        m_namespaceTagsOnly = namespaceTagsOnly;
    }

    /**
     * Generate classes and output.
     */
    public void execute()
    {
        setupFilters();
        setupInterceptors();

        if( m_namespaceTagsOnly )
        {
            m_compiler.
                addInterceptor( NonNamespaceAttributeRemovingInterceptor.INTERCEPTOR );
        }

        m_compiler.setDestDir( m_destDir );
        m_compiler.setMonitor( this );
        m_compiler.setKeepEmptyMethods( m_keepEmptyMethods );

        setupTarget();

        setupFileList();

        try
        {
            m_compiler.execute();
        }
        catch( final Exception e )
        {
            throw new BuildException( e.getMessage() );
        }
        if( m_failed )
        {
            final String message = "Error generating ClassDescriptors";
            throw new BuildException( message );
        }
    }

    /**
     * Setup list of files compiler will compile.
     */
    private void setupFileList()
    {
        final int count = m_filesets.size();
        for( int i = 0; i < count; i++ )
        {
            final FileSet fileSet = (FileSet)m_filesets.get( i );
            appendFileSetToCompiler( fileSet );
        }
    }

    /**
     * Add all files contained in fileset to compilers file list.
     *
     * @param fileSet the fileset
     */
    private void appendFileSetToCompiler( final FileSet fileSet )
    {
        final File dir = fileSet.getDir( getProject() );
        final DirectoryScanner directoryScanner =
            fileSet.getDirectoryScanner( getProject() );
        directoryScanner.scan();
        final String[] includedFiles = directoryScanner.getIncludedFiles();
        for( int j = 0; j < includedFiles.length; j++ )
        {
            final File file = new File( dir, includedFiles[ j ] );
            m_compiler.addSourceFile( file );
        }
    }

    /**
     * Setup the output target of compiler.
     */
    void setupTarget()
    {
        if( BINARY_TYPE == m_format )
        {
            m_compiler.setExtension( DefaultMetaClassAccessor.BINARY_EXT );
            m_compiler.setMetaClassIO( c_binaryIO );
        }
        else
        {
            m_compiler.setExtension( DefaultMetaClassAccessor.XML_EXT );
            m_compiler.setMetaClassIO( c_xmlIO );
        }
    }

    /**
     * Creat filters and add them to compiler.
     */
    private void setupFilters()
    {
        final Iterator iterator = m_filterSet.toPlugins().iterator();
        while( iterator.hasNext() )
        {
            final PluginElement element = (PluginElement)iterator.next();
            final JavaClassFilter filter = (JavaClassFilter)
                createInstance( element,
                                JavaClassFilter.class,
                                "filter" );
            m_compiler.addFilter( filter );
        }
    }

    /**
     * Build the interceptors and add them to the compiler.
     */
    private void setupInterceptors()
    {
        final Iterator iterator = m_interceptorSet.toPlugins().iterator();
        while( iterator.hasNext() )
        {
            final PluginElement element = (PluginElement)iterator.next();
            final QDoxAttributeInterceptor interceptor = (QDoxAttributeInterceptor)
                createInstance( element,
                                QDoxAttributeInterceptor.class,
                                "interceptor" );
            m_compiler.addInterceptor( interceptor );
        }
    }

    /**
     * Create an instance of a plugin object.
     *
     * @param element the plugin def
     * @param type the expected type
     * @param description the description of type
     * @return the instance of type
     */
    Object createInstance( final PluginElement element,
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
     * Return a description of output format to print as debug message.
     *
     * @return the output formats descriptive name
     */
    final String getOutputDescription()
    {
        if( XML_TYPE == m_format )
        {
            return "xml";
        }
        else
        {
            return "binary";
        }
    }

    /**
     * Print error message and flag task as having failed.
     *
     * @param descriptor the descriptor
     * @param e the exception
     */
    public void errorWritingDescriptor( final ClassDescriptor descriptor,
                                        final Exception e )
    {
        log( "Error writing descriptor for " +
             descriptor.getName() + " due to " + e,
             Project.MSG_ERR );
        m_failed = true;
    }

    /**
     * Print error message and flag task as having failed.
     *
     * @param file the source file
     */
    public void missingSourceFile( final File file )
    {
        log( "Missing Source file " + file, Project.MSG_ERR );
        m_failed = true;
    }

    /**
     * * @see CompilerMonitor#javaClassObjectsLoaded
     */
    public void javaClassObjectsLoaded( final Collection classes )
    {
        log( "Loaded " + classes.size() + " Java classes.",
             Project.MSG_DEBUG );
    }

    /**
     * * @see CompilerMonitor#postFilterJavaClassList
     */
    public void postFilterJavaClassList( final Collection classes )
    {
        log( "MetaClass Attributes Compiler building " + classes.size() +
             " " + getOutputDescription() + " descriptors.",
             Project.MSG_DEBUG );
    }

    /**
     * @see CompilerMonitor#postBuildDescriptorsList
     */
    public void postBuildDescriptorsList( Collection descriptors )
    {
    }

    /**
     * @see CompilerMonitor#postCompactDescriptorsList
     */
    public void postCompactDescriptorsList( final Collection descriptors )
    {
        log( "MetaClass Attributes Compiler writing " + descriptors.size() +
             " " + getOutputDescription() + " descriptors.",
             Project.MSG_INFO );
    }

    /**
     * @see CompilerMonitor#errorGeneratingDescriptor
     */
    public void errorGeneratingDescriptor( final String classname,
                                           final Throwable t )
    {
        log( "Error Generating decriptor for  " + classname +
             ". Reason: " + t, Project.MSG_ERR );
        m_failed = true;
    }

    /**
     * Return the Compiler used to create descriptors.
     *
     * @return the Compiler used to create descriptors.
     */
    protected final ClassDescriptorCompiler getCompiler()
    {
        return m_compiler;
    }
}
