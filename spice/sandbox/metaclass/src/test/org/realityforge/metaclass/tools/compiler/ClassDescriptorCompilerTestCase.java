/*
 * Copyright (C) The Spice Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */
package org.realityforge.metaclass.tools.compiler;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Modifier;
import junit.framework.TestCase;
import org.realityforge.metaclass.introspector.DefaultMetaClassAccessor;
import org.realityforge.metaclass.io.MetaClassIOBinary;
import org.realityforge.metaclass.model.Attribute;
import org.realityforge.metaclass.model.ClassDescriptor;
import org.realityforge.metaclass.model.FieldDescriptor;
import org.realityforge.metaclass.model.MethodDescriptor;
import org.realityforge.metaclass.tools.qdox.DefaultQDoxAttributeInterceptor;
import org.realityforge.metaclass.tools.qdox.DeletingAttributeInterceptor;

/**
 *
 * @author <a href="mailto:peter at realityforge.org">Peter Donald</a>
 * @version $Revision: 1.1 $ $Date: 2003-10-04 08:55:36 $
 */
public class ClassDescriptorCompilerTestCase
    extends TestCase
{
    public void testGetOutputFileForClassWithBinary()
        throws Exception
    {
        final ClassDescriptorCompiler task = new ClassDescriptorCompiler();
        task.setExtension( DefaultMetaClassAccessor.BINARY_EXT );
        final File destDir = new File( "." );
        task.setDestDir( destDir );
        final File file = task.getOutputFileForClass( "foo" );
        final File expected = new File( destDir, "foo-meta.binary" ).getCanonicalFile();
        assertEquals( expected, file );
    }

    public void testGetOutputFileForClassWithXML()
        throws Exception
    {
        final ClassDescriptorCompiler task = new ClassDescriptorCompiler();
        task.setExtension( DefaultMetaClassAccessor.XML_EXT );
        final File destDir = new File( "." );
        task.setDestDir( destDir );
        final File file = task.getOutputFileForClass( "foo" );
        final File expected = new File( destDir, "foo-meta.xml" ).getCanonicalFile();
        assertEquals( expected, file );
    }

    public void testFailToWriteClassDescriptors()
        throws Exception
    {
        final ClassDescriptorCompiler task = new ClassDescriptorCompiler();
        final MockMonitor mockMonitor = new MockMonitor();
        task.setMonitor( mockMonitor );
        task.setMetaClassIO( new MockIO() );
        final ClassDescriptor descriptor =
            new ClassDescriptor( "test",
                                 0,
                                 Attribute.EMPTY_SET,
                                 FieldDescriptor.EMPTY_SET,
                                 MethodDescriptor.EMPTY_SET );
        task.writeClassDescriptor( descriptor );
        assertTrue( "error writing descriptor", mockMonitor.isError() );
    }

    public void testNullDestDir()
        throws Exception
    {
        final ClassDescriptorCompiler task = new ClassDescriptorCompiler();
        try
        {
            task.execute();
        }
        catch( final Exception e )
        {
            assertEquals( "DestDir not specified", e.getMessage() );
            return;
        }
        fail( "Expected execute to fail as no destdir specified." );
    }

    public void testFileAsDestDir()
        throws Exception
    {
        final ClassDescriptorCompiler task = new ClassDescriptorCompiler();
        final File baseDirectory = getBaseDirectory();
        final File destDir =
            File.createTempFile( "mgtest", ".tmp", baseDirectory ).getCanonicalFile();
        task.setDestDir( destDir );
        try
        {
            task.execute();
        }
        catch( final Exception e )
        {
            assertEquals( "DestDir (" + destDir + ") is not a directory.", e.getMessage() );
            return;
        }
        fail( "Expected execute to fail as destdir specified by file." );
    }

    public void testDestDirNoExistAndNoCreate()
        throws Exception
    {
        if( -1 != System.getProperty( "os.name" ).indexOf( "Windows" ) )
        {
            //Read-Only directorys still allow java to write
            //to them under windows
            return;
        }
        final ClassDescriptorCompiler task = new ClassDescriptorCompiler();
        final File baseDirectory = getBaseDirectory();
        final File secondBaseDir = new File( baseDirectory, "subDir1" );
        secondBaseDir.mkdirs();
        secondBaseDir.setReadOnly();
        final File destDir = new File( secondBaseDir, "subDir" );
        assertFalse( "destDir.exists()", destDir.exists() );

        task.setDestDir( destDir );
        try
        {
            task.execute();
        }
        catch( final Exception e )
        {
            assertEquals( "DestDir (" + destDir + ") could not be created.", e.getMessage() );
            return;
        }
        fail( "Expected execute to fail as destdir could not be created." );
    }

    public void testNoSourceFiles()
        throws Exception
    {
        final File destDirectory = generateDirectory();

        final ClassDescriptorCompiler task = new ClassDescriptorCompiler();
        task.setDestDir( destDirectory );
        task.execute();

        assertEquals( "generated dirs", 0, destDirectory.listFiles().length );
    }

    public void testSingleSourceFile()
        throws Exception
    {
        final String source =
            "package com.biz;\n" +
            "\n" +
            "/**\n" +
            " * @anAttribute\n" +
            " */\n" +
            "public class MyClass\n" +
            "{\n" +
            "}\n";

        final File sourceDirectory = generateDirectory();
        final File destDirectory = generateDirectory();

        final String sourceFilename =
            sourceDirectory + File.separator + "com" + File.separator + "biz" + File.separator + "MyClass.java";
        final File sourceFile = new File( sourceFilename );
        sourceFile.getParentFile().mkdirs();
        final FileOutputStream output = new FileOutputStream( sourceFile );
        output.write( source.getBytes() );
        output.close();

        final ClassDescriptorCompiler task = new ClassDescriptorCompiler();
        task.setDestDir( destDirectory );
        task.addSourceFile( sourceFile );
        task.execute();
        final String destFilename =
            destDirectory + File.separator + "com" + File.separator + "biz" + File.separator + "MyClass" + DefaultMetaClassAccessor.BINARY_EXT;
        final File destFile = new File( destFilename );

        assertTrue( "destFile.exists()", destFile.exists() );
        final MetaClassIOBinary io = new MetaClassIOBinary();
        final FileInputStream input = new FileInputStream( destFile );
        final ClassDescriptor descriptor = io.deserializeClass( input );
        assertEquals( "descriptor.name", "com.biz.MyClass", descriptor.getName() );
        assertEquals( "descriptor.modifiers", Modifier.PUBLIC, descriptor.getModifiers() );
        assertEquals( "descriptor.attributes.length", 1, descriptor.getAttributes().length );
        assertEquals( "descriptor.attributes[0].name", "anAttribute", descriptor.getAttributes()[ 0 ].getName() );
        assertEquals( "descriptor.methods.length", 0, descriptor.getMethods().length );
        assertEquals( "descriptor.fields.length", 0, descriptor.getFields().length );
    }

    public void testSingleSourceFileInWrongDirectory()
        throws Exception
    {
        final String source =
            "package com.biz;\n" +
            "\n" +
            "/**\n" +
            " * @anAttribute\n" +
            " */\n" +
            "public class MyClass\n" +
            "{\n" +
            "}\n";

        final File sourceDirectory = generateDirectory();
        final File destDirectory = generateDirectory();

        final String sourceFilename =
            sourceDirectory + File.separator + "com" + File.separator + "MyClass.java";
        final File sourceFile = new File( sourceFilename );
        sourceFile.getParentFile().mkdirs();
        final FileOutputStream output = new FileOutputStream( sourceFile );
        output.write( source.getBytes() );
        output.close();

        final ClassDescriptorCompiler task = new ClassDescriptorCompiler();
        task.setDestDir( destDirectory );
        task.addSourceFile( sourceFile );
        task.execute();
        final String destFilename =
            destDirectory + File.separator + "com" + File.separator + "biz" + File.separator + "MyClass" + DefaultMetaClassAccessor.BINARY_EXT;
        final File destFile = new File( destFilename );

        assertTrue( "destFile.exists()", destFile.exists() );
        final MetaClassIOBinary io = new MetaClassIOBinary();
        final FileInputStream input = new FileInputStream( destFile );
        final ClassDescriptor descriptor = io.deserializeClass( input );
        assertEquals( "descriptor.name", "com.biz.MyClass", descriptor.getName() );
        assertEquals( "descriptor.modifiers", Modifier.PUBLIC, descriptor.getModifiers() );
        assertEquals( "descriptor.attributes.length", 1, descriptor.getAttributes().length );
        assertEquals( "descriptor.attributes[0].name", "anAttribute", descriptor.getAttributes()[ 0 ].getName() );
        assertEquals( "descriptor.methods.length", 0, descriptor.getMethods().length );
        assertEquals( "descriptor.fields.length", 0, descriptor.getFields().length );
    }

    public void testSingleSourceFileWithPassThroughInterceptor()
        throws Exception
    {
        final String source =
            "package com.biz;\n" +
            "\n" +
            "/**\n" +
            " * @anAttribute\n" +
            " */\n" +
            "public class MyClass\n" +
            "{\n" +
            "}\n";

        final File sourceDirectory = generateDirectory();
        final File destDirectory = generateDirectory();

        final String sourceFilename =
            sourceDirectory + File.separator + "com" + File.separator + "biz" + File.separator + "MyClass.java";
        final File sourceFile = new File( sourceFilename );
        sourceFile.getParentFile().mkdirs();
        final FileOutputStream output = new FileOutputStream( sourceFile );
        output.write( source.getBytes() );
        output.close();

        final ClassDescriptorCompiler task = new ClassDescriptorCompiler();
        task.setDestDir( destDirectory );
        task.addSourceFile( sourceFile );
        task.addInterceptor( new DefaultQDoxAttributeInterceptor() );
        task.execute();
        final String destFilename =
            destDirectory + File.separator + "com" + File.separator + "biz" + File.separator + "MyClass" + DefaultMetaClassAccessor.BINARY_EXT;
        final File destFile = new File( destFilename );

        assertTrue( "destFile.exists()", destFile.exists() );
        final MetaClassIOBinary io = new MetaClassIOBinary();
        final FileInputStream input = new FileInputStream( destFile );
        final ClassDescriptor descriptor = io.deserializeClass( input );
        assertEquals( "descriptor.name", "com.biz.MyClass", descriptor.getName() );
        assertEquals( "descriptor.modifiers", Modifier.PUBLIC, descriptor.getModifiers() );
        assertEquals( "descriptor.attributes.length", 1, descriptor.getAttributes().length );
        assertEquals( "descriptor.attributes[0].name", "anAttribute", descriptor.getAttributes()[ 0 ].getName() );
        assertEquals( "descriptor.methods.length", 0, descriptor.getMethods().length );
        assertEquals( "descriptor.fields.length", 0, descriptor.getFields().length );
    }

    public void testSingleSourceFileWithDeletingInterceptor()
        throws Exception
    {
        final String source =
            "package com.biz;\n" +
            "\n" +
            "/**\n" +
            " * @anAttribute\n" +
            " * @deleteme\n" +
            " */\n" +
            "public class MyClass\n" +
            "{\n" +
            "}\n";

        final File sourceDirectory = generateDirectory();
        final File destDirectory = generateDirectory();

        final String sourceFilename =
            sourceDirectory + File.separator + "com" + File.separator + "biz" + File.separator + "MyClass.java";
        final File sourceFile = new File( sourceFilename );
        sourceFile.getParentFile().mkdirs();
        final FileOutputStream output = new FileOutputStream( sourceFile );
        output.write( source.getBytes() );
        output.close();

        final ClassDescriptorCompiler task = new ClassDescriptorCompiler();
        task.setDestDir( destDirectory );
        task.addSourceFile( sourceFile );
        task.addInterceptor( new DeletingAttributeInterceptor() );
        task.execute();
        final String destFilename =
            destDirectory + File.separator + "com" + File.separator + "biz" + File.separator + "MyClass" + DefaultMetaClassAccessor.BINARY_EXT;
        final File destFile = new File( destFilename );

        assertTrue( "destFile.exists()", destFile.exists() );
        final MetaClassIOBinary io = new MetaClassIOBinary();
        final FileInputStream input = new FileInputStream( destFile );
        final ClassDescriptor descriptor = io.deserializeClass( input );
        assertEquals( "descriptor.name", "com.biz.MyClass", descriptor.getName() );
        assertEquals( "descriptor.modifiers", Modifier.PUBLIC, descriptor.getModifiers() );
        assertEquals( "descriptor.attributes.length", 1, descriptor.getAttributes().length );
        assertEquals( "descriptor.attributes[0].name", "anAttribute", descriptor.getAttributes()[ 0 ].getName() );
        assertEquals( "descriptor.methods.length", 0, descriptor.getMethods().length );
        assertEquals( "descriptor.fields.length", 0, descriptor.getFields().length );
    }

    public void testSingleSourceFileWithDeletingFilter()
        throws Exception
    {
        final String source =
            "package com.biz;\n" +
            "\n" +
            "/**\n" +
            " * @anAttribute\n" +
            " * @deleteme\n" +
            " */\n" +
            "public class MyClass\n" +
            "{\n" +
            "}\n";

        final File sourceDirectory = generateDirectory();
        final File destDirectory = generateDirectory();

        final String sourceFilename =
            sourceDirectory + File.separator + "com" + File.separator + "biz" + File.separator + "MyClass.java";
        final File sourceFile = new File( sourceFilename );
        sourceFile.getParentFile().mkdirs();
        final FileOutputStream output = new FileOutputStream( sourceFile );
        output.write( source.getBytes() );
        output.close();

        final ClassDescriptorCompiler task = new ClassDescriptorCompiler();
        task.setDestDir( destDirectory );
        task.addSourceFile( sourceFile );
        task.addInterceptor( new DeletingAttributeInterceptor() );
        task.addFilter( new DeletingFilter() );
        task.execute();
        final String destFilename =
            destDirectory + File.separator + "com" + File.separator + "biz" + File.separator + "MyClass" + DefaultMetaClassAccessor.BINARY_EXT;
        final File destFile = new File( destFilename );

        assertTrue( "!destFile.exists()", !destFile.exists() );
    }

    private static final File generateDirectory()
        throws IOException
    {
        final File baseDirectory = getBaseDirectory();
        final File dir =
            File.createTempFile( "mgtest", ".tmp", baseDirectory ).getCanonicalFile();
        dir.delete();
        dir.mkdirs();
        assertTrue( "dir.exists()", dir.exists() );
        return dir;
    }

    private static final File getBaseDirectory()
    {
        final String tempDir = System.getProperty( "java.io.tmpdir" );
        final String baseDir = System.getProperty( "basedir", tempDir );

        final File base = new File( baseDir ).getAbsoluteFile();
        final String pathname =
            base + File.separator + "target" + File.separator + "test-data";
        final File dir = new File( pathname );
        dir.mkdirs();
        return dir;
    }
}
