/*
 * Copyright (C) The Spice Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */
package org.codehaus.spice.jervlet.impl.avalon;

import java.net.URL;
import java.net.MalformedURLException;

import org.apache.avalon.framework.context.Contextualizable;
import org.apache.avalon.framework.context.Context;
import org.apache.avalon.framework.logger.LogEnabled;
import org.apache.avalon.framework.logger.Logger;
import org.apache.avalon.framework.service.Serviceable;
import org.apache.avalon.framework.service.ServiceManager;
import org.apache.avalon.framework.configuration.Configurable;
import org.apache.avalon.framework.configuration.Configuration;
import org.apache.avalon.framework.configuration.ConfigurationException;
import org.apache.avalon.framework.activity.Initializable;

import org.codehaus.spice.jervlet.Container;
import org.codehaus.spice.jervlet.ContextException;
import org.codehaus.spice.jervlet.Instantiator;
import org.codehaus.spice.jervlet.impl.AbstractDeploymentHelper;

/**
 * Avalon component deploying Avalon style Servlet and Filter
 * components. The logger, context and service manager given to this
 * component are directly used for the <code>Instantiator</code>.
 * This means all dependencies of this component are seen by the
 * deployed classes. <code>SimpleAvalonDeployer</code> can also be
 * configured to deploy contexts upon initialization. The only mandatory
 * dependency for this component is a <code>Container</code>. The
 * container is used for creating one <code>ContextHandler</code> and
 * all contexts are deployed to it. If no <code>Instantiator</code>
 * is available from the <code>ServiceManager</code> a new
 * <code>AvalonInstantiator</code> is created and used.
 *
 * @dna.component
 * @author Johan Sjoberg
 */
public class SimpleAvalonDeployer extends AbstractDeploymentHelper
    implements LogEnabled, Contextualizable, Configurable, Serviceable, Initializable
{
    /** The service manager */
    private ServiceManager m_serviceManager;

    /** The logger */
    private Logger m_logger;

    /** The context */
    private Context m_context;
    
    /** The configuration */
    private Configuration m_configuration;

    /**
     * Set the logger
     *
     * @dna.logger
     */
    public void enableLogging( Logger logger )
    {
        m_logger = logger;
    }    
    
    /**
     * Set the context
     */
    public void contextualize( Context context )
    {
        m_context = context;
    }
    
    /**
     * Set the service manager
     * <br/><br/>
     * Note, the Instantiator is an optional dependency. If given, it
     * will be used, otherwise a new <code>AvalonInstantiator</code>
     * is created.
     *
     * @dna.dependency type="org.codehaus.spice.jervlet.Container"
     * @dna.dependency type="org.codehaus.spice.jervlet.Instantiator" optional="true"
     */
    public void service( ServiceManager serviceManager )
    {
        m_serviceManager = serviceManager;
    }
    
    /**
     * Set the configuration
     * <br/><br/>
     * The configuration can be empty. In this case no contexts will be
     * automatically deployed. The format is:
     * <br/>
     * <pre>
     * &lt;contexts&gt;
     *     &lt;!-- Enumeration of "context" elements --&gt;
     *     &lt;context&gt;
     *         &lt;!-- The path, e.g. "mywebapp" --&gt;
     *         &lt;path&gt;&lt;/path&gt;
     *         &lt;-- Resource URL, e.g. "file:///usr/local/java/webapps/mywebapp.war" --&gt;
     *         &lt;resource&gt;&lt;/resource&gt;
     *         &lt;!-- Virtual hosts (Optional) --&gt;
     *         &lt;virtual-hosts&gt;
     *             &lt;!-- Enumeration ov "virtual-host" elements, e.g. "www.biz.com" --&gt;
     *             &lt;virtual-host&gt;&lt;/virtual-host&gt;
     *         &lt;/virtual-hosts&gt;
     *     &lt;/context&gt;
     * &lt;/contexts&gt;
     *</pre>
     *
     * @param configuration The Avalon configuration
     */
    public void configure( Configuration configuration )
    {
        m_configuration = configuration;
    }

    /**
     * Initialize the component
     * <br/><br/>
     * If no Instantiator was given, an AvalonInstantiator is created
     * here with this component's logger, context and service
     * manager. If this compoent was configured with contexts in its
     * configuration they are also deployed here.
     *
     * @throws Exception on all errors
     */
    public void initialize() throws Exception
    {
        Instantiator instantiator = null;
        if( m_serviceManager.hasService( Instantiator.ROLE ) )
        {
            instantiator = (Instantiator)m_serviceManager.lookup(
              Instantiator.ROLE );
        }
        else
        {
            m_logger.info( "Creating a new AvalonInstantiator." );
            instantiator = new AvalonInstantiator(
              m_context, m_serviceManager, m_logger );
        }
        setInstantiator( instantiator );
        
        final Container container =
          (Container)m_serviceManager.lookup( Container.ROLE );
        setContextHandler( container.createContextHandler() );
        deployFromConfiguration( m_configuration );
    }
    
    /**
     * Deploy contexts from a configuration.
     * 
     * @see #configure( Configuration configuration ) for the
     *      configuration format.
     * @param configuration The configuration
     */
    private void deployFromConfiguration( Configuration configuration )
      throws ConfigurationException, MalformedURLException, ContextException
    {
       Configuration[] contextConfigurations = configuration
         .getChild( "contexts", true ).getChildren( "context" );
       if( null != contextConfigurations )
       {
           for( int i = 0; i < contextConfigurations.length; i++ )
           {
               String path =
                 contextConfigurations[i].getChild( "path" ).getValue();
               String resourceString =
                 contextConfigurations[i].getChild( "resource" ).getValue();
               URL resource = new URL( resourceString );               
               Configuration[] virtualHostConfigurations =
                 contextConfigurations[i].getChild( "virtual-hosts", true )
                 .getChildren( "virtual-host" );
               String[] virtualHosts = null;
               if( null != virtualHostConfigurations )
               {
                   virtualHosts = new String[virtualHostConfigurations.length];
                   for( int j = 0; j < virtualHosts.length; j++ )
                   {
                       virtualHosts[j] = virtualHostConfigurations[j].getValue();
                   }
               }
               deployContext( path, virtualHosts, resource );
           }
       }
    }
}
