package org.codehaus.spice.netserve.connection.impl;

import org.apache.avalon.framework.activity.Disposable;
import org.apache.avalon.framework.activity.Initializable;
import org.apache.avalon.framework.configuration.Configurable;
import org.apache.avalon.framework.configuration.Configuration;
import org.apache.avalon.framework.configuration.ConfigurationException;
import org.apache.avalon.framework.logger.LogEnabled;
import org.apache.avalon.framework.logger.Logger;

/**
 * An Avalon compliant implementation of AcceptorManager.
 *
 * <p>The component takes a single configuration parameter;
 * "shutdownTimeout". This specifies the amount of time to wait
 * while waiting for connections to shutdown gracefully. A
 * sample configuration follows;</p>
 * <pre>
 *  &lt;!-- wait 200ms for connections to gracefully shutdown --&gt;
 *  &lt;shutdownTimeout&gt;200&lt;/shutdownTimeout&gt;
 * </pre>
 *
 * @author Peter Donald
 * @author Mauro Talevi
 * @version $Revision: 1.1 $ $Date: 2004-07-10 13:06:21 $
 * @phoenix.component
 * @phoenix.service type="org.codehaus.spice.netserve.connection.SocketAcceptorManager"
 * @see org.codehaus.spice.netserve.connection.impl.DefaultAcceptorManager
 */
public class AvalonAcceptorManager
   extends DefaultAcceptorManager
   implements LogEnabled, Configurable, Initializable, Disposable
{
   /**
    * @phoenix.logger
    */
   public void enableLogging( final Logger logger )
   {
      setMonitor( new AvalonAcceptorMonitor( logger ) );
   }

   /**
    * @phoenix.configuration type="http://relaxng.org/ns/structure/1.0"
    *    location="AcceptorManager-schema.xml"
    */
   public void configure( final Configuration configuration )
      throws ConfigurationException
   {
      setShutdownTimeout( configuration.getChild( "shutdownTimeout" ).getValueAsInteger( 0 ) );
   }

   /**
    * Nothing to do to initial AcceptorManager.
    */
   public void initialize()
      throws Exception
   {
   }

   /**
    * Shutdown all connections.
    */
   public void dispose()
   {
      shutdownAcceptors();
   }
}
