/*
 * Copyright (C) The Spice Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */
package org.realityforge.metaclass.introspector;

import java.io.InputStream;
import org.realityforge.metaclass.io.MetaClassIO;
import org.realityforge.metaclass.io.MetaClassIOBinary;
import org.realityforge.metaclass.io.MetaClassIOXml;
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
 * @version $Revision: 1.7 $ $Date: 2003-11-01 01:31:57 $
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
     * Class used to read the MetaData in binary format.
     */
    private static final MetaClassIO c_binaryIO = new MetaClassIOBinary();

    /**
     * Class used to read the MetaData in XML format.
     */
    private static final MetaClassIO c_xmlIO = new MetaClassIOXml();

    /**
     * @see MetaClassAccessor#getClassDescriptor
     */
    public ClassDescriptor getClassDescriptor( final String classname,
                                               final ClassLoader classLoader,
                                               final MetaClassAccessor accessor )
        throws MetaClassException
    {
        boolean isXML = false;
        final String baseName = classname.replace( '.', '/' );
        String resource = baseName + BINARY_EXT;
        InputStream inputStream = classLoader.getResourceAsStream( resource );
        if( null == inputStream )
        {
            resource = baseName + XML_EXT;
            inputStream = classLoader.getResourceAsStream( resource );
            isXML = true;
        }

        if( null == inputStream )
        {
            final String message =
                "Missing Attributes for " + classname;
            throw new MetaClassException( message );
        }

        try
        {
            if( isXML )
            {
                return c_xmlIO.deserializeClass( inputStream );
            }
            else
            {
                return c_binaryIO.deserializeClass( inputStream );
            }
        }
        catch( final Exception e )
        {
            final String message =
                "Unable to load Attributes for " + classname;
            throw new MetaClassException( message, e );
        }
    }
}
