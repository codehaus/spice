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

    public void testActions() {

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

    public void testActionGroups() {

        final ConfigReader reader = new ConfigReader();
        try {
            InputStream input = this.getClass().getResourceAsStream
                ("/org/jcomponent/swingactions/reader/swingactions2.xml");
            assertNotNull("InputStream from swingactions2.xml", input);
            reader.parse(input);
            input.close();
        } catch (Throwable t) {
            t.printStackTrace(System.out);
            fail("Exception in parsing:  " + t);
        }
    
        final String[] groupIds = reader.getActionGroupIds();
        assertNotNull( "Read actions configurations", groupIds );
        assertEquals( "Found 2 groups", groupIds.length, 2 );
        final ActionSetMetaData group1 = reader.getActionGroupMetaData( "group-1" );
        assertEquals( "Found 2 group actions ", group1.getActions().length, 2 );
        final ActionSetMetaData group2 = reader.getActionGroupMetaData( "group-2" );
        assertEquals( "Found 3 group actions ", group2.getActions().length, 3 );
        String groupAction11 = "[GroupActionMetaData groupId=group-1, id=action-1, groupOrder=1, name=Name of Action 1, shortDescription=null, longDescription=null, smallIcon=null, largeIcon=null, actionCommandKey=null, acceleratorKey=null, mnemonicKey=null]";
        assertEquals( "GroupAction 11", group1.getAction( "action-1" ).toString(), groupAction11 );
        String groupAction12 = "[GroupActionMetaData groupId=group-1, id=action-2, groupOrder=2, name=null, shortDescription=null, longDescription=null, smallIcon=/path/to/group/icon/action2-small.gif, largeIcon=null, actionCommandKey=null, acceleratorKey=null, mnemonicKey=null]";
        assertEquals( "GroupAction 12", group1.getAction( "action-2" ).toString(), groupAction12 );
        String groupAction23 = "[GroupActionMetaData groupId=group-2, id=action-3, groupOrder=2, name=null, shortDescription=Action 3 Short Desc in group-2, longDescription=null, smallIcon=null, largeIcon=null, actionCommandKey=null, acceleratorKey=null, mnemonicKey=null]";
        assertEquals( "GroupAction 23", group2.getAction( "action-3" ).toString(), groupAction23 );
        String groupAction24 = "[GroupActionMetaData groupId=group-2, id=action-4, groupOrder=1, name=null, shortDescription=null, longDescription=null, smallIcon=null, largeIcon=null, actionCommandKey=null, acceleratorKey=null, mnemonicKey='M']";
        assertEquals( "GroupAction 24", group2.getAction( "action-4" ).toString(), groupAction24 );


    }

}
