/*
 * Copyright (C) The Spice Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */
package org.realityforge.metaclass.io;

import java.io.InputStream;
import java.io.OutputStream;
import org.realityforge.metaclass.model.ClassDescriptor;

/**
 * This is the interface used to read and write
 * descriptors into Input and Output streams.
 *
 * @author <a href="mailto:doug at doug@stocksoftware.com.au">Doug Hagan</a>
 * @version $Revision: 1.6 $ $Date: 2003-11-01 01:21:04 $
 */
public interface MetaClassIO
{
    /**
     * Read a ClassDescriptor from an input stream.
     *
     * @param input the input stream
     * @return the ClassDescriptor
     * @throws Exception if unable ot read class descriptor
     */
    ClassDescriptor deserializeClass( InputStream input )
        throws Exception;

    /**
     * Write a ClassDescriptor to an output stream.
     *
     * @param output the stream to write class descriptor out to
     * @param info the ClassDescriptor to write out
     * @throws Exception if unable ot write class descriptor
     */
    void serializeClass( OutputStream output, ClassDescriptor info )
        throws Exception;
}
