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
import org.apache.tools.ant.BuildException;
import org.realityforge.metaclass.io.DefaultMetaClassAccessor;
import org.realityforge.metaclass.io.MetaClassIO;
import org.realityforge.metaclass.io.MetaClassIOBinary;
import org.realityforge.metaclass.model.ClassDescriptor;
import org.realityforge.metaclass.tools.qdox.QDoxDescriptorParser;

/**
 * A Task to generate Attributes descriptors from source files.
 *
 * @author <a href="mailto:peter at realityforge.org">Peter Donald</a>
 * @author <a href="mailto:doug at doug@stocksoftware.com.au">Doug Hagan</a>
 * @version $Revision: 1.11 $ $Date: 2003-08-24 02:02:56 $
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
            "Writing Attributes descriptors as " +
            getOutputDescription() + ".";
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
            final JavaClass candidate = (JavaClass)allClasses.get( i );
            final JavaClass javaClass = filterClass( candidate );
            if( null == javaClass )
            {
                continue;
            }

            writeClassDescriptor( c_infoBuilder.buildClassDescriptor( javaClass ) );
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
