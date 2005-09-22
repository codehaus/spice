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
 * Created by IntelliJ IDEA.
 * User: sjoberg
 * Date: 04-Sep-2005
 * Time: 23:50:51
 * To change this template use File | Settings | File Templates.
 */
public class Pinger
{
    ArrayList m_messages = new ArrayList();

    public void ping( String message )
    {
        m_messages.add( message );
    }

    public List getMessages()
    {
        return m_messages;
    }
}

