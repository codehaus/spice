/*
 * Copyright (C) The Spice Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */
package org.spice.event;

/**
 * @author <a href="mailto:peter.royal@pobox.com">peter royal</a>
 */
public class EventManagerRuntimeException extends RuntimeException
{
    public EventManagerRuntimeException( final String message, final Throwable cause )
    {
        super( message, cause );
    }

    public EventManagerRuntimeException( final String message )
    {
        super( message );
    }
}