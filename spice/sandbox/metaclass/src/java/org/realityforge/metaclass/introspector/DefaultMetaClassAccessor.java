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
 * objects. This class follows the following steps to locate
 * the ClassDescriptor.
 *
 * <ul>
 *   <li>Look for a file with extension "-meta.binary" sitting
 *       side-by-side the class file. ie If class is named
 *       com.biz.Foo it has a class file named
 *       <tt>com/biz/Foo.class</tt> and an descriptor file
 *       named <tt>com/biz/Foo-meta.binary</tt>. Assume the
 *       descriptor is in binary format.</li>
 *   <li>Look for a file with extension "-meta.xml" sitting
 *       side-by-side the class file. ie If class is named
 *       com.biz.Foo it has a class file named
 *       <tt>com/biz/Foo.class</tt> and an descriptor file
 *       named <tt>com/biz/Foo-meta.xml</tt>. Assume the
 *       descriptor is in xml format.</li>
 *   <li>If unable to locate descriptor throw a
 *       <tt>MetaClassException</tt>.</li>
 * </ul>
 *
 * @author <a href="mailto:peter at realityforge.org">Peter Donald</a>
 * @version $Revision: 1.5 $ $Date: 2003-10-28 07:01:01 $
 */
public class DefaultMetaClassAccessor
    implements MetaClassAccessor
{
    /**
     * Extension of metadata files that are in binary format.
     */
    public static final String BINARY_EXT = "-meta.binary";

    /**
     * Extension of metadata files that are in xml format.
     */
    public static final String XML_EXT = "-meta.xml";

    /**
     * Class used to read the MetaData.
     */
    private static final MetaClassIO c_metaClassIO = new MetaClassIOBinary();

    /**
     * Return a {@link ClassDescriptor} for specified class.
     * Uses process specified in class Javadoc to create descriptor.
     *
     * @param classname the classname to get {@link ClassDescriptor} for
     * @param classLoader the classLoader to use
     * @return the newly created {@link ClassDescriptor}
     * @throws MetaClassException if unable to create {@link ClassDescriptor}
     */
    public ClassDescriptor getClassDescriptor( final String classname,
                                               final ClassLoader classLoader, MetaClassAccessor accessor )
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
