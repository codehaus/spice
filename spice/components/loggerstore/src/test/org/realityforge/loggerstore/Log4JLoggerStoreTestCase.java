/*
 * Copyright (C) The Spice Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */
package org.realityforge.loggerstore;

import junit.framework.TestCase;
import org.apache.avalon.framework.logger.Logger;

/**
 *  Test case for Log4JLoggerStore
 *
 * @author <a href="mailto:mauro.talevi at aquilonia.org">Mauro Talevi</a>
 */
public class Log4JLoggerStoreTestCase
    extends TestCase
{
    public Log4JLoggerStoreTestCase( final String name )
    {
        super( name );
    }

    public void testPropertiesConfiguration()
        throws Exception
    {
        LoggerStore store = new Log4JLoggerStore( LoggerStoreFactory.PROPERTIES,
                                                  getClass().getResourceAsStream( "log4j.properties" ) );
        assertNotNull( "rootLogger", store.getLogger() );
        runLoggerTest( store.getLogger() );
    }

    public void testXMLConfiguration()
        throws Exception
    {
        LoggerStore store = new Log4JLoggerStore( LoggerStoreFactory.XML,
                                                  getClass().getResourceAsStream( "log4j.xml" ) );
        assertNotNull( "rootLogger", store.getLogger() );
        runLoggerTest( store.getLogger() );
    }

    private void runLoggerTest( final Logger logger )
    {
        logger.info( "Testing log4j Logger" );
    }
}
