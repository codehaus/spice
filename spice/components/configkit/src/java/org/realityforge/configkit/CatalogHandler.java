/*
 * Copyright (C) The Spice Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */
package org.realityforge.configkit;

import java.util.List;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 * This is the SAX handler that creates a list of entitys.
 * The handler verifies that the correct version is specified
 * in catalog tag and that the entity tag has correct attributes
 * specified. See {@link ResolverFactory} for a description of
 * xml format that is parsed.
 *
 * @author <a href="mailto:peter at realityforge.org">Peter Donald</a>
 * @version $Revision: 1.1 $ $Date: 2003-03-11 13:28:33 $
 */
class CatalogHandler
    extends DefaultHandler
{
    /**
     * The list of entitys collected by handler.
     */
    private final List m_entitys;

    /**
     * Create handler that adds entitys to specified list.
     *
     * @param entitys the list to add entitys to
     */
    CatalogHandler( final List entitys )
    {
        if( null == entitys )
        {
            throw new NullPointerException( "entitys" );
        }

        m_entitys = entitys;
    }

    /**
     * Process an xml element.
     *
     * @param uri the uri of element (ignored)
     * @param localName the local name of element
     * @param qName the qualified name (with prefix)
     * @param attributes the attributes of element
     * @throws SAXException if unable to parse element
     */
    public void startElement( final String uri,
                              final String localName,
                              final String qName,
                              final Attributes attributes )
        throws SAXException
    {
        if( "catalog".equals( qName ) )
        {
            final String version = attributes.getValue( "version" );
            if( null == version )
            {
                final String message =
                    "'version' attribute must be specified for catalog element.";
                throw new SAXException( message );
            }
            else if( !"1.0".equals( version ) )
            {
                final String message =
                    "'version' attribute must have value of '1.0' for catalog element.";
                throw new SAXException( message );
            }
        }
        else if( "entity".equals( qName ) )
        {
            /*
             *     <entity publicId=""-//PHOENIX/Mx Info DTD Version 1.0//EN""
             *             systemId="http://jakarta.apache.org/phoenix/mxinfo_1_0.dtd"
             *             resource="org/apache/avalon/phoenix/tools/mxinfo.dtd"/>
             */
            final String publicId = attributes.getValue( "publicId" );
            final String systemId = attributes.getValue( "systemId" );
            final String resource = attributes.getValue( "resource" );

            if( null == publicId && null == systemId )
            {
                final String message =
                    "One of publicId or systemId attributes must be specified " +
                    "for entity element.";
                throw new SAXException( message );
            }
            if( null == resource )
            {
                final String message =
                    "resource attribute must be specified for entity element.";
                throw new SAXException( message );
            }

            m_entitys.add( new EntityInfo( publicId, systemId, resource ) );
        }
        else
        {
            final String message = "unknown element " + qName + ".";
            throw new SAXException( message );
        }
    }
}
