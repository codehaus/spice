/*
 * Copyright (C) The Spice Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */
package org.codehaus.spice.event;

import org.apache.avalon.framework.logger.ConsoleLogger;

import junit.framework.TestCase;
import org.drools.RuleIntegrationException;
import org.drools.rule.RuleSet;

/**
 * @author <a href="mailto:peter.royal@pobox.com">peter royal</a>
 */
public class DefaultEventManagerTestCase extends TestCase
{
    private DefaultEventManager m_eventManager;

    protected void setUp() throws Exception
    {
        super.setUp();

        m_eventManager = new DefaultEventManager( null, new ConsoleLogger() );
    }

    public void testRegisterUnregister() throws Exception
    {
        final MockSubscriber subscriber = new MockSubscriber( new RuleSet( "test" ) );

        m_eventManager.subscribe( subscriber );
        m_eventManager.unsubscribe( subscriber );
    }

    public void testUnregisterWithoutRegister() throws Exception
    {
        try
        {
            m_eventManager.unsubscribe( new MockSubscriber( null ) );

            fail( "Do not allow unsubscribe without subscribe" );
        }
        catch( NoSuchSubscriberException e )
        {
            assertTrue( "nothing registered", true );
        }
    }

    public void testRegisterNull() throws RuleIntegrationException
    {
        try
        {
            m_eventManager.subscribe( null );

            fail( "Did not throw NPE upon registering null" );
        }
        catch( NullPointerException e )
        {
            assertEquals( "subscriber", e.getMessage() );
        }
    }

    public void testRegisterWithNullRuleset() throws RuleIntegrationException
    {
        try
        {
            m_eventManager.subscribe( new MockSubscriber( null ) );

            fail( "Cannot register null ruleset" );
        }
        catch( EventManagerRuntimeException e )
        {
            assertEquals( "Subscriber had null RuleSet", e.getMessage() );
        }
    }

    private static class MockSubscriber implements Subscriber
    {
        private final RuleSet m_ruleSet;

        public MockSubscriber( final RuleSet ruleSet )
        {
            m_ruleSet = ruleSet;
        }

        public RuleSet getRuleSet()
        {
            return m_ruleSet;
        }
    }
}