/*
 * Copyright (C) The Spice Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */
package org.codehaus.spice.message;

/**
 * @author <a href="mailto:peter.royal@pobox.com">peter royal</a>
 */
public class InvalidMessageException extends RuntimeException
{
    private final String m_address;
    private final Object m_invalidMessage;

    public InvalidMessageException( final String address, final Object invalidMessage )
    {
        super( "Invalid invalidMessage '" + invalidMessage + "' for destination '" + address + "'" );

        m_address = address;
        m_invalidMessage = invalidMessage;
    }

    public String getAddress()
    {
        return m_address;
    }

    public Object getInvalidMessage()
    {
        return m_invalidMessage;
    }
}