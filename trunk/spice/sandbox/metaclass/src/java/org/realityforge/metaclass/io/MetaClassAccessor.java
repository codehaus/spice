package org.realityforge.metaclass.io;

import org.realityforge.metaclass.MetaClassException;
import org.realityforge.metaclass.model.ClassDescriptor;
import org.realityforge.metaclass.model.PackageDescriptor;

/**
 * This is the interface used to access MetaClass
 * {@link ClassDescriptor}s for a particular class.
 *
 * @author <a href="mailto:peter at realityforge.org">Peter Donald</a>
 * @version $Revision: 1.3 $ $Date: 2003-08-18 07:18:22 $
 */
public interface MetaClassAccessor
{
    /**
     * Return a {@link ClassDescriptor} for specified class.
     *
     * @param classname the classname to get {@link ClassDescriptor} for
     * @param classLoader the classLoader to use
     * @return the newly created {@link ClassDescriptor}
     * @throws MetaClassException if unable to create {@link ClassDescriptor}
     */
    ClassDescriptor getClassDescriptor( String classname,
                                        ClassLoader classLoader )
        throws MetaClassException;

    /**
     * Return a {@link PackageDescriptor} for specified package.
     *
     * @param name the name to get {@link PackageDescriptor} for
     * @param classLoader the classLoader to use
     * @return the newly created {@link PackageDescriptor}
     * @throws MetaClassException if unable to create {@link PackageDescriptor}
     */
    PackageDescriptor getPackageDescriptor( String name,
                                            ClassLoader classLoader )
        throws MetaClassException;
}
