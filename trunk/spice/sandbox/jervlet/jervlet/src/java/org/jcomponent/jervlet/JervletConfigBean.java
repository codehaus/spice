package org.jcomponent.jervlet;


public class JervletConfigBean implements JervletConfig {

    /** Virtual host to bind the Jetty to.  null implies all hosts are in context. */
    private String m_hostName;

    private int m_port;
    private int m_minThreads;
    private int m_maxThreads;
    private boolean m_extractWebArchive;

    public boolean getExtractWarFile() {
        return m_extractWebArchive;
    }

    public void setExtractWarFile(boolean extractWebArchive) {
        this.m_extractWebArchive = extractWebArchive;
    }

    public int getMaxThreads() {
        return m_maxThreads;
    }

    public void setMaxThreads(int maxThreads) {
        this.m_maxThreads = maxThreads;
    }

    public int getMinThreads() {
        return m_minThreads;
    }

    public void setMinThreads(int minThreads) {
        this.m_minThreads = minThreads;
    }

    public int getPort() {
        return m_port;
    }

    public void setPort(int port) {
        this.m_port = port;
    }

    public String getHostName() {
        return m_hostName;
    }

    public void setHostName(String hostName) {
        this.m_hostName = hostName;
    }

}
