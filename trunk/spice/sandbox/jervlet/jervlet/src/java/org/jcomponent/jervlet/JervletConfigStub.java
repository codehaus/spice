/*
 * Copyright (C) The Spice Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */

package org.jcomponent.jervlet;

public class JervletConfigStub implements JervletConfig {

    /** Virtual host to bind the Jetty to.  null implies all hosts are in context. */
    protected String m_hostName;

    protected int m_port;
    protected int m_minThreads;
    protected int m_maxThreads;
    protected boolean m_extractWebArchive;

    public boolean getExtractWarFile() {
        return m_extractWebArchive;
    }

    public int getMaxThreads() {
        return m_maxThreads;
    }

    public int getMinThreads() {
        return m_minThreads;
    }

    public int getPort() {
        return m_port;
    }

    public String getHostName() {
        return m_hostName;
    }

}
