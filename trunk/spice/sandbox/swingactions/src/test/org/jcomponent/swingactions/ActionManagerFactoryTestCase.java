/*
 * Copyright (C) The Spice Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */
package org.jcomponent.swingactions;

import java.util.HashMap;

/**
 *  Test case for ActionManagerFactory
 *
 * @author <a href="mailto:mauro.talevi at aquilonia.org">Mauro Talevi</a>
 * @author <a href="mailto:peter at realityforge.org">Peter Donald</a>
 */
public class ActionManagerFactoryTestCase
    extends AbstractTestCase
{
    public ActionManagerFactoryTestCase( final String name )
    {
        super( name );
    }

    // InitialActionManagerFactory tests
    public void testInitialActionManagerFactoryFromConfigurerClassLoader()
        throws Exception
    {
        Thread.currentThread().setContextClassLoader( null );
        final HashMap config = new HashMap();
        runFactoryTest( new InitialActionManagerFactory(),
                        config,
                        "xml" );
    }

    public void testInitialActionManagerFactoryUsingDefaults()
        throws Exception
    {
        final HashMap config = new HashMap();
        config.put( ClassLoader.class.getName(),
                    ClassLoader.getSystemClassLoader().getParent() );
        final InitialActionManagerFactory factory = new InitialActionManagerFactory();
        try
        {
            factory.createActionManager( config );
            fail( "Expected to not be able to create ActionManagerFactory as no type was specified or on ClassPath" );
        }
        catch( final Exception e )
        {
        }
    }

    public void testInitialActionManagerFactoryUsingSpecifiedType()
        throws Exception
    {
        final HashMap config = new HashMap();
        config.put( InitialActionManagerFactory.CONFIGURABLE_FACTORY,
                    XMLActionManagerFactory.class.getName() );
        final InitialActionManagerFactory factory = new InitialActionManagerFactory();
        final ActionManager manager = factory.createActionManager( config );
        runManagerTest( manager );
    }

    public void testInitialActionManagerFactoryWithInvalidType()
        throws Exception
    {
        final HashMap config = new HashMap();
        config.put( InitialActionManagerFactory.CONFIGURABLE_FACTORY, "Blah" );
        final InitialActionManagerFactory factory = new InitialActionManagerFactory();
        try
        {
            factory.createActionManager( config );
            fail( "Expected exception as invalid type specified" );
        }
        catch( final Exception e )
        {
        }
    }

    public void testInitialActionManagerFactoryFromSpecifiedClassLoader()
        throws Exception
    {
        Thread.currentThread().setContextClassLoader( null );
        final HashMap config = new HashMap();
        config.put( ClassLoader.class.getName(),
                    InitialActionManagerFactory.class.getClassLoader() );
        runFactoryTest( new InitialActionManagerFactory(),
                        config,
                        "xml" );
    }

    public void testInitialActionManagerFactoryFromContextClassLoader()
        throws Exception
    {
        Thread.currentThread().setContextClassLoader( InitialActionManagerFactory.class.getClassLoader() );
        final HashMap config = new HashMap();
        runFactoryTest( new InitialActionManagerFactory(),
                        config,
                        "xml" );
    }


    // XMLActionManagerFactory tests
    public void testXMLActionManagerFactoryInvalidInput()
        throws Exception
    {
        runInvalidInputData( new XMLActionManagerFactory() );
    }

    public void testXMLActionManagerFactoryWithStreams()
        throws Exception
    {
        runStreamBasedFactoryTest( "actions.xml",
                                   new XMLActionManagerFactory(),
                                   "xml",
                                   new HashMap() );
    }

}
