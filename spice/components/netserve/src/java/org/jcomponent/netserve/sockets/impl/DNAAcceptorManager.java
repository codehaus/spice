package org.jcomponent.netserve.sockets.impl;

import org.jcontainer.dna.Configuration;
import org.jcontainer.dna.ConfigurationException;
import org.jcontainer.dna.Active;
import org.jcontainer.dna.Configurable;

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
 * @author <a href="mailto:peter at realityforge.org">Peter Donald</a>
 * @version $Revision: 1.1 $ $Date: 2003-10-27 05:11:32 $
 * @dna.component
 * @dna.service type="org.jcomponent.netserve.sockets.SocketAcceptorManager"
 * @see DefaultAcceptorManager
 */
public class DNAAcceptorManager
   extends DefaultAcceptorManager
   implements Configurable, Active
{
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
