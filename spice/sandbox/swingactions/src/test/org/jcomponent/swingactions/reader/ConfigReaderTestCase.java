/*
 * Copyright (C) The Spice Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */
package org.jcomponent.swingactions.reader;


import java.io.InputStream;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.jcomponent.swingactions.metadata.ActionMetaData;

/**
 * Unit tests ConfigReader
 *
 * @author Mauro Talevi
 */
public class ConfigReaderTestCase extends TestCase {



    public ConfigReaderTestCase(String name) {
        super(name);
    }

    public static Test suite() {

        return (new TestSuite(ConfigReaderTestCase.class));

    }

    public void setUp() {

    }

    public void tearDown() {


    }

    public void testConfig() {

        final ConfigReader reader = new ConfigReader();
        try {
            InputStream input = this.getClass().getResourceAsStream
                ("/org/jcomponent/swingactions/reader/swingactions.xml");
            assertNotNull("InputStream from swingactions.xml", input);
            reader.parse(input);
            input.close();
        } catch (Throwable t) {
            t.printStackTrace(System.out);
            fail("Exception in parsing:  " + t);
        }
    
        final ActionMetaData[] actions = reader.getActionMetaData();
        assertNotNull( "Read actions configurations", actions );
        assertEquals( "Found 2 actions", 2, actions.length );
        assertEquals( "Action 1", "[ActionMetaData id=action-1, name=Action 1]", 
                      actions[0].toString() );
        assertEquals( "Action 2", "[ActionMetaData id=action-2, name=Action 2]", 
                      actions[1].toString() );


    }



}
