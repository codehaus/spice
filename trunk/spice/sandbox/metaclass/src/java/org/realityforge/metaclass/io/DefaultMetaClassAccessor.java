package org.realityforge.metaclass.io;

import java.io.IOException;
import java.io.InputStream;
import org.realityforge.metaclass.MetaClassException;
import org.realityforge.metaclass.model.ClassDescriptor;
import org.realityforge.metaclass.model.PackageDescriptor;

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
 * @version $Revision: 1.5 $ $Date: 2003-08-22 04:30:14 $
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
     * Return a {@link PackageDescriptor} for specified package.
     *
     * @param name the name to get {@link PackageDescriptor} for
     * @param classLoader the classLoader to use
     * @return the newly created {@link PackageDescriptor}
     * @throws MetaClassException if unable to create {@link PackageDescriptor}
     */
    public PackageDescriptor getPackageDescriptor( final String name,
                                                   final ClassLoader classLoader )
        throws MetaClassException
    {
        final String resource;
        if( "".equals( name ) )
        {
            resource = "package" + BINARY_EXT;
        }
        else
        {
            resource = name.replace( '.', '/' ) + "/package" + BINARY_EXT;
        }
        final InputStream inputStream =
            classLoader.getResourceAsStream( resource );
        if( null == inputStream )
        {
            final String message =
                "Missing Attributes for " + name;
            throw new MetaClassException( message );
        }

        try
        {
            return c_metaClassIO.deserializePackage( inputStream );
        }
        catch( final IOException ioe )
        {
            final String message =
                "Unable to load Attributes for " + name;
            throw new MetaClassException( message, ioe );
        }
    }

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
