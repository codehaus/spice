/*
 * Copyright (C) The Spice Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */
package org.spice.event;

import org.drools.rule.Declaration;
import org.drools.spi.Condition;
import org.drools.spi.ConditionException;
import org.drools.spi.Tuple;

/**
 * @author <a href="mailto:peter.royal@pobox.com">peter royal</a>
 */
public class TupleMemberNotNullCondition implements Condition
{
    private final Declaration m_declaration;

    public TupleMemberNotNullCondition( final Declaration declaration )
    {
        if( null == declaration )
        {
            throw new NullPointerException( "declaration" );
        }

        m_declaration = declaration;
    }

    public Declaration[] getRequiredTupleMembers()
    {
        return new Declaration[]{m_declaration};
    }

    public boolean isAllowed( final Tuple tuple ) throws ConditionException
    {
        return null != tuple.get( m_declaration );
    }
}