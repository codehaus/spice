/*
 * Copyright (C) The Spice Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */
package org.spice.message;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.transaction.Status;
import javax.transaction.Synchronization;

import org.d_haven.event.PreparedEnqueue;

/**
 * <code>Synchronization</code> That deals with PreparedEnqueue objects
 *
 * @author <a href="mailto:peter.royal@pobox.com">peter royal</a>
 */
class MessageTransactionSynchronization implements Synchronization
{
    private final List m_enqueues = new ArrayList();

    public void commit()
    {
        final Iterator i = m_enqueues.iterator();

        while( i.hasNext() )
        {
            ( (PreparedEnqueue)i.next() ).commit();
        }
    }

    public void rollback()
    {
        final Iterator i = m_enqueues.iterator();

        while( i.hasNext() )
        {
            ( (PreparedEnqueue)i.next() ).abort();
        }
    }

    void addEnqueue( final PreparedEnqueue enqueue )
    {
        m_enqueues.add( enqueue );
    }

    public void afterCompletion( final int status )
    {
        switch( status )
        {
            case Status.STATUS_COMMITTED:
                commit();
                break;
            default:
                rollback();
                ;
                break;
        }
    }

    public void beforeCompletion()
    {
    }
}