/*
 * Copyright (C) The Spice Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */
package org.codehaus.spice.alchemist.instrument;

import java.util.logging.Level;
import junit.framework.TestCase;
import org.apache.avalon.framework.logger.Jdk14Logger;
import org.codehaus.dna.Configuration;
import org.codehaus.dna.ConfigurationException;
import org.codehaus.dna.impl.DefaultConfiguration;
import org.codehaus.spice.alchemist.logger.DNALogger;
import org.codehaus.spice.alchemist.logger.MockLogger;

public class DNAInstrumentManagerTestCase extends TestCase {

    public void testConstructorWithNullManager() throws Exception {
        try {
            new DNAInstrumentManager( null );
            fail("Expected NPE");
        } catch ( NullPointerException e ) {
            assertEquals( "NPE message", "manager", e.getMessage() );
        }
    }

    public void testRegisterInstrumentable() throws Exception {
        MockAvalonInstrumentManager avalonManager = new MockAvalonInstrumentManager();
        DNAInstrumentManager dnaManager = new DNAInstrumentManager( avalonManager );
        dnaManager.registerInstrumentable(null, null);
        assertEquals( "registerInstrumentable()", "registerInstrumentable", avalonManager.getMethodCalled() );               
    }

    public void testLifecycleWithAvalonManager() throws Exception {
        MockAvalonInstrumentManager mockManager = new MockAvalonInstrumentManager();
        DNAInstrumentManager dnaManager = new DNAInstrumentManager( mockManager );
        dnaManager.initialize();
        assertEquals( "initialize()", "initialize", mockManager.getMethodCalled() );               
        dnaManager.enableLogging(createDNALogger());
        assertEquals( "enableLogging()", "enableLogging", mockManager.getMethodCalled() );               
        dnaManager.configure(createDNAConfiguration());
        assertEquals( "configure()", "configure", mockManager.getMethodCalled() );               
        dnaManager.dispose();
        assertEquals( "dispose()", "dispose", mockManager.getMethodCalled() );               
    }

    public void testLifecycleWithNonAvalonManager() throws Exception {
        MockDNAInstrumentManager mockManager = new MockDNAInstrumentManager();
        DNAInstrumentManager dnaManager = new DNAInstrumentManager( mockManager );
        dnaManager.initialize();
        assertNull( "initialize()", mockManager.getMethodCalled() );               
        dnaManager.enableLogging(createDNALogger());
        assertNull( "enableLogging()", mockManager.getMethodCalled() );               
        dnaManager.configure(createDNAConfiguration());
        assertNull( "configure()", mockManager.getMethodCalled() );               
        dnaManager.dispose();
        assertNull( "dispose()", mockManager.getMethodCalled() );               
    }
    
    public void testConfigureWithNonConfigurableAvalonManager() throws Exception {
        DNAInstrumentManager dnaManager = new DNAInstrumentManager( new NonConfigurableMockAvalonInstrumentManager() );
        try {
            dnaManager.configure( createDNAConfiguration() );
            fail( "Expected ConfigurationException" );
        } catch ( ConfigurationException e ) {
            assertEquals( "ConfigurationException message", "Not Configurable", e.getMessage() );
        }
    }
    
    private Configuration createDNAConfiguration() {
        return new DefaultConfiguration( "", "", "" );
    }

    private DNALogger createDNALogger()
    {
        return new DNALogger( new Jdk14Logger(new MockLogger( Level.INFO ) ) );
    }    
}