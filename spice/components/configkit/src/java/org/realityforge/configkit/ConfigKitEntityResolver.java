/*
 * Copyright (C) The Spice Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */
package org.realityforge.configkit;

import java.io.IOException;
import java.io.InputStream;
import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

/**
 * A Class to help to resolve Entitys for items such as DTDs or
 * Schemas.
 *
 * @author <a href="mailto:peter at apache.org">Peter Donald</a>
 * @version $Revision: 1.3 $ $Date: 2003-04-05 09:39:24 $
 */
class ConfigKitEntityResolver
    implements EntityResolver
{
    /**
     * The list of DTDs that can be resolved by this class.
     */
    private final EntityInfo[] m_infos;

    /**
     * The ClassLoader to use when loading resources for DTDs.
     */
    private final ClassLoader m_classLoader;

    /**
     * Construct a resolver using specified DTDInfos where resources are loaded
     * from specified ClassLoader.
     */
    ConfigKitEntityResolver( final EntityInfo[] infos,
                             final ClassLoader classLoader )
    {
        if( null == infos )
        {
            throw new NullPointerException( "infos" );
        }
        m_infos = infos;
        m_classLoader = classLoader;
    }

    /**
     * Resolve an entity in the XML file.
     * Called by parser to resolve DTDs.
     */
    public InputSource resolveEntity( final String publicId, final String systemId )
        throws IOException, SAXException
    {
        for( int i = 0; i < m_infos.length; i++ )
        {
            final EntityInfo info = m_infos[ i ];
            if( ( publicId != null && publicId.equals( info.getPublicId() ) ) ||
                ( systemId != null && systemId.equals( info.getSystemId() ) ) )
            {
                final ClassLoader classLoader = getClassLoader();
                final String resource = info.getResource();
                final InputStream inputStream =
                    classLoader.getResourceAsStream( resource );
                if( null == inputStream )
                {
                    final String message =
                        "Unable to locate resource " + resource +
                        " for entity with publicId=" + publicId +
                        " and systemId=" + systemId + ". " +
                        "Looking in classloader " + classLoader + ".";
                    throw new IOException( message );
                }
                final InputSource inputSource = new InputSource( inputStream );
                inputSource.setPublicId( publicId );
                inputSource.setSystemId( systemId );
                return inputSource;
            }
        }

        return null;
    }

    /**
     * Return CLassLoader to load resource from.
     * If a ClassLoader is specified in the constructor use that,
     * else use ContextClassLoader unless that is null in which case
     * use the current classes ClassLoader.
     */
    private ClassLoader getClassLoader()
    {
        ClassLoader classLoader = m_classLoader;
        if( null == classLoader )
        {
            classLoader = Thread.currentThread().getContextClassLoader();
        }
        if( null == classLoader )
        {
            classLoader = getClass().getClassLoader();
        }
        return classLoader;
    }
}
