/*
 * Copyright (C) The Spice Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */
package org.spice.message;

import javax.transaction.TransactionManager;

import junit.framework.TestCase;
import org.d_haven.event.command.DefaultThreadManager;
import org.d_haven.event.command.ThreadManager;
import org.jmock.Mock;

/**
 * @author <a href="mailto:peter.royal@pobox.com">peter royal</a>
 */
public class TransactionalMessageRouterTestCase extends TestCase
{
    private TransactionalMessageRouter m_transactionalMessageRouter;

    protected void setUp() throws Exception
    {
        final ThreadManager threadManager = new DefaultThreadManager();
        final Mock transactionManager = new Mock( TransactionManager.class );

        m_transactionalMessageRouter
            = new TransactionalMessageRouter( threadManager,
                                              (TransactionManager)transactionManager.proxy() );
    }

    public void testUnregisterUnknownDestination() throws Exception
    {
        final DestinationRegistrar registrar = m_transactionalMessageRouter;
        final MockDestination destination = new MockDestination();

        try
        {
            registrar.unregister( destination );

            fail( "cannot unregister non-registered destination" );
        }
        catch( NoSuchDestinationException e )
        {
            assertEquals( destination, e.getDestination() );
        }
    }

    public void testDuplicateRegistration() throws Exception
    {
        final DestinationRegistrar registrar = m_transactionalMessageRouter;
        final MockDestination destination = new MockDestination();

        registrar.register( destination );

        try
        {
            registrar.register( destination );

            fail( "allowed duplication registration" );
        }
        catch( DuplicateRegistrationException e )
        {
            assertEquals( destination, e.getDestination() );
        }
    }

    private static class MockDestination implements Destination
    {
        public String getAddress()
        {
            return "test";
        }

        public boolean isValidMessage( final Object message )
        {
            return false;
        }

        public void deliver( final Object message )
        {
        }
    }
}