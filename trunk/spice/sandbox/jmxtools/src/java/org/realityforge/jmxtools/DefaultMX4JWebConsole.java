package org.realityforge.jmxtools;

import java.io.File;
import java.util.ArrayList;
import javax.management.Attribute;
import javax.management.MBeanServer;
import javax.management.ObjectName;
import javax.management.MBeanServerFactory;

import org.jcontainer.dna.AbstractLogEnabled;
import org.jcontainer.dna.Active;
import org.jcontainer.dna.Composable;
import org.jcontainer.dna.Configurable;
import org.jcontainer.dna.Configuration;
import org.jcontainer.dna.ConfigurationException;
import org.jcontainer.dna.MissingResourceException;
import org.jcontainer.dna.ResourceLocator;

/**
 * @dna.component
 * @mx.component
 */
public class DefaultMX4JWebConsole
   extends AbstractLogEnabled
   implements Composable, Configurable, Active
{
   private static final String ADDRESS_SECTION = "bindAddress";
   private static final String PORT_SECTION = "port";
   private static final String USERS_SECTION = "users";
   private static final String USER_SECTION = "user";
   private static final String NAME_SECTION = "name";
   private static final String PASSWORD_SECTION = "password";

   private static final String DEFAULT_ADDRESS = "0.0.0.0";
   private static final int DEFAULT_PORT = 8080;


   private static final String[] ADD_AUTH_PARAM_TYPES = new String[]{String.class.getName(), String.class.getName()};

   private MBeanServer _mBeanServer;

   private String _address;
   private int _port;
   private UserConfig[] _users;

   private ObjectName _httpName;
   private ObjectName _processorName;

   /**
    * @dna.dependency type="MBeanServer"
    */
   public void compose( final ResourceLocator locator )
      throws MissingResourceException
   {
      if ( locator.contains( MBeanServer.class.getName() ) )
      {
         _mBeanServer = (MBeanServer)
            locator.lookup( MBeanServer.class.getName() );
      }
   }

   /**
    * @dna.configuration
    */
   public void configure( final Configuration configuration )
      throws ConfigurationException
   {
      _address = configuration.
         getChild( ADDRESS_SECTION ).getValue( DEFAULT_ADDRESS );
      _port = configuration.
         getChild( PORT_SECTION ).getValueAsInteger( DEFAULT_PORT );

      final Configuration[] children =
         configuration.getChild( USERS_SECTION ).getChildren( USER_SECTION );
      _users = new UserConfig[ children.length ];
      for ( int i = 0; i < children.length; i++ )
      {
         final Configuration child = children[ i ];
         final String username = child.getChild( NAME_SECTION ).getValue();
         final String password = child.getChild( PASSWORD_SECTION ).getValue();
         _users[ i ] = new UserConfig( username, password );
      }
   }

   public void initialize()
      throws Exception
   {
      if ( null == _mBeanServer )
      {
         final ArrayList list = MBeanServerFactory.findMBeanServer( null );
         if ( 1 == list.size() )
         {
            _mBeanServer = (MBeanServer) list.get( 0 );
         }
         else
         {
            final String message =
               "No MBeanServer specified for WebConsole " +
               "and multiple MBeanServers found in JVM. " +
               "Unable to determine which MBeanServer to " +
               "use.";
            getLogger().error( message );
            throw new Exception( message );
         }
      }
      setupHttpAdaptor();

      setupCommandProcessors();

      setupProcessor();

      try
      {
         _mBeanServer.invoke( _httpName, "start", null, null );
      }
      catch ( final Exception e )
      {
         getLogger().error( "Failed to start the MX Server.  " +
                            "Verify that the port '" + _port +
                            "' is available", e );
         throw e;
      }
   }

   private void setupHttpAdaptor()
      throws Exception
   {
      _httpName = new ObjectName( "Http:name=HttpAdaptor" );
      _mBeanServer.createMBean( "mx4j.adaptor.http.HttpAdaptor",
                                _httpName,
                                null );
      _mBeanServer.
         setAttribute( _httpName, new Attribute( "Host", _address ) );
      _mBeanServer.
         setAttribute( _httpName,
                       new Attribute( "Port", new Integer( _port ) ) );

      // use basic authentication
      _mBeanServer.setAttribute( _httpName,
                                 new Attribute( "AuthenticationMethod", "basic" ) );

      for ( int i = 0; i < _users.length; i++ )
      {
         final UserConfig user = _users[ i ];
         final Object[] params =
            new Object[]{user.getUsername(), user.getPassword()};
         _mBeanServer.invoke( _httpName,
                              "addAuthorization",
                              params,
                              ADD_AUTH_PARAM_TYPES );
      }
   }

   protected void setupProcessor()
      throws Exception
   {
      _processorName = new ObjectName( "Http:name=XSLTProcessor" );
      _mBeanServer.createMBean( "mx4j.adaptor.http.XSLTProcessor", _processorName, null );
      _mBeanServer.setAttribute( _processorName, new Attribute( "UseCache", new Boolean( false ) ) );
      _mBeanServer.setAttribute( _processorName, new Attribute( "File", ".." + File.separator + "xsl" ) );
      _mBeanServer.setAttribute( _httpName, new Attribute( "ProcessorName", _processorName ) );
   }

   protected void setupCommandProcessors()
      throws Exception
   {
   }

   public void dispose()
      throws Exception
   {
      if ( null != _httpName && _mBeanServer.isRegistered( _httpName ) )
      {
         _mBeanServer.invoke( _httpName, "stop", null, null );
         _mBeanServer.unregisterMBean( _httpName );
      }
      if ( null != _processorName && _mBeanServer.isRegistered( _processorName ) )
      {
         _mBeanServer.unregisterMBean( _processorName );
      }
   }
}
