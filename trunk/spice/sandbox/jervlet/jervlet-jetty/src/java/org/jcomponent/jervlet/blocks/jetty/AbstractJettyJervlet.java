package org.jcomponent.jervlet.blocks.jetty;

import org.mortbay.jetty.Server;
import org.mortbay.http.SocketListener;
import org.mortbay.util.MultiException;
import org.jcomponent.jervlet.JervletConfig;
import org.jcomponent.jervlet.JervletException;
import org.apache.avalon.framework.CascadingRuntimeException;

import java.net.UnknownHostException;
import java.util.HashMap;

public class AbstractJettyJervlet {

    protected JervletConfig config;
    protected Server m_server;
    protected HashMap m_webcontexts = new HashMap();

    protected final Server createHttpServer()
    {
        return new Server();
    }

    protected final SocketListener createSocketListener() throws UnknownHostException
    {
        SocketListener listener = new SocketListener();

        if( null != config.getHostName() )
        {
            listener.setHost( config.getHostName() );
        }

        listener.setPort( config.getPort() );
        listener.setMinThreads( config.getMinThreads() );
        listener.setMaxThreads( config.getMaxThreads() );
        return listener;
    }

    /**
     * Start
     */
    public final void start()
    {
        try
        {
            m_server.start();
        }
        catch( MultiException e )
        {
            throw new CascadingRuntimeException( "Some problem starting Jetty", e );
        }
    }

    /**
     * Stop
     */
    public final void stop()
    {
        try
        {
            m_server.stop();
        }
        catch( InterruptedException e )
        {
            throw new CascadingRuntimeException( "Some problem stopping Jetty", e );
        }
    }

    protected final WebApplicationContextHolder getWebApplicationContextHolder( String context )
        throws JervletException
    {
        WebApplicationContextHolder holder =
            (WebApplicationContextHolder)m_webcontexts.get( context );

        if( null == holder )
        {
            throw new JervletException( "Unknown context: " + context );
        }

        return holder;
    }
}
