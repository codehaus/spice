/*
 * Copyright (C) The Spice Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */
package org.jcomponent.threadpool.impl;

import org.apache.avalon.framework.logger.LogEnabled;
import org.apache.avalon.framework.logger.Logger;
import org.apache.avalon.framework.configuration.Configurable;
import org.apache.avalon.framework.configuration.ConfigurationException;
import org.apache.avalon.framework.configuration.Configuration;
import org.apache.avalon.framework.activity.Initializable;
import org.apache.avalon.framework.activity.Disposable;
import org.apache.avalon.framework.container.ContainerUtil;
import org.apache.commons.pool.impl.GenericObjectPool;


/**
 * The CommonsThreadPool is a component that provides a basic
 * mechanism for pooling threads. A sample configuration for this
 * component is;
 * <pre>
 * &lt;config&gt;
 *   &lt;name&gt;MyThreadPool&lt;/name&gt; &lt;!-- base name of all threads --&gt;
 *   &lt;priority&gt;5&lt;/priority&gt; &lt;!-- set to default priority --&gt;
 *   &lt;is-daemon&gt;false&lt;/is-daemon&gt; &lt;!-- are threads daemon threads? --&gt;
 *   &lt;resource-limiting&gt;false&lt;/resource-limiting&gt; &lt;!-- will pool block when max threads reached? --&gt;
 *   &lt;max-threads&gt;10&lt;/max-threads&gt;
 *   &lt;max-idle&gt;5&lt;/max-idle&gt; &lt;!-- maximum number of idle threads --&gt;
 * &lt;/config&gt;
 * </pre>
 *
 * @author <a href="mailto:peter at realityforge.org">Peter Donald</a>
 * @version $Revision: 1.2 $ $Date: 2003-08-29 07:42:04 $
 * @phoenix.service type="ThreadPool"
 */
public class AvalonCommonsThreadPool
   extends CommonsThreadPool
   implements LogEnabled, Configurable, Initializable, Disposable
{
   /**
    * The logger for component.
    */
   private Logger m_logger;

   /**
    * Set the logger for component.
    *
    * @param logger the logger for component.
    */
   public void enableLogging( final Logger logger )
   {
      m_logger = logger;
   }

   /**
    * Configure the pool. See class javadocs for example.
    *
    * @param configuration the configuration object
    * @throws ConfigurationException if malformed configuration
    * @phoenix.configuration
    *    type="http://relaxng.org/ns/structure/1.0"
    *    location="CommonsThreadPool-schema.xml"
    */
   public void configure( final Configuration configuration )
      throws ConfigurationException
   {
      final String name =
         configuration.getChild( "name" ).getValue();
      setName( name );
      final int priority =
         configuration.getChild( "priority" ).getValueAsInteger( Thread.NORM_PRIORITY );
      setPriority( priority );
      final boolean isDaemon =
         configuration.getChild( "is-daemon" ).getValueAsBoolean( false );
      setDaemon( isDaemon );

      final GenericObjectPool.Config config = getCommonsConfig();

      final boolean limit =
         configuration.getChild( "resource-limiting" ).getValueAsBoolean( false );
      if ( limit )
      {
         config.whenExhaustedAction = GenericObjectPool.WHEN_EXHAUSTED_BLOCK;
      }
      else
      {
         config.whenExhaustedAction = GenericObjectPool.WHEN_EXHAUSTED_GROW;
      }

      config.maxActive =
         configuration.getChild( "max-threads" ).getValueAsInteger( 10 );
      config.maxIdle = configuration.getChild( "max-idle" ).
         getValueAsInteger( config.maxActive / 2 );
   }

   /**
    * Initialize the monitor then initialize parent class.
    */
   public void initialize()
   {
      final AvalonThreadPoolMonitor monitor = new AvalonThreadPoolMonitor();
      ContainerUtil.enableLogging( monitor, m_logger );
      setMonitor( monitor );
      super.initialize();
   }
}
