/*

 ============================================================================
                   The Apache Software License, Version 1.1
 ============================================================================

 Copyright (C) 1999-2003 The Apache Software Foundation. All rights reserved.

 Redistribution and use in source and binary forms, with or without modifica-
 tion, are permitted provided that the following conditions are met:

 1. Redistributions of  source code must  retain the above copyright  notice,
    this list of conditions and the following disclaimer.

 2. Redistributions in binary form must reproduce the above copyright notice,
    this list of conditions and the following disclaimer in the documentation
    and/or other materials provided with the distribution.

 3. The end-user documentation included with the redistribution, if any, must
    include  the following  acknowledgment:  "This product includes  software
    developed  by the  Apache Software Foundation  (http://www.apache.org/)."
    Alternately, this  acknowledgment may  appear in the software itself,  if
    and wherever such third-party acknowledgments normally appear.

 4. The names "Jakarta", "Avalon", "Excalibur" and "Apache Software Foundation"
    must not be used to endorse or promote products derived from this  software
    without  prior written permission. For written permission, please contact
    apache@apache.org.

 5. Products  derived from this software may not  be called "Apache", nor may
    "Apache" appear  in their name,  without prior written permission  of the
    Apache Software Foundation.

 THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED WARRANTIES,
 INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
 FITNESS  FOR A PARTICULAR  PURPOSE ARE  DISCLAIMED.  IN NO  EVENT SHALL  THE
 APACHE SOFTWARE  FOUNDATION  OR ITS CONTRIBUTORS  BE LIABLE FOR  ANY DIRECT,
 INDIRECT, INCIDENTAL, SPECIAL,  EXEMPLARY, OR CONSEQUENTIAL  DAMAGES (INCLU-
 DING, BUT NOT LIMITED TO, PROCUREMENT  OF SUBSTITUTE GOODS OR SERVICES; LOSS
 OF USE, DATA, OR  PROFITS; OR BUSINESS  INTERRUPTION)  HOWEVER CAUSED AND ON
 ANY  THEORY OF LIABILITY,  WHETHER  IN CONTRACT,  STRICT LIABILITY,  OR TORT
 (INCLUDING  NEGLIGENCE OR  OTHERWISE) ARISING IN  ANY WAY OUT OF THE  USE OF
 THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.

 This software  consists of voluntary contributions made  by many individuals
 on  behalf of the Apache Software  Foundation. For more  information on the
 Apache Software Foundation, please see <http://www.apache.org/>.

*/
package org.realityforge.xsyslog.reader.test;

import java.io.InputStream;
import java.lang.Exception;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import junit.framework.TestCase;
import org.w3c.dom.Document;
import org.realityforge.xsyslog.metadata.SyslogMetaData;
import org.realityforge.xsyslog.metadata.RouteMetaData;
import org.realityforge.xsyslog.metadata.DestinationMetaData;
import org.realityforge.xsyslog.reader.SyslogConfigReader;

/**
 * TestCase for {@link SyslogConfigReader}.
 *
 * @author <a href="mailto:peter at apache.org">Peter Donald</a>
 */
public class SyslogConfigReaderTestCase
    extends TestCase
{
    public SyslogConfigReaderTestCase( final String name )
    {
        super( name );
    }

    public void testNullInRouteMetaData1()
        throws Exception
    {
        try
        {
            new RouteMetaData( null,
                               new String[ 0 ],
                               new String[ 0 ],
                               null );
        }
        catch( final NullPointerException e )
        {
            assertEquals( "null in syslog ctor", "destinations", e.getMessage() );
        }
    }

    public void testNullInRouteMetaData2()
        throws Exception
    {
        try
        {
            new RouteMetaData( new String[ 0 ],
                               null,
                               new String[ 0 ],
                               null );
        }
        catch( final NullPointerException e )
        {
            assertEquals( "null in syslog ctor", "includes", e.getMessage() );
        }
    }

    public void testNullInRouteMetaData3()
        throws Exception
    {
        try
        {
            new RouteMetaData( new String[ 0 ],
                               new String[ 0 ],
                               null,
                               null );
        }
        catch( final NullPointerException e )
        {
            assertEquals( "null in syslog ctor", "excludes", e.getMessage() );
        }
    }

    public void testNullInSyslogMetaData1()
        throws Exception
    {
        try
        {
            new SyslogMetaData( null,
                                new String[ 0 ],
                                new DestinationMetaData[ 0 ],
                                new RouteMetaData[ 0 ] );
        }
        catch( final NullPointerException e )
        {
            assertEquals( "null in syslog ctor", "predefined", e.getMessage() );
        }
    }

    public void testNullInSyslogMetaData2()
        throws Exception
    {
        try
        {
            new SyslogMetaData( new String[ 0 ],
                                null,
                                new DestinationMetaData[ 0 ],
                                new RouteMetaData[ 0 ] );
        }
        catch( final NullPointerException e )
        {
            assertEquals( "null in syslog ctor", "packages", e.getMessage() );
        }
    }

    public void testNullInSyslogMetaData3()
        throws Exception
    {
        try
        {
            new SyslogMetaData( new String[ 0 ],
                                new String[ 0 ],
                                null,
                                new RouteMetaData[ 0 ] );
        }
        catch( final NullPointerException e )
        {
            assertEquals( "null in syslog ctor", "destinations", e.getMessage() );
        }
    }

    public void testNullInSyslogMetaData4()
        throws Exception
    {
        try
        {
            new SyslogMetaData( new String[ 0 ],
                                new String[ 0 ],
                                new DestinationMetaData[ 0 ],
                                null );
        }
        catch( final NullPointerException e )
        {
            assertEquals( "null in syslog ctor", "routes", e.getMessage() );
        }
    }

    public void testConfig1()
        throws Exception
    {
        final SyslogMetaData defs = buildFromResource( "config1.xml" );

        assertEquals( "syslog Predefined Count",
                      2,
                      defs.getPredefined().length );

        assertEquals( "syslog packages Count",
                      1,
                      defs.getPackages().length );

        assertEquals( "syslog packages Count",
                      "org.realityforge.xsyslog.lib",
                      defs.getPackages()[ 0 ] );

        assertEquals( "syslog Predefined 1",
                      "*console*",
                      defs.getPredefined()[ 0 ] );
        assertEquals( "syslog Predefined 2",
                      "*server*",
                      defs.getPredefined()[ 1 ] );

        assertEquals( "syslog Count",
                      2,
                      defs.getDestinations().length );
        assertNotNull( "syslog dest1",
                       defs.getDestination( "dest1" ) );
        assertNotNull( "syslog dest2",
                       defs.getDestination( "dest2" ) );

        assertNull( "syslog no exist destination",
                    defs.getDestination( "no exist" ) );

        assertEquals( "syslog dest1.entrys Name",
                      defs.getDestination( "dest1" ).getName(),
                      "dest1" );
        assertEquals( "syslog dest1.type Count",
                      defs.getDestination( "dest1" ).getType(),
                      "File" );
        assertNotNull( "syslog dest1.config Count",
                       defs.getDestination( "dest1" ).getConfiguration() );

        assertEquals( "syslog dest2 Name",
                      defs.getDestination( "dest2" ).getName(),
                      "dest2" );
        assertEquals( "syslog dest1.type Count",
                      defs.getDestination( "dest2" ).getType(),
                      "RotatingFile" );
        assertNotNull( "syslog dest2.config Count",
                       defs.getDestination( "dest2" ).getConfiguration() );

        final RouteMetaData[] routes = defs.getRoutes();
        assertEquals( "syslog Routes Count", 2, routes.length );

        assertEquals( "routes[ 0 ] destination Count", 2, routes[ 0 ].getDestinations().length );
        assertEquals( "routes[ 0 ]getDestinations()[ 0 ]",
                      "dest1",
                      routes[ 0 ].getDestinations()[ 0 ] );
        assertEquals( "routes[ 0 ]getDestinations()[ 0 ]",
                      "dest2",
                      routes[ 0 ].getDestinations()[ 1 ] );

        assertEquals( "routes[ 0 ] includes Count", 1, routes[ 0 ].getIncludes().length );
        assertEquals( "routes[ 0 ].getIncludes()[ 0 ]",
                      "*.auth",
                      routes[ 0 ].getIncludes()[ 0 ] );

        assertEquals( "routes[ 0 ] getExcludes Count", 1, routes[ 0 ].getExcludes().length );
        assertEquals( "routes[ 0 ].getExcludes()[ 0 ]",
                      "http.*",
                      routes[ 0 ].getExcludes()[ 0 ] );
        assertEquals( "routes[ 0 ] level", null, routes[ 0 ].getLevel() );

        assertEquals( "routes[ 1 ] destination Count", 1, routes[ 1 ].getDestinations().length );
        assertEquals( "routes[ 1 ]getDestinations()[ 0 ]",
                      "dest2",
                      routes[ 1 ].getDestinations()[ 0 ] );

        assertEquals( "routes[ 1 ] includes Count", 1, routes[ 1 ].getIncludes().length );
        assertEquals( "routes[ 1 ].getIncludes()[ 0 ]",
                      "network.encoder",
                      routes[ 1 ].getIncludes()[ 0 ] );

        assertEquals( "routes[ 1 ] getExcludes Count", 0, routes[ 1 ].getExcludes().length );

        assertEquals( "routes[ 1 ] level", "WARN", routes[ 1 ].getLevel() );

    }

    public void testConfig2()
        throws Exception
    {
        try
        {
            buildFromResource( "config2.xml" );
        }
        catch( final Throwable t )
        {
            return;
        }
        fail( "Should have failed as loaded a " +
              "configuration with bad version" );
    }

    public void testConfig3()
        throws Exception
    {
        try
        {
            buildFromResource( "config3.xml" );
        }
        catch( final Throwable t )
        {
            return;
        }
        fail( "Should have failed as loaded a " +
              "configuration with no version set" );
    }

    public void testConfig4()
        throws Exception
    {
        buildFromResource( "config4.xml" );
    }

    protected SyslogMetaData buildFromStream( final InputStream stream )
        throws Exception
    {
        try
        {
            final SyslogConfigReader builder = new SyslogConfigReader();
            final Document config = load( stream );
            return builder.build( config.getDocumentElement() );
        }
        catch( final Exception e )
        {
            fail( "Error building SyslogMetaData: " + e );
            return null;
        }
    }

    protected Document load( final InputStream stream )
        throws Exception
    {
        final DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        final DocumentBuilder builder = factory.newDocumentBuilder();
        return builder.parse( stream );
    }

    protected SyslogMetaData buildFromResource( final String resource )
        throws Exception
    {
        final InputStream stream = getClass().getResourceAsStream( resource );
        if( null == stream )
        {
            fail( "Missing resource " + resource );
        }
        return buildFromStream( stream );
    }
}
