/*
 * Copyright (C) The Spice Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */
package org.realityforge.metaclass.io;

import java.io.InputStream;
import java.io.DataInputStream;
import java.io.ByteArrayInputStream;
import java.util.Map;
import java.util.HashMap;

/**
 *
 * @author <a href="mailto:peter at realityforge.org">Peter Donald</a>
 * @version $Revision: 1.1 $ $Date: 2003-08-22 03:51:42 $
 */
class MockClassLoader
    extends ClassLoader
{
    private final Map m_resources = new HashMap();

    void bindResource( final String name, final byte[] data )
    {
        m_resources.put( name, data );
    }

    public InputStream getResourceAsStream( String name )
    {
        final byte[] bytes = (byte[])m_resources.get( name );
        if( null == bytes )
        {
            return null;
        }
        return new DataInputStream( new ByteArrayInputStream( bytes ) );
    }
}
