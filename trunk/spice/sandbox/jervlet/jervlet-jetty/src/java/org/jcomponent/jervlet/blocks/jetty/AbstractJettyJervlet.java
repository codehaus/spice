package org.jcomponent.jervlet.blocks.jetty;

import org.mortbay.jetty.Server;
import org.mortbay.http.SocketListener;
import org.jcomponent.jervlet.JervletConfig;
import org.jcomponent.jervlet.JervletException;

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
