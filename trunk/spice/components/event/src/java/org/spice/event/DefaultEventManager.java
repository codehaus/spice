/*
 * Copyright (C) The Spice Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */
package org.spice.event;

import java.util.EventObject;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.WeakHashMap;
import javax.transaction.SystemException;
import javax.transaction.Transaction;
import javax.transaction.TransactionManager;

import org.apache.avalon.framework.logger.Logger;

import EDU.oswego.cs.dl.util.concurrent.CopyOnWriteArraySet;
import EDU.oswego.cs.dl.util.concurrent.ReadWriteLock;
import EDU.oswego.cs.dl.util.concurrent.ReaderPreferenceReadWriteLock;
import EDU.oswego.cs.dl.util.concurrent.SyncMap;
import EDU.oswego.cs.dl.util.concurrent.WriterPreferenceReadWriteLock;
import org.drools.FactException;
import org.drools.RuleBase;
import org.drools.RuleBaseBuilder;
import org.drools.RuleIntegrationException;
import org.drools.RuleSetIntegrationException;
import org.drools.WorkingMemory;
import org.drools.rule.RuleSet;

/**
 * @author <a href="mailto:peter.royal@pobox.com">peter royal</a>
 */
public class DefaultEventManager implements EventPublisher, EventRegister
{
    private final Set m_subscribers = new CopyOnWriteArraySet();
    private final ReadWriteLock m_lock = new WriterPreferenceReadWriteLock();
    private final Map m_workingMemories = new SyncMap( new WeakHashMap(), new ReaderPreferenceReadWriteLock() );

    private final Logger m_logger;
    private final TransactionManager m_transactionManager;

    private RuleBase m_ruleBase;

    public DefaultEventManager( final TransactionManager transactionManager,
                                final Logger logger )
        throws RuleIntegrationException, RuleSetIntegrationException
    {
        m_transactionManager = transactionManager;
        m_logger = logger;

        rebuildRuleBase();
    }

    public void publish( final EventObject event )
    {
        try
        {
            m_lock.readLock().acquire();
        }
        catch( InterruptedException e )
        {
            throw new RuntimeException( "Unable to aquire read lock on RuleBase", e );
        }

        try
        {
            final WorkingMemory workingMemory = getWorkingMemory();

            workingMemory.assertObject( event );
            workingMemory.fireAllRules();
        }
        catch( FactException e )
        {
            throw new PublicationException( event, e );
        }
        catch( SystemException e )
        {
            throw new RuntimeException( "Transaction failure when processing event '" + event + "'", e );
        }
        finally
        {
            m_lock.readLock().release();
        }
    }

    /**
     * Get the <code>WorkingMemory</code> for the in-progress transaction. If there is no transaction in progress, a new
     * <code>WorkingMemory</code> is returned.
     *
     * We are only checking against the current Transaction instance, and not registering an XAResource since we don't
     * care about doing anything on commit/rollback.
     *
     * @return <code>WorkingMemory</code> instance
     *
     * @throws SystemException on failure to get current <code>Transaction</code>
     */
    private WorkingMemory getWorkingMemory() throws SystemException
    {
        final Transaction transaction = m_transactionManager.getTransaction();

        if( null == transaction )
        {
            return m_ruleBase.newWorkingMemory();
        }
        else
        {
            WorkingMemory workingMemory = (WorkingMemory)m_workingMemories.get( transaction );

            if( null == workingMemory )
            {
                workingMemory = m_ruleBase.newWorkingMemory();

                m_workingMemories.put( transaction, workingMemory );
            }

            return workingMemory;
        }
    }

    public void subscribe( final Subscriber subscriber )
    {
        if( null == subscriber )
        {
            throw new NullPointerException( "subscriber" );
        }

        if( getLogger().isDebugEnabled() ) getLogger().debug( "Adding subscriber: " + subscriber );

        m_subscribers.add( subscriber );

        try
        {
            rebuildRuleBase();
        }
        catch( RuleIntegrationException e )
        {
            final String msg = "Unable to rebuild RuleBase after adding Subscriber. "
                + "Reported '" + e.getMessage() + "' on rule: " + e.getRule();

            throw new EventManagerRuntimeException( msg, e );
        }
        catch( RuleSetIntegrationException e )
        {
            final String msg = "Unable to rebuild RuleBase after adding Subscriber. "
                + "Reported '" + e.getMessage() + "' on ruleset: " + e.getRuleSet().getName();

            throw new EventManagerRuntimeException( msg, e );
        }
    }

    public void unsubscribe( final Subscriber subscriber ) throws NoSuchSubscriberException
    {
        if( m_subscribers.remove( subscriber ) )
        {
            try
            {
                rebuildRuleBase();
            }
            catch( RuleIntegrationException e )
            {
                final String msg = "Unable to rebuild RuleBase after removing Subscriber. Subscriber modified rules "
                    + "after initial subscription or internal drools error. "
                    + "Reported '" + e.getMessage() + "' on rule: " + e.getRule();

                throw new EventManagerRuntimeException( msg, e );
            }
            catch( RuleSetIntegrationException e )
            {
                final String msg = "Unable to rebuild RuleBase after removing Subscriber. Subscriber modified rules "
                    + "after initial subscription or internal drools error. "
                    + "Reported '" + e.getMessage() + "' on ruleset: " + e.getRuleSet().getName();

                throw new EventManagerRuntimeException( msg, e );
            }
        }
        else
        {
            throw new NoSuchSubscriberException();
        }
    }

    private void rebuildRuleBase() throws RuleIntegrationException, RuleSetIntegrationException
    {
        try
        {
            m_lock.writeLock().acquire();
        }
        catch( InterruptedException e )
        {
            throw new RuntimeException( "Unable to aquire write lock on RuleBase", e );
        }

        try
        {
            final RuleBaseBuilder builder = new RuleBaseBuilder();
            final Iterator i = m_subscribers.iterator();

            while( i.hasNext() )
            {
                final Subscriber subscriber = (Subscriber)i.next();
                final RuleSet ruleSet = subscriber.getRuleSet();

                if( null == ruleSet )
                {
                    throw new EventManagerRuntimeException( "Subscriber had null RuleSet" );
                }

                builder.addRuleSet( ruleSet );
            }

            m_ruleBase = builder.build();
        }
        finally
        {
            m_lock.writeLock().release();
        }
    }

    private Logger getLogger()
    {
        return m_logger;
    }
}