/*
 * Copyright (C) The Spice Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */
package org.realityforge.metaclass.tools.compiler;

import org.realityforge.metaclass.io.MetaClassIO;
import org.realityforge.metaclass.model.ClassDescriptor;
import java.io.InputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 *
 * @author <a href="mailto:peter at realityforge.org">Peter Donald</a>
 * @version $Revision: 1.1 $ $Date: 2003-10-04 06:58:56 $
 */
class MockIO
    implements MetaClassIO
{
    public ClassDescriptor deserializeClass( InputStream input )
        throws IOException
    {
        return null;
    }

    public void serializeClass( OutputStream output, ClassDescriptor info )
        throws IOException
    {
        throw new IOException();
    }
}
