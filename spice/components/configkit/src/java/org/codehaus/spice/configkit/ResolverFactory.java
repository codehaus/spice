/*
 * Copyright (C) The Spice Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */
package org.codehaus.spice.configkit;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParserFactory;
import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

/**
 * This is a utility class for creating an EntityResolver that can resolve all
 * entitys contained within ClassLoader. The entitys are discovered by looking
 * in a catalog file <code>META-INF/spice/catalog.xml</code>. The format of the
 * catalog file is;
 *
 * <pre>
 *   &lt;catalog version="1.0"&gt;
 *     &lt;entity publicId="-//PHOENIX/Assembly DTD Version 1.0//EN"
 *             resource="org/apache/avalon/phoenix/tools/assembly.dtd"/&gt;
 *     &lt;entity systemId="http://jakarta.apache.org/phoenix/assembly_1_0.dtd"
 *             resource="org/apache/avalon/phoenix/tools/assembly.dtd"/&gt;
 *     &lt;entity publicId=""-//PHOENIX/Mx Info DTD Version 1.0//EN""
 *             systemId="http://jakarta.apache.org/phoenix/mxinfo_1_0.dtd"
 *             resource="org/apache/avalon/phoenix/tools/mxinfo.dtd"/&gt;
 *   &lt;/catalog&gt;
 * </pre>
 *
 * <p>Note that at least one of <code>publicId</code> or <code>systemId</code>
 * must be specified and <code>resource</code> must always be specified.
 *
 * @author Peter Donald
 * @version $Revision: 1.1 $ $Date: 2003-12-03 03:19:28 $
 */
public final class ResolverFactory
{
    /** Constant for location where catalog is loaded. */
    private static final String CATALOG_RESOURCE = "META-INF/spice/catalog.xml";

    /**
     * @param classLoader the ClassLoader to scan for catalog files
     * @return an Entity Resolver that will resolver all the entitys defined in
     *         catalog and loadable from ClassLoader
     * @throws SAXException if unable to parse a Catalog file
     * @throws IOException if unable to load a Catalog file
     */
    public static final EntityResolver createResolver(
        final ClassLoader classLoader )
        throws ParserConfigurationException, SAXException, IOException
    {
        if( null == classLoader )
        {
            throw new NullPointerException( "classLoader" );
        }
        final List entitys = new ArrayList();
        final Enumeration resources = classLoader.getResources(
            CATALOG_RESOURCE );
        while( resources.hasMoreElements() )
        {
            final URL url = (URL)resources.nextElement();
            parseCatalog( url, entitys );
        }
        final EntityInfo[] infos = (EntityInfo[])entitys.
            toArray( new EntityInfo[ entitys.size() ] );
        return new ConfigKitEntityResolver( infos, classLoader );
    }

    /**
     * Helper method to parse a catalog specified by url and add all the
     * discovered entitys to the entity list.
     *
     * @param url the url of catalog
     * @param entitys the list of entitys
     * @throws SAXException if unable to parse catalog
     * @throws IOException if unable to read catalog from url
     */
    private static void parseCatalog( final URL url, final List entitys )
        throws ParserConfigurationException, SAXException, IOException
    {
        final InputStream inputStream = url.openStream();
        try
        {
            final XMLReader xmlReader = createXMLReader();
            final CatalogHandler handler = new CatalogHandler( entitys );
            xmlReader.setContentHandler( handler );
            xmlReader.setErrorHandler( handler );
            xmlReader.parse( new InputSource( inputStream ) );
        }
        finally
        {
            try
            {
                inputStream.close();
            }
            catch( final IOException ioe )
            {
                //ignore as probably means it already closed
            }
        }
    }

    /**
     * Create an XMLReader.
     *
     * @return the created XMLReader
     * @throws SAXException if unable to create reader
     */
    private static XMLReader createXMLReader()
        throws SAXException, ParserConfigurationException
    {
        final SAXParserFactory saxParserFactory = SAXParserFactory.newInstance();
        saxParserFactory.setNamespaceAware( false );
        return saxParserFactory.newSAXParser().getXMLReader();
    }
}
