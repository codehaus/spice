/*
 * Copyright (C) The Spice Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */
package org.realityforge.metaclass.tools.compiler;

import org.realityforge.metaclass.model.ClassDescriptor;
import java.io.File;
import java.util.List;

/**
 * The default no-op implementation of compiler monitor.
 *
 * @author <a href="mailto:peter at realityforge.org">Peter Donald</a>
 * @version $Revision: 1.1 $ $Date: 2003-10-04 04:20:03 $
 */
public class DefaultCompilerMonitor
    implements CompilerMonitor
{
    /**
     * Method called when there was an error
     * writing ClassDescriptor object.
     *
     * @param descriptor the ClassDescriptor object
     * @param e the Exception
     */
    public void errorWritingDescriptor( final ClassDescriptor descriptor,
                                        final Exception e )
    {
    }

    /**
     * Method called when a specified
     * source file does not exist.
     *
     * @param file the source file
     */
    public void missingSourceFile( final File file )
    {
    }

    /**
     * Called to notify the monitor about the list of JavaClass
     * objects loaded from the source files.
     *
     * @param classes the list of JavaClass objects
     */
    public void javaClassObjectsLoaded( final List classes )
    {
    }

    /**
     * Called to notify the monitor about the list of JavaClass
     * objects that still remain after filtering.
     *
     * @param classes the list of JavaClass objects
     */
    public void postFilterJavaClassList( final List classes )
    {
    }
}
