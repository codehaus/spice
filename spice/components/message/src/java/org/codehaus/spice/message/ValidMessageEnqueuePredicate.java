/*
 * Copyright (C) The Spice Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */
package org.codehaus.spice.message;

import org.d_haven.event.EnqueuePredicate;
import org.d_haven.event.Sink;

/**
 * EnqueuePredicate that only accepts valid messages
 *
 * @author <a href="mailto:peter.royal@pobox.com">peter royal</a>
 */
class ValidMessageEnqueuePredicate implements EnqueuePredicate
{
    private final Destination m_destination;

    public ValidMessageEnqueuePredicate( final Destination destination )
    {
        m_destination = destination;
    }

    public boolean accept( final Object element, final Sink modifyingSink )
    {
        return m_destination.isValidMessage( element );
    }

    public boolean accept( final Object[] elements, final Sink modifyingSink )
    {
        for( int i = 0; i < elements.length; i++ )
        {
            if( !accept( elements[i], modifyingSink ) )
            {
                return false;
            }
        }

        return true;
    }
}