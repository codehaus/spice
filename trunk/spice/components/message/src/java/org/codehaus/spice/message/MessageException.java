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
public class MessageException extends RuntimeException
{
    public MessageException( final String message )
    {
        super( message );
    }
}