/*
 * Copyright (C) The Spice Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */
package org.realityforge.configkit;

/**
 * Holds information about a given Entity.
 *
 * @author <a href="mailto:peter at realityforge.org">Peter Donald</a>
 * @version $Revision: 1.2 $ $Date: 2003-06-27 03:45:56 $
 */
class EntityInfo
{
    /**
     * The public identifier. Null if unknown.
     */
    private final String m_publicId;

    /**
     * The system identifier.  Null if unknown.
     */
    private final String m_systemId;

    /**
     * The resource name, if a copy of the document is available.
     */
    private final String m_resource;

    EntityInfo( final String publicId,
                final String systemId,
                final String resource )
    {
        //One of systemId and publicId should be non-null
        if( null == publicId && null == systemId )
        {
            throw new NullPointerException( "systemId" );
        }
        if( null == resource )
        {
            throw new NullPointerException( "resource" );
        }
        m_publicId = publicId;
        m_systemId = systemId;
        m_resource = resource;
    }

    /**
     * Returns the public identifier. Null if unknown.
     *
     * @return the public identifier. Null if unknown.
     */
    String getPublicId()
    {
        return m_publicId;
    }

    /**
     * Return the system identifier.  Null if unknown.
     *
     * @return the system identifier.  Null if unknown.
     */
    String getSystemId()
    {
        return m_systemId;
    }

    /**
     * Return the resource name, if a copy of the document is available.
     *
     * @return the resource name, if a copy of the document is available.
     */
    String getResource()
    {
        return m_resource;
    }
}
