/*
 * Copyright (C) The Spice Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */
package org.realityforge.metaclass.tools.compiler;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import org.realityforge.metaclass.io.MetaClassIO;
import org.realityforge.metaclass.model.ClassDescriptor;

/**
 * @author Peter Donald
 * @version $Revision: 1.2 $ $Date: 2003-11-28 11:14:54 $
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
