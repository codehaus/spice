package org.realityforge.connector;

/**
 * Add in interface representing Connection
 * managed by Connector.
 */
public interface ConnectorConnection
{
   /**
    * Establish the connection.
    *
    * @throws Exception if unable to connect
    */
   void connect()
      throws Exception;

   /**
    * Disconnect the connection.
    *
    * @throws Exception if unable to connect
    */
   void disconnect()
      throws Exception;

   /**
    * Validate the connection. The validation should
    * involve explicitly testing that that the
    * connection is valid.
    *
    * @throws Exception if unable to connection is not valid
    */
   void validateConnection()
      throws Exception;
}
