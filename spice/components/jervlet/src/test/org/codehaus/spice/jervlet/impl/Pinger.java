/*
 * Copyright (C) The Spice Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */
package org.codehaus.spice.jervlet.impl;

import java.util.ArrayList;
import java.util.List;

/**
 * Pinger class to be used in testcases
 *
 * @author Johan Sjoberg
 */
public class Pinger
{
    /** List of messages */
    ArrayList m_messages = new ArrayList();

    /**
     * Ping
     *
     * @param message a message
     */
    public void ping( String message )
    {
        m_messages.add( message );
    }

    /**
     * Fetch current ping messages
     *
     * @return a list of ping messages
     */
    public List getMessages()
    {
        return m_messages;
    }
}

