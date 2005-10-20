/*
 * Copyright (C) The Spice Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */
package org.codehaus.spice.jervlet.containers.jetty;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.Collections;

import org.codehaus.spice.jervlet.Container;
import org.codehaus.spice.jervlet.ContextHandler;
import org.codehaus.spice.jervlet.ContextMonitor;
import org.codehaus.spice.jervlet.Listener;
import org.codehaus.spice.jervlet.ListenerException;
import org.codehaus.spice.jervlet.ListenerHandler;
import org.codehaus.spice.jervlet.ListenerMonitor;
import org.codehaus.spice.jervlet.impl.NoopListenerMonitor;
import org.codehaus.spice.jervlet.impl.NoopContextMonitor;
import org.codehaus.spice.jervlet.impl.DefaultListener;
import org.mortbay.http.HttpListener;
import org.mortbay.http.SocketListener;
import org.mortbay.http.SunJsseListener;
import org.mortbay.http.ajp.AJP13Listener;
import org.mortbay.jetty.Server;

/**
 * Starting point for a Jetty server.
 * <br/><br/>
 * The <code>ListenerHandler</code> contract can be very limited in
 * many situations. Some functions of the Jetty server can be
 * parametrized through System parameters, but for most cases where
 * more advanced setups are needed it is best to feed the Constructor
 * an already configured Jetty Server and use this class for
 * deploying web applications. Note, this class is not intended
 * to be used as a component, but rather be used as a tool by
 * compnents.
 */
public class JettyServer implements Container, ListenerHandler
{
    /** The Jetty Server */
    private Server m_server;

    /** List of ContextHandlers given out */
    private final ArrayList m_contextHandlers;

    /** Listener monitor */
    private ListenerMonitor m_listenerMonitor;

    /** Context monitor */
    private ContextMonitor m_contextMonitor;

    /** Warning message, about null Listeners */
    private final static String NULL_LISTENER = "Given Listener was null.";

    /**
     * Create a new JettyServer instance
     * <br/><br/>
     * Note that calls to setServer, setListenerMonitor and
     * setContextMonitor are needed if this constructor is used. The
     * newly created instance will NOT work if all listed methods
     * aren't correctly called.
     */
    public JettyServer()
    {
        m_contextHandlers = new ArrayList();
    }

    /**
     * Set up a JettyServer instance.
     *
     * @param jettyServer a jetty Server instance, or null. If
     *        <code>null</code> is given a new unconfigured intance
     *        will be created.
     * @param listenerMonitor A listener monitor instance
     * @param contextMonitor  A context monitor instance
     */
    public JettyServer( final Server jettyServer,
                        final ListenerMonitor listenerMonitor,
                        final ContextMonitor contextMonitor )
    {
        this();
        setListenerMonitor( listenerMonitor );
        setContextMonitor( contextMonitor );
        setServer( jettyServer );
    }

    /**
     * Set the Jetty server. It can only be set once. Still a server
     * must be set before the class will work, either in the
     * constructor or here. <code>null</code> is an accepted value,
     * and if given, a new empty and unconfigured <code>Server</code>
     * instance will be created.
     *
     * @param jettyServer a jetty Server instance, or null. If
     *        <code>null</code> is given a new unconfigured intance
     *        will be created.
     */
    public void setServer( final Server jettyServer )
    {
        if( null == m_server )
        {
            if( null == jettyServer )
            {
                m_server = new Server();
            }
            else
            {
                m_server = jettyServer;
            }
        }
    }

    /**
     * Set the context monitor. A context monitor must be set before
     * the class will work, and can be set anytime again while the
     * system is running. Null is an accepted value and will result
     * in no monitor events.
     *
     * @param contextMonitor the context monitor, or null for NoOp
     */
    public void setContextMonitor( final ContextMonitor contextMonitor )
    {
        if( null == contextMonitor )
        {
            m_contextMonitor = new NoopContextMonitor();
        }
        else
        {
            m_contextMonitor = contextMonitor;
        }
    }

    /**
     * Set the listener monitor. A listener monitor must be set
     * before the class will work, and can be set anytime again while
     * the system is running. Null is an accepted value and will result
     * in no monitor events.
     *
     * @param listenerMonitor the listener monitor, or null for NoOp
     */
    public void setListenerMonitor( final ListenerMonitor listenerMonitor )
    {
        if( null == listenerMonitor )
        {
            m_listenerMonitor = new NoopListenerMonitor();
        }
        else
        {
            m_listenerMonitor = listenerMonitor;
        }
    }

    /**
     * Start the server
     *
     * @throws Exception if Jetty's <code>Server</code> threw one
     */
    public void start() throws Exception
    {
        m_server.start();
    }

    /**
     * Stop the server
     *
     * @throws InterruptedException if Jetty's server threw one
     */
    public void stop() throws InterruptedException
    {
        m_server.stop();
    }

    /**
     * Check if the server is started
     *
     * @return true if the server is started, else false
     */
    public boolean isServerStarted()
    {
        if( null == m_server )
        {
            return false;
        }
        else
        {
            return m_server.isStarted();
        }
    }

    /**
     * Create a new context handler.
     *
     * @return A new ContextHandler.
     */
    public ContextHandler createContextHandler()
    {
        final JettyContextHandler contextHandler =
          new JettyContextHandler( m_server, m_contextMonitor );
        m_contextHandlers.add( contextHandler );
        return contextHandler;
    }

    /**
     * Destroy a context handler. If the given context handler still
     * has contexts, they will be stopped and removed before
     * destruction.
     *
     * @param contextHandler the ContextHandler to destroy
     */
    public void destroyContextHandler( final ContextHandler contextHandler )
    {
        if( null == contextHandler ||
            !m_contextHandlers.contains( contextHandler ) )
        {
            return;
        }
        ( (JettyContextHandler)contextHandler ).destroy();
        m_contextHandlers.remove( contextHandler );
    }

    /**
     * Add a <code>Listener</code> the container.
     *
     * @param listener the Listener to add
     * @throws ListenerException if the listener couldn't be added
     *         (the host was unknown)
     */
    public void addListener( final Listener listener ) throws ListenerException
    {
        final HttpListener httpListener = createHttpListener( listener );
        try
        {
            httpListener.setHost( listener.getHost() );
        }
        catch( UnknownHostException ue )
        {
            m_listenerMonitor.addListenerException( getClass(), listener, ue );
        }
        httpListener.setHttpServer( m_server );
        httpListener.setPort( listener.getPort() );
        m_server.addListener( httpListener );
        m_listenerMonitor.addListenerNotification( getClass(), listener );
    }

    /**
     * Create a new Jetty <code>HttpListener</code>
     *
     * @param listener the listener to get information from
     * @return a new Jetty HTTP listener
     * @throws IllegalArgumentException if the listner type was
     *         unknown (not AJP13, HTTP or TSL)
     */
    private static HttpListener createHttpListener( final Listener listener )
    {
        switch( listener.getType() )
        {
            case Listener.AJP13: return new AJP13Listener();
            case Listener.HTTP: return new SocketListener();
            case Listener.TLS: return new SunJsseListener();
            default:
                throw new IllegalArgumentException( "Unknown listener type: "
                  + listener.getType() );
        }
    }

    /**
     * Remove a <code>Listener</code> from the container.
     *
     * @param listener the listener to remove
     */
    public void removeListener( final Listener listener )
    {
        if( null == listener )
        {
            m_listenerMonitor.removeListenerWarning( getClass(),
                                                     listener,
                                                     NULL_LISTENER );
        }
        else
        {
            final HttpListener[] listeners = m_server.getListeners();
            if( null != listeners && listeners.length > 0 )
            {
                for( int i = 0; i < listeners.length; i++ )
                {
                    if( listener.getPort() == listeners[i].getPort() &&
                        listener.getHost().equals( listeners[i].getHost() ) )
                    {
                        m_server.removeListener( listeners[i] );
                    }
                }
                m_listenerMonitor.removeListenerNotification( getClass(), listener );
            }
        }
    }

    /**
     * Start a <code>Listener</code>
     *
     * @param listener the listener to start
     */
    public void startListener( final Listener listener ) throws ListenerException
    {
        final HttpListener httpListener = getHttpListener( listener );
        if( null == httpListener )
        {
            m_listenerMonitor.startListenerWarning( getClass(),
                                                    listener,
                                                    NULL_LISTENER );
        }
        else if( !httpListener.isStarted() )
        {
            try
            {
                httpListener.start();
                m_listenerMonitor.startListenerNotification( getClass(), listener );
            }
            catch( Exception e )
            {
                m_listenerMonitor.startListenerException( getClass(), listener, e );
            }
        }
        else
        {
            m_listenerMonitor.startListenerWarning( getClass(), listener,
                                                    "Given Listener was already started." );
        }
    }

    /**
     * Stop a <code>Listener</code>.
     *
     * @param listener the listener to stop
     * @throws ListenerException if the listener couldn't be stopped
     */
    public void stopListener( final Listener listener ) throws ListenerException
    {
        final HttpListener httpListener = getHttpListener( listener );
        if( null == httpListener )
        {
            m_listenerMonitor.stopListenerWarning( getClass(),
                                                   listener,
                                                   NULL_LISTENER );
        }
        else if( httpListener.isStarted() )
        {
            try
            {
                httpListener.stop();
                m_listenerMonitor.stopListenerNotification( getClass(), listener );
            }
            catch( Exception e )
            {
                m_listenerMonitor.stopListenerException( getClass(), listener, e );
            }
        }
        else
        {
            m_listenerMonitor.stopListenerWarning( getClass(), listener,
                                                   "Given Listener was not started." );
        }
    }

    /**
     * Fetch a list of all current <code>Listener</code>s. If there
     * are no listener the returned list can be empty.
     *
     * @return All new list all current listeners.
     */
    public List getListeners()
    {
        final HttpListener[] listeners = m_server.getListeners();
        if( null == listeners || 0 == listeners.length )
        {
            return Collections.EMPTY_LIST;
        }
        else
        {
            final ArrayList listenerList = new ArrayList();
            for( int i = 0; i < listeners.length; i++ )
            {
                listenerList.add( createListener( listeners[i] ) );
            }
            return listenerList;
        }
    }

    /**
     * Check if a listener is started or not.
     *
     * @param listener The Listener to check
     *
     * @return true if the listener was handles by this container
     *         and started, else false.
     */
    public boolean isStarted( final Listener listener )
    {
        final HttpListener httpListener = getHttpListener( listener );
        if( null == httpListener )
        {
            return false;
        }
        return httpListener.isStarted();
    }

    /**
     * Fetch a Jetty <code>HttpListener</code> represented by a
     * Jervlet <code>Listener</code>.
     *
     * @param listener A listener
     *
     * @return The corresponding Jervlet HttpListener, or null
     */
    private HttpListener getHttpListener( final Listener listener )
    {
        if( null != listener )
        {
            final HttpListener[] listeners = m_server.getListeners();

            if( null != listeners && listeners.length != 0 )
            {
                for( int i = 0; i < listeners.length; i++ )
                {
                    if( listener.getPort() == listeners[i].getPort() &&
                        ( listener.getHost() == listeners[i].getHost() ||
                        listener.getHost().equals( listeners[i].getHost() ) ) )
                    {
                        return listeners[i];
                    }
                }
            }
        }
        return null;
    }

    /**
     * Create a new Listener representing the given HttpListener. A
     * new instance of a DefaultListener will be created.
     *
     * @param httpListener The Jetty HttpListener
     *
     * @return a new DefaultListener instance.
     */
    private Listener createListener( final HttpListener httpListener )
    {
        int type = -1;
        if( httpListener instanceof AJP13Listener )
        {
            type = Listener.AJP13;
        }
        else if( httpListener instanceof SunJsseListener )
        {
            type = Listener.TLS;
        }
        else if( httpListener instanceof SocketListener )
        {
            type = Listener.HTTP;
        }
        return new DefaultListener( httpListener.getHost(),
                                    httpListener.getPort(),
                                    type );
    }
}