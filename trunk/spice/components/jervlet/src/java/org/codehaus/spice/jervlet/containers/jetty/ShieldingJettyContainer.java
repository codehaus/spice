/*
 * Copyright (C) The Spice Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */
package org.codehaus.spice.jervlet.containers.jetty;

import java.util.List;
import java.util.Properties;

import org.codehaus.spice.jervlet.Context;
import org.codehaus.spice.jervlet.ContextException;
import org.codehaus.spice.jervlet.ContextHandler;
import org.codehaus.spice.jervlet.Listener;
import org.codehaus.spice.jervlet.ListenerException;
import org.codehaus.spice.jervlet.tools.isolate.SysPropertiesRedirector;

/**
 * Component representing a Jetty server.
 * <br/><br/>
 * Use this component to run, or as base for a Jetty server. You can
 * configure the server with a Jetty configuration, path to a Jetty
 * configuration or with an URL. This component can also let you set
 * system properties affecting <b>only this</b> instance of Jetty.
 * This means no real system properties are used, but Jetty will
 * believe so. Some of Jettys low level behavior can only be changed
 * like this. If you need to configure the Jetty Server with the
 * default configuration, you will probably want to at least set
 * <code>jetty.home</code> and such parameters. It is highly
 * recommended to use this as the starting point for a Jetty server.
 * Shielding the implementation is important in a component based
 * environment.
 * <br/><br/>
 * Following is a list of possible parameters Jetty can use. These
 * were copied from Jetty's website and might be outdated or not
 * affect the version of Jetty you run.
 * <br/><br/>
 * <font size="-1">
 * <table border="1" cellspacing="0" cellpadding="0">
 * <tr><th>Property Name</th><th>Default Value</th><th>Description</th></tr>
 * <tr><td colspan=3><b>JRE Properties</b></td></tr>
 * <tr><td>java.version</td><td>No default</td><td>JRE version</td></tr>
 * <tr><td>java.io.tmpdir</td><td>Platform dependent</td><td>Default temporary file path</td></tr>
 * <tr><td>line.separator</td><td>Platform dependent</td><td>Line separator ("/n" on *nix)</td></tr>
 * <tr><td>os.arch</td><td>Platform dependent</td><td>Operating system architecture</td></tr>
 * <tr><td>os.name</td><td>Platform dependent</td><td>Name of the operating system</td></tr>
 * <tr><td>os.version</td><td>Platform dependent</td><td>Version of the operating system</td></tr>
 * <tr><td>user.home</td><td>Platform dependent</td><td>User's home directory</td></tr>
 * <tr><td colspan=3><b>Jetty Properties</b></td></tr>
 * <tr><td>jetty.class.path</td><td>Not set</td><td>Adds extra class directories or jars onto the system classpath at startup</td></tr>
 * <tr><td>jetty.home</td><td>Determined at runtime</td><td>Path of the Jetty installation. The start.jar startup method uses an algorithm to search for this location unless this property has been set on the command line</td></tr>
 * <tr><td>jetty.ssl.keystore</td><td></td><td>See Jetty's FAQ entry on SSL for more info.</td></tr>
 * <tr><td>jetty.ssl.keystore.provider.class</td><td></td><td>See Jetty's FAQ entry on SSL for more info.</td></tr>
 * <tr><td>jetty.ssl.keystore.provider.name</td><td></td><td>See Jetty's FAQ entry on SSL for more info.</td></tr>
 * <tr><td>jetty.ssl.keystore.type</td><td></td><td>See Jetty's FAQ entry on SSL for more info.</td></tr>
 * <tr><td>jetty.ssl.keypassword</td><td></td><td>See Jetty's FAQ entry on SSL for more info.</td></tr>
 * <tr><td>jetty.ssl.password</td><td></td><td>See Jetty's FAQ entry on SSL for more info.</td></tr>
 * <tr><td>org.mortbay.http.HttpRequest.maxFormContentSize</td><td>200000</td><td>Limits the size of the data a client can push at the server</td></tr>
 * <tr><td>org.mortbay.http.PathMap.separators</td><td>:,</td><td>Separators used to specify multiple paths as a single string.<td></tr>
 * <tr><td>org.mortbay.http.Version.paranoid</td><td>false</td><td>If <code>true</code>, suppresses the Jetty version information in the response's HTTP headers</td></tr>
 * <tr><td>org.mortbay.jetty.servlet.SessionCookie</td><td>JSESSIONID</td><td>Name of cookie used for sessions</td></tr>
 * <tr><td>org.mortbay.jetty.servlet.SessionURL</td><td>jsessionid</td><td>Name of parameter when using URL rewriting for sessions</td></tr>
 * <tr><td>org.mortbay.util.ByteArrayPool.pool_size</td><td>8</td><td>Size of pool for recycling byte arrays.</td></tr>
 * <tr><td>org.mortbay.util.FileResource.checkAliases</td><td>true</td><td>If set to <code>false</code>, Jetty will allow requests for files that involve symbolic links or aliases</td></tr>
 * <tr><td>org.mortbay.util.TypeUtil.IntegerCacheSize</td><td>600</td><td>Size of cache for converting int and String to Integer</td></tr>
 * <tr><td>org.mortbay.util.URI.charset</td><td>UTF-8</td><td>Character encoding of URLs</td></tr>
 * <tr><td>org.mortbay.xml.XmlParser.NotValidating</td><td>false</td><td>If true, validation is <strong>not</strong> performed on the web.xml file</td></tr>
 * <tr><td>start.class</td><td></td><td>Specifies the name of the class of the Jetty server to invoke by the start.jar mechanism, overriding the default. For standard Jetty the default is org.mortbay.jetty.Server, for JettyPlus it is org.mortbay.jetty.plus.Server. Defaults are specified in a start.config file.</td></tr>
 * <tr><td>JETTY_NO_SHUTDOWN_HOOK</td><td>false</td><td>If false, a shutdown hook thread is registered with the runtime to stop the server when the runtime is shutdown</td></tr>
 * <tr><td>POOL_MAX</td><td>256</td><td>Used by the utility class org.mortbay.util.Pool to determine the default max number of items in the pool. Can be overridden by calling setMaxSize(int i). The org.mortbay.util.ThreadPool class uses Pool internally, thus this property can be used to effect the default size of the ThreadPool for servicing requests.</td></tr>
 * <tr><td>POOL_MIN</td><td>2</td><td>Used by the utility class org.mortbay.util.Pool to determine the minimum number of items in the pool. Can be overriden by calling setMinSize (int i). The org.mortbay.util.ThreadPool class uses Pool internally, thus this property can be used to effect the default minimum size of the ThreadPool for servicing requests.</td></tr>
 * <tr><td>ROLLOVERFILE_BACKUP_FORMAT</td><td>HHmmssSSS</td><td>Format for the date string that is appended to files during rollover.</td></tr>
 * <tr><td>ROLLOVERFILE_RETAIN_DAYS</td><td>31</td><td>Number of days before old request log files will be removed.</td></tr>
 * <tr><td>START</td><td>org/mortbay/start/start.config</td><td>Path to customized startup file. Used for example, by JettyPlus.</td></tr>
 * <tr><td>STOP.KEY</td><td>No default</td><td>If specified with the start.jar command, a random key is printed on stdout and must be used with the stop.jar command. Security measure to guard against unauthorized stop/starts.</td></tr>
 * <tr><td>STOP.PORT</td><td>8079</td><td>The local port used to signal to stop the server used with stop.jar. A value of 0 disables the mechanism. See also STOP.KEY</td></tr>
 * <tr><td>DEBUG</td><td>Not set</td><td>If true, enables debug messages when used with the Jetty logging plugin for Commons logging. See Jetty's FAQ for more info about logging.</td></tr>
 * <tr><td>DEBUG_PATTERNS</td><td>No default</td><td>Only for use with the Jetty log plugin for Commons logging. Specifies a list of patterns for classnames for which to generate debug messages. See Jetty's FAQ and tutorial for more info about logging.</td></tr>
 * <tr><td>DEBUG_VERBOSE</td><td>0</td><td>Set to a positive integer value, enables both trace and debug level messages. Set to 0, only debug messages are enabled. Only for use with the Jetty logging plugin for Commons logging. See Jetty's FAQ and tutorial for more info about logging.</td></tr>
 * <tr><td colspan=3><b>Other</b></td></tr>
 * <tr><td>org.apache.commons.logging.LogFactory</td><td>org.mortbay.log.Factory</td><td>Property used by the <a href="http://jakarta.apache.org/commons/logging">Apache Commons Logging API</a> to specify the classname of the LogFactory implementation. This property is set in the start.config file. For standard Jetty, it is the Jetty log implemenation org.mortbay.log.Factory. For JettyPlus, it is the Log4J implementation.</td></tr>
 * </table>
 * </font>
 *
 * @author Johan Sjoberg
 * @author Peter Royal
 */
public class ShieldingJettyContainer extends DefaultJettyContainer
{
    /** Jetty's system properties */
    private final Properties m_jettyProperties;

    /** Stored throwable form Jetty's inialization */
    private Throwable m_jettyThrowable;

    /**
     * Create a new ShieldingJettyContainer instance and set some default values.
     */
    public ShieldingJettyContainer()
    {
        m_jettyProperties = new Properties();
        m_jettyProperties.putAll( System.getProperties() );
    }

    /**
     * Set "system properties" for Jetty. Actually these properties
     * are set and unset on the current thread for each call to the
     * underlaying Jetty <code>Server</code>. Note that Jetty or
     * some of its dependencies might need some properties from
     * <code>System</code>, like <code>java.home</code>. If you
     * replace or clear out the properties here Jetty might not work.
     * Use <code>addJettyProperties</code> to add more properties to
     * the current set, or add all needed values to the new
     * <code>Properties</code> object.
     *
     * @param properties The new properties. If null or an empty
     *        <code>Properties</code> object is given, the current
     *        properties will be cleared out.
     */
    public void setJettyProperties( final Properties properties )
    {
        m_jettyProperties.clear();
        if( null != properties )
        {
            m_jettyProperties.putAll( properties );
        }
    }

    /**
     * Add more "system properties" for Jetty. Actually these
     * properties are set and unset on the current thread for
     * each call to the underlaying Jetty <code>Server</code>. The
     * initial value of the underlaying <code>Properties</code> are
     * copied from <code>System.getProperties()</code>.
     *
     * @param properties The new properties. If null or an empty
     *        <code>Properties</code> object is given, the current
     *        properties will be cleared out.
     */
    public void addJettyProperties( final Properties properties )
    {
        if( null != properties )
        {
            m_jettyProperties.putAll( properties );
        }
    }

    /**
     * Fetch the current set of Jetty properties.
     *
     * @return current jetty "system properties"
     */
    public Properties getJettyProperties()
    {
        return m_jettyProperties;
    }

    /**
     * Initialize and create a new instance of a Jetty
     * <code>Server</code>.
     * <br/><br/>
     * The server instance can only be created once, so all possible
     * system properties and configurations that should go to the
     * constructor must be set before calling this method.
     * <br/><br/>
     * A new thread will be spawned, shield itself, call
     * <code>initialize()</code>, unshield itself and join. If an
     * <code>Exception</code> was thrown from the Jetty server,
     * it is stored in <code>m_jettyThrowable</code> and rethrown
     * after joining again.
     *
     * @throws Exception on all errors
     */
    public void initialize() throws Exception
    {
        final Runnable runnable = new Runnable()
        {
            public void run()
            {
                SysPropertiesRedirector.install();
                SysPropertiesRedirector.bindProperties( m_jettyProperties );
                try
                {
                    ShieldingJettyContainer.super.initialize();
                }
                catch( Exception e )
                {
                    m_jettyThrowable = e;
                }
            }
        };

        final Thread shieldThread = new Thread( Thread.currentThread().getThreadGroup(),
                                                runnable,
                                                getClass().getName() + " initialization" );
        shieldThread.start();
        shieldThread.join();

        if( null != m_jettyThrowable )
        {
            throw (Exception)m_jettyThrowable;
        }
    }

    /**
     * Start the Jetty container
     *
     * @throws Exception on all errors
     */
    public void start() throws Exception
    {
        try
        {
            setupShield();
            super.start();
        }
        finally
        {
            teardownShield();
        }
    }

    /**
     * Stop the Jetty container
     *
     * @throws Exception on all errors
     */
    public void stop() throws Exception
    {
        try
        {
            setupShield();
            super.stop();
        }
        finally
        {
            teardownShield();
        }
    }

    /**
     * Create a new context handler.
     *
     * @return A new <code>ContextHandler</code> instance
     */
    public ContextHandler createContextHandler()
    {
        try
        {
            setupShield();
            return super.createContextHandler();
        }
        finally
        {
            teardownShield();
        }
    }

    /**
     * Destroy a context handler. If the given context handler still
     * has contexts, they will be stopped and removed before
     * destryction.
     *
     * @param contextHandler The <code>ContextHandler</code> to destroy
     */
    public void destroyContextHandler( final ContextHandler contextHandler )
    {
        try
        {
            setupShield();
            super.destroyContextHandler( contextHandler );
        }
        finally
        {
            teardownShield();
        }
    }

    /**
     * Add a <code>Listener</code> the container.
     *
     * @param listener the listener to add
     */
    public void addListener( final Listener listener ) throws ListenerException
    {
        try
        {
            setupShield();
            super.addListener( listener );
        }
        finally
        {
            teardownShield();
        }
    }

    /**
     * Remove a <code>Listener</code> from the container.
     *
     * @param listener the listener to remove
     */
    public void removeListener( final Listener listener )
    {
        try
        {
            setupShield();
            super.removeListener( listener );
        }
        finally
        {
            teardownShield();
        }
    }

    /**
     * Start a <code>Listener</code>.
     *
     * @param listener the listener to start
     * @throws ListenerException if the listener couldn't be started
     */
    public void startListener( final Listener listener ) throws ListenerException
    {
        setupShield();
        try
        {
            super.startListener( listener );
        }
        finally
        {
            teardownShield();
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
        setupShield();
        try
        {
            super.stopListener( listener );
        }
        finally
        {
            teardownShield();
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
        try
        {
            setupShield();

            return super.getListeners();
        }
        finally
        {
            teardownShield();
        }
    }

    /**
     * Check if a <code>Listener</code> is started or not.
     *
     * @param listener The listener
     *
     * @return True if the listener is started, else false.
     */
    public boolean isStarted( final Listener listener )
    {
        try
        {
            setupShield();
            return super.isStarted( listener );
        }
        finally
        {
            teardownShield();
        }
    }

    /**
     * Add a context to the container.
     *
     * @param context The context to add
     */
    public void addContext( final Context context )
    {
        try
        {
            setupShield();
            super.addContext( context );
        }
        finally
        {
            teardownShield();
        }
    }

    /**
     * Remove a context from the container.
     *
     * @param context The context to remove
     */
    public void removeContext( final Context context )
    {
        try
        {
            setupShield();
            super.removeContext( context );
        }
        finally
        {
            teardownShield();
        }
    }

    /**
     * Start a context.
     *
     * @param context The context to start
     * @throws ContextException if the context couldn't be started
     */
    public void startContext( final Context context ) throws ContextException
    {
        try
        {
            setupShield();
            super.startContext( context );
        }
        finally
        {
            teardownShield();
        }
    }

    /**
     * Stop a context.
     *
     * @param context The context to stop
     * @throws ContextException if the context couldn't be stopped
     */
    public void stopContext( final Context context ) throws ContextException
    {
        try
        {
            setupShield();
            super.startContext( context );
        }
        finally
        {
            teardownShield();
        }
    }

    /**
     * List the <code>Context</code>s that this
     * <code>ContextHandler</code> can manage.
     *
     * @return A new list of contexts holding all JervletContexts
     */
    public List getContexts()
    {
        try
        {
            setupShield();
            return super.getContexts();
        }
        finally
        {
            teardownShield();
        }
    }

    /**
     * Check if <code>Context</context> owned by this handler is
     * started on not.
     *
     * @param context The context to check
     * @return true if the given context is started, else false
     */
    public boolean isStarted( final Context context )
    {
        try
        {
            setupShield();
            return super.isStarted( context );
        }
        finally
        {
            teardownShield();
        }
    }

    /**
     * Set up a system property shield for the current thread.
     */
    private void setupShield()
    {
        SysPropertiesRedirector.install();
        SysPropertiesRedirector.bindProperties( m_jettyProperties );
    }

    /**
     * Tear down the system property shield for the current thread.
     */
    private void teardownShield()
    {
        SysPropertiesRedirector.uninstall();
    }
}

