package org.jcomponent.jervlet.blocks.jetty;

import org.mortbay.util.LogSink;
import org.mortbay.util.Frame;

public class SimpleLogSink implements LogSink {

    /**
     * Stop (unimpled)
     * @throws InterruptedException
     */
    public void stop() throws InterruptedException
    {
    }

    /**
     * Is this started (always)
     * @return
     */
    public boolean isStarted()
    {
        return true;
    }

    /**
     * Set Options (un implemented)
     * @param s the options
     */
    public void setOptions( String s )
    {
    }

    /**
     * Get Option (unimplemented)
     * @return the options
     */
    public String getOptions()
    {
        return "";
    }

    /**
     * Start a logger (unimpled)
     * @throws Exception
     */
    public void start() throws Exception
    {
    }

    public void log(String string) {
    }

    public void log(String string, Object object, Frame frame, long l) {
    }
}
