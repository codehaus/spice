/*
 * Copyright (C) The Spice Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */
package org.realityforge.loggerstore;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import junit.framework.TestCase;
import org.apache.avalon.framework.logger.Logger;
import org.apache.avalon.framework.logger.Log4JLogger;


/**
 *  Test case for Jdk14LoggerStore
 *
 * @author <a href="mailto:mauro.talevi at aquilonia.org">Mauro Talevi</a>
 */
public class Jdk14LoggerStoreTestCase extends TestCase
{

    
    public Jdk14LoggerStoreTestCase(final String name)
    {
        super( name );
    }

    protected void setUp() throws Exception
    {
    }
   
    public void testConfiguration()
        throws Exception
    {
        LoggerStore store = new Jdk14LoggerStore( 
                                    getClass().getResourceAsStream( "logging.properties" ) );
        assertNotNull( "rootLogger", store.getLogger() );
        runLoggerTest( store.getLogger() );
    }


    private void runLoggerTest( final Logger logger )
    {
        logger.info( "Testing jdk14 Logger" );
    }

}
