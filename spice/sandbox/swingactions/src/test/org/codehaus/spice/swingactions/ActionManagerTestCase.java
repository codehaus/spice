/*
 * Copyright (C) The Spice Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */
package org.codehaus.spice.swingactions;

import org.codehaus.spice.swingactions.ActionManager;
import org.codehaus.spice.swingactions.XMLActionManager;


/**
 *  Test case for ActionManager
 *
 * @author Mauro Talevi
 */
public class ActionManagerTestCase
    extends AbstractTestCase
{

    public ActionManagerTestCase( final String name )
    {
        super( name );
    }



    public void testXMLConfiguration()
        throws Exception
    {
        final ActionManager manager =
            new XMLActionManager( getResource( "actions.xml" ) );
        //runActionTest( "xml", manager );
    }

    public void testNullConfiguration()
        throws Exception
    {
        try
        {
            final ActionManager manager =
                new XMLActionManager( null );
            fail( "Expected to get an exception as resource is null." );
        }
        catch( final Exception npe )
        {
            assertEquals( "NPE message",
                          "resource",
                          npe.getMessage() );
        }
    }




}
