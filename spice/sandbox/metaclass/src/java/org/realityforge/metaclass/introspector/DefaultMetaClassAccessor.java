/*
 * Copyright (C) The Spice Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */
package org.realityforge.metaclass.introspector;

import java.io.IOException;
import java.io.InputStream;
import org.realityforge.metaclass.io.MetaClassIO;
import org.realityforge.metaclass.io.MetaClassIOBinary;
import org.realityforge.metaclass.model.ClassDescriptor;

/**
 * This is the default mechanism for loading ClassDescriptor
 * and PackageDescriptor objects. This class follows the following
 * steps to locate the ClassDescriptor.
 *
 * <ul>
 *   <li>Look for a file with extension ".mad" sitting side-by-side
 *       class file. ie If class is named com.biz.Foo it has a class
 *       file named <tt>com/biz/Foo.class</tt> and a Attributes file
 *       named <tt>com/biz/Foo.mad</tt>. If found load descriptor
 *       from said file assuming binary format.</li>
 *   <li>If unable to locate descriptor throw a
 *       <tt>MetaClassException</tt>.</li>
 * </ul>
 *
 * <p>Similar steps are followed to locate the PackageDescriptor
 * except that ".package" is postfixed to the package name prior
 * to following the above steps. ie the package <tt>com.biz</tt>
 * may result in loading the file <tt>com/biz/package.mad</tt> to
 * look for attributes.</p>
 *
 * @author <a href="mailto:peter at realityforge.org">Peter Donald</a>
 * @version $Revision: 1.1 $ $Date: 2003-10-04 00:47:48 $
 */
public class DefaultMetaClassAccessor
    implements MetaClassAccessor
{
    /**
     * Extension of metadata files that are in binary format.
     */
    public static final String BINARY_EXT = "-meta.binary";

    /**
     * Extension of metadata files that are in serialized objects format.
     */
    public static final String SERIALIZED_EXT = "-meta.ser";

    /**
     * Extension of metadata files that are in xml format.
     */
    public static final String XML_EXT = "-meta.xml";

    /**
     * Class used to read the MetaData.
     */
    private static final MetaClassIO c_metaClassIO = new MetaClassIOBinary();

    /**
     * Return a {@link org.realityforge.metaclass.model.ClassDescriptor} for specified class.
     * Uses process specified in class Javadoc to create descriptor.
     *
     * @param classname the classname to get {@link org.realityforge.metaclass.model.ClassDescriptor} for
     * @param classLoader the classLoader to use
     * @return the newly created {@link org.realityforge.metaclass.model.ClassDescriptor}
     * @throws org.realityforge.metaclass.introspector.MetaClassException if unable to create {@link org.realityforge.metaclass.model.ClassDescriptor}
     */
    public ClassDescriptor getClassDescriptor( final String classname,
                                               final ClassLoader classLoader )
        throws MetaClassException
    {
        final String resource = classname.replace( '.', '/' ) + BINARY_EXT;
        final InputStream inputStream =
            classLoader.getResourceAsStream( resource );
        if( null == inputStream )
        {
            final String message =
                "Missing Attributes for " + classname;
            throw new MetaClassException( message );
        }

        try
        {
            return c_metaClassIO.deserializeClass( inputStream );
        }
        catch( final IOException ioe )
        {
            final String message =
                "Unable to load Attributes for " + classname;
            throw new MetaClassException( message, ioe );
        }
    }
}
