/*
 * Copyright (C) The Spice Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */
package org.codehaus.spice.message;

import java.util.Map;
import java.util.WeakHashMap;
import javax.transaction.RollbackException;
import javax.transaction.SystemException;
import javax.transaction.Transaction;
import javax.transaction.TransactionManager;

import EDU.oswego.cs.dl.util.concurrent.ConcurrentHashMap;
import EDU.oswego.cs.dl.util.concurrent.ReaderPreferenceReadWriteLock;
import EDU.oswego.cs.dl.util.concurrent.SyncMap;
import org.d_haven.event.EnqueuePredicateFailedException;
import org.d_haven.event.Sink;
import org.d_haven.event.SinkException;
import org.d_haven.event.command.ThreadManager;

/**
 * Default implementation of a {@link DestinationRegistrar} and {@link Dispatcher}
 *
 * @author <a href="mailto:peter.royal@pobox.com">peter royal</a>
 */
public class TransactionalMessageRouter implements DestinationRegistrar, Dispatcher
{
    private final Map m_registrations = new ConcurrentHashMap();
    private final Map m_transactionResources = new SyncMap( new WeakHashMap(), new ReaderPreferenceReadWriteLock() );

    private final ThreadManager m_threadManager;
    private final TransactionManager m_transactionManager;
    private final DestinationMonitor m_monitor;

    public TransactionalMessageRouter( final ThreadManager threadManager,
                                       final TransactionManager transactionManager,
                                       final DestinationMonitor monitor )
    {
        if( null == threadManager )
        {
            throw new NullPointerException( "threadManager" );
        }
        else if( null == transactionManager )
        {
            throw new NullPointerException( "transactionManager" );
        }
        else if( null == monitor )
        {
            throw new NullPointerException( "monitor" );
        }

        m_threadManager = threadManager;
        m_transactionManager = transactionManager;
        m_monitor = monitor;
    }

    public void register( final Destination destination ) throws DuplicateRegistrationException
    {
        if( m_registrations.containsKey( destination.getAddress() ) )
        {
            throw new DuplicateRegistrationException( destination );
        }
        else
        {
            addDestination( destination );
        }
    }

    private void addDestination( final Destination destination )
    {
        final DestinationEventPipeline pipeline = new DestinationEventPipeline( destination, m_monitor );

        m_threadManager.register( pipeline );
        m_registrations.put( destination.getAddress(), pipeline );
    }

    public void unregister( final Destination destination ) throws NoSuchDestinationException
    {
        if( !m_registrations.containsKey( destination.getAddress() ) )
        {
            throw new NoSuchDestinationException( destination );
        }
        else
        {
            removeDestination( destination );
        }
    }

    private void removeDestination( final Destination destination )
    {
        final String address = destination.getAddress();
        final DestinationEventPipeline pipeline = getDestinationEventPipeline( address );

        m_threadManager.unregister( pipeline );
        m_registrations.remove( address );
    }

    public void send( final String address, final Object message )
    {
        final DestinationEventPipeline pipeline = getDestinationEventPipeline( address );

        if( null != pipeline )
        {
            try
            {
                final MessageTransactionSynchronization synchronization = getMessageTransactionResource();
                final Sink sink = pipeline.getSink();

                if( null == synchronization )
                {
                    sink.enqueue( message );
                }
                else
                {
                    synchronization.addEnqueue( sink.prepareEnqueue( new Object[]{message} ) );
                }
            }
            catch( EnqueuePredicateFailedException e )
            {
                throw new InvalidMessageException( address, message );
            }
            catch( SinkException e )
            {
                final String msg = "Unable to send message '" + message + "' to destination '" + address + "'";
                throw new RuntimeException( msg, e );
            }
            catch( SystemException e )
            {
                final String msg = "Transaction failure when sending message '" + message
                    + "' to destination '" + address + "'";
                throw new RuntimeException( msg, e );
            }
            catch( RollbackException e )
            {
                final String msg = "Attempted to send message during rolled back transaction '" + message
                    + "' to destination '" + address + "'";
                throw new RuntimeException( msg, e );
            }
        }
    }

    private MessageTransactionSynchronization getMessageTransactionResource() throws SystemException,
        RollbackException
    {
        final Transaction transaction = m_transactionManager.getTransaction();

        if( null == transaction )
        {
            return null;
        }
        else
        {
            MessageTransactionSynchronization synchronization =
                (MessageTransactionSynchronization)m_transactionResources.get( transaction );

            if( null == synchronization )
            {
                synchronization = new MessageTransactionSynchronization();
                transaction.registerSynchronization( synchronization );
                m_transactionResources.put( transaction, synchronization );
            }

            return synchronization;
        }
    }

    private DestinationEventPipeline getDestinationEventPipeline( final String address )
    {
        return (DestinationEventPipeline)m_registrations.get( address );
    }
}