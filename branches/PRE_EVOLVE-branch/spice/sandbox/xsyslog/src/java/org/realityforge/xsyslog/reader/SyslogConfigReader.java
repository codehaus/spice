/*
 * Copyright (C) The Spice Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */
package org.realityforge.xsyslog.reader;

import java.util.ArrayList;
import java.util.StringTokenizer;
import org.realityforge.xsyslog.metadata.DestinationMetaData;
import org.realityforge.xsyslog.metadata.RouteMetaData;
import org.realityforge.xsyslog.metadata.SyslogMetaData;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

/**
 * This class builds a {@link SyslogMetaData} object from
 * specified configuration.
 *
 * @author <a href="mailto:peter at apache.org">Peter Donald</a>
 * @version $Revision: 1.1 $ $Date: 2003-06-27 03:27:51 $
 */
public class SyslogConfigReader
{
    /**
     * Build SyslogMetaData from a DOM tree.
     *
     * @param config the root element
     * @return the meta data
     * @throws Exception if malformed DOM
     */
    public SyslogMetaData build( final Element config )
        throws Exception
    {
        final String version =
            config.getAttribute( "version" );
        if( !"1.0".equals( version ) )
        {
            final String message = "Bad version:" + version;
            throw new Exception( message );
        }

        final NodeList predefinedConfigs =
            config.getElementsByTagName( "predefined" );
        final String[] predefined =
            buildPredefined( predefinedConfigs );

        final String packageList = config.getAttribute( "packages" );
        final String[] packages = splitString( packageList );

        final NodeList destinationConfigs =
            config.getElementsByTagName( "destination" );
        final DestinationMetaData[] destinations = buildDestinations( destinationConfigs );

        final NodeList clConfigs = config.getElementsByTagName( "route" );
        final RouteMetaData[] routes = buildRoutes( clConfigs );

        return new SyslogMetaData( predefined,
                                   packages,
                                   destinations,
                                   routes );
    }

    /**
     * Parse out a set of predefined classloaders from
     * specified nodes.
     *
     * @param configs the nodes to process
     * @return the predefined classloaders
     */
    private String[] buildPredefined( final NodeList configs )
    {
        final ArrayList predefines = new ArrayList();

        final int length = configs.getLength();
        for( int i = 0; i < length; i++ )
        {
            final Element element = (Element)configs.item( i );
            final String predefined = element.getAttribute( "name" );
            predefines.add( predefined );
        }

        return (String[])predefines.toArray( new String[ predefines.size() ] );
    }

    /**
     * Build an array of Destination meta datas from node list.
     *
     * @param configs the nodes to process
     * @return the Destinations
     */
    private DestinationMetaData[] buildDestinations( final NodeList configs )
        throws Exception
    {
        final ArrayList loaders = new ArrayList();
        final int length = configs.getLength();

        for( int i = 0; i < length; i++ )
        {
            final Element item = (Element)configs.item( i );
            final DestinationMetaData destination = buildDestination( item );
            loaders.add( destination );
        }

        return (DestinationMetaData[])loaders.toArray( new DestinationMetaData[ loaders.size() ] );
    }

    /**
     * Build a Destination from element.
     *
     * @param config the nodes to process
     * @return the DestinationMetaData
     */
    private DestinationMetaData buildDestination( final Element config )
        throws Exception
    {
        final String name = config.getAttribute( "name" );
        final String type = config.getAttribute( "type" );
        return new DestinationMetaData( name, type, config );
    }

    /**
     * Build an array of RouteMetaData out of supplied nodes.
     *
     * @param configs the nodes to process
     * @return the RouteMetaData array
     */
    private RouteMetaData[] buildRoutes( final NodeList configs )
    {
        final ArrayList routes = new ArrayList();

        final int length = configs.getLength();
        for( int i = 0; i < length; i++ )
        {
            final Element config = (Element)configs.item( i );
            final RouteMetaData route = buildRoute( config );
            routes.add( route );
        }

        return (RouteMetaData[])routes.toArray( new RouteMetaData[ routes.size() ] );
    }

    /**
     * Build a RouteMetaData out of supplied Element.
     *
     * @param config the config element
     * @return the RouteMetaData
     */
    private RouteMetaData buildRoute( final Element config )
    {
        final String[] destinations = splitString( config.getAttribute( "destinations" ) );
        final NodeList includeConfig = config.getElementsByTagName( "include" );
        final String[] includes = extractPatterns( includeConfig );
        final NodeList excludeConfig = config.getElementsByTagName( "exclude" );
        final String[] excludes = extractPatterns( excludeConfig );
        final NodeList nodeList = config.getElementsByTagName( "filter" );
        final int length = nodeList.getLength();
        String level = null;
        if( 0 != length )
        {
            level = ( (Element)nodeList.item( 0 ) ).getAttribute( "level" );
        }
        return new RouteMetaData( destinations, includes, excludes, level );
    }

    /**
     * Extract include/exclude patterns from list of nodes.
     *
     * @param list the list of nodes
     * @return the list of extracted patterns
     */
    private String[] extractPatterns( final NodeList list )
    {
        final ArrayList results = new ArrayList();
        final int length = list.getLength();
        for( int i = 0; i < length; i++ )
        {
            final Element element = (Element)list.item( i );
            final String pattern = element.getAttribute( "name" );
            results.add( pattern );
        }
        return (String[])results.toArray( new String[ results.size() ] );
    }

    /**
     * Splits the string on every token into an array of strings.
     *
     * @param string the string to split
     * @return the resultant array
     */
    private String[] splitString( final String string )
    {
        if( null == string || 0 == string.length() )
        {
            return new String[ 0 ];
        }
        final StringTokenizer tokenizer = new StringTokenizer( string, "," );
        final String[] result = new String[ tokenizer.countTokens() ];

        for( int i = 0; i < result.length; i++ )
        {
            result[ i ] = tokenizer.nextToken();
        }

        return result;
    }

}
