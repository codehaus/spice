/*
 * Copyright (C) The Spice Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */
package org.realityforge.metaclass.tools.compiler;

import java.io.File;
import java.util.Collection;
import org.realityforge.metaclass.model.ClassDescriptor;

/**
 * This is the interface via which interested parties
 * can monitor events that occur during compilation.
 *
 * @author <a href="mailto:peter at realityforge.org">Peter Donald</a>
 * @version $Revision: 1.2 $ $Date: 2003-10-16 06:38:57 $
 */
public interface CompilerMonitor
{
    /**
     * Method called when there was an error
     * writing ClassDescriptor object.
     *
     * @param descriptor the ClassDescriptor object
     * @param e the Exception
     */
    void errorWritingDescriptor( ClassDescriptor descriptor, Exception e );

    /**
     * Method called when a specified
     * source file does not exist.
     *
     * @param file the source file
     */
    void missingSourceFile( File file );

    /**
     * Called to notify the monitor about the list of JavaClass
     * objects loaded from the source files.
     *
     * @param classes the list of JavaClass objects
     */
    void javaClassObjectsLoaded( Collection classes );

    /**
     * Called to notify the monitor about the list of JavaClass
     * objects that still remain after filtering.
     *
     * @param classes the list of JavaClass objects
     */
    void postFilterJavaClassList( Collection classes );
}
