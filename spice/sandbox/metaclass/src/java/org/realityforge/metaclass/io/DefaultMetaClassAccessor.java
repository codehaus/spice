package org.realityforge.metaclass.io;

import java.io.IOException;
import java.io.InputStream;
import org.realityforge.metaclass.MetaClassException;
import org.realityforge.metaclass.model.ClassDescriptor;

/**
 * This is the default mechanism for loading ClassDescriptor objects.
 * This class follows the following steps to loacte the descriptor.
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
 * @author <a href="mailto:peter at realityforge.org">Peter Donald</a>
 * @version $Revision: 1.2 $ $Date: 2003-08-15 08:40:01 $
 */
public class DefaultMetaClassAccessor
    implements MetaClassAccessor
{
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
                                               final ClassLoader classLoader )
        throws MetaClassException
    {
        final String resource = classname.replace( '.', '/' ) + ".mad";
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
            return c_metaClassIO.deserialize( inputStream );
        }
        catch( final IOException ioe )
        {
            final String message =
                "Unable to load Attributes for " + classname;
            throw new MetaClassException( message, ioe );
        }
    }
}
