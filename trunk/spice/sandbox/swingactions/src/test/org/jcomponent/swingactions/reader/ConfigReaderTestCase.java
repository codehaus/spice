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

import org.jcomponent.swingactions.metadata.ActionSetMetaData;

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
    
        final ActionSetMetaData actionSet = reader.getActionSetMetaData();
        assertNotNull( "Read actions configurations", actionSet );
        assertEquals( "Found 2 actions", actionSet.getActions().length, 2 );
        assertEquals( "Action 1", actionSet.getAction( "action-1" ).toString(),
                      "[ActionMetaData id=action-1, name=Action 1, shortDescription=Action 1 Short Desc, longDescription=Action 1 Long Desc, smallIcon=/path/to/icon/action1-small.gif, largeIcon=/path/to/icon/action1-large.gif, actionCommandKey=, acceleratorKey=CTRL-1, mnemonicKey='1']" );
        assertEquals( "Action 2", actionSet.getAction( "action-2" ).toString(),
                      "[ActionMetaData id=action-2, name=Action 2, shortDescription=Action 2 Short Desc, longDescription=Action 2 Long Desc, smallIcon=/path/to/icon/action2-small.gif, largeIcon=/path/to/icon/action2-large.gif, actionCommandKey=, acceleratorKey=CTRL-2, mnemonicKey='2']" );


    }

}
