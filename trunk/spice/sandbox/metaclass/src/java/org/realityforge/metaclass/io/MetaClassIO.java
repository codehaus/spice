/*
 * Copyright (C) The Spice Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */
package org.realityforge.metaclass.io;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import org.realityforge.metaclass.model.ClassDescriptor;
import org.realityforge.metaclass.model.PackageDescriptor;

/**
 * This is the interface used to read and write
 * descriptors into Input and Output streams.
 *
 * @author <a href="mailto:doug at doug@stocksoftware.com.au">Doug Hagan</a>
 * @version $Revision: 1.4 $ $Date: 2003-08-18 07:18:22 $
 */
public interface MetaClassIO
{
    /**
     * Read a ClassDescriptor from an input stream.
     *
     * @param input the input stream
     * @return the ClassDescriptor
     * @throws IOException if unable ot read class descriptor
     */
    ClassDescriptor deserializeClass( InputStream input )
        throws IOException;

    /**
     * Write a ClassDescriptor to an output stream.
     *
     * @param output the stream to write class descriptor out to
     * @param info the ClassDescriptor to write out
     * @throws IOException if unable ot write class descriptor
     */
    void serializeClass( OutputStream output, ClassDescriptor info )
        throws IOException;

    /**
     * Read a PackageDescriptor from an input stream.
     *
     * @param input the input stream
     * @return the PackageDescriptor
     * @throws IOException if unable ot read class descriptor
     */
    PackageDescriptor deserializePackage( InputStream input )
        throws IOException;

    /**
     * Write a PackageDescriptor to an output stream.
     *
     * @param output the stream to write class descriptor out to
     * @param info the PackageDescriptor to write out
     * @throws IOException if unable ot write class descriptor
     */
    void serializePackage( OutputStream output, PackageDescriptor info )
        throws IOException;
}
