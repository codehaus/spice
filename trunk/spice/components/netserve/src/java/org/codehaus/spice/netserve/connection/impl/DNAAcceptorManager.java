package org.codehaus.spice.netserve.connection.impl;

import org.codehaus.dna.Configuration;
import org.codehaus.dna.ConfigurationException;
import org.codehaus.dna.Active;
import org.codehaus.dna.Configurable;
import org.codehaus.dna.LogEnabled;
import org.codehaus.dna.Logger;
import org.codehaus.spice.netserve.connection.impl.DefaultAcceptorManager;

/**
 * A DNA compliant implementation of AcceptorManager.
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
 * @version $Revision: 1.3 $ $Date: 2004-04-25 11:14:17 $
 * @dna.component
 * @dna.service type="org.codehaus.spice.netserve.connection.SocketAcceptorManager"
 * @see org.codehaus.spice.netserve.connection.impl.DefaultAcceptorManager
 */
public class DNAAcceptorManager
   extends DefaultAcceptorManager
   implements LogEnabled, Configurable, Active
{
   /**
    * @dna.logger
    */
   public void enableLogging( final Logger logger )
   {
      setMonitor( new DNAAcceptorMonitor( logger ) );
   }

   /**
    * @dna.configuration type="http://relaxng.org/ns/structure/1.0"
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
