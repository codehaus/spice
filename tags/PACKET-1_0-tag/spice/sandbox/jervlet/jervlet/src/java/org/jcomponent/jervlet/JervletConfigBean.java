/*
 * Copyright (C) The Spice Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */

package org.jcomponent.jervlet;

public class JervletConfigBean extends JervletConfigStub {

    public void setExtractWarFile(boolean extractWebArchive) {
        this.m_extractWebArchive = extractWebArchive;
    }

    public void setMaxThreads(int maxThreads) {
        this.m_maxThreads = maxThreads;
    }

    public void setMinThreads(int minThreads) {
        this.m_minThreads = minThreads;
    }

    public void setPort(int port) {
        this.m_port = port;
    }

    public void setHostName(String hostName) {
        this.m_hostName = hostName;
    }

}
