/*
 * Copyright (C) The Spice Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */
package org.codehaus.spice.swingactions;

import org.codehaus.spice.swingactions.Configurator;


/**
 *  Test case for Configurator
 *
 * @author Mauro Talevi
 * @author Peter Donald
 */
public class ConfiguratorTestCase
    extends AbstractTestCase
{
    public ConfiguratorTestCase( final String name )
    {
        super( name );
    }

    public void testInvalidConfiguratorType()
        throws Exception
    {
        try
        {
            Configurator.createActionManager( "blah", "org/codehaus/spice/swingactions/actions.xml" );
            fail( "Expected exception as invalid type specified" );
        }
        catch( final Exception e )
        {
        }
    }

    public void testXMLConfigurator()
        throws Exception
    {
        runManagerTest( Configurator.createActionManager( Configurator.XML, "org/codehaus/spice/swingactions/actions.xml" ) );
        runManagerTest( Configurator.createActionManager( Configurator.XML, getResource( "actions.xml" ) ) );
    }




}
