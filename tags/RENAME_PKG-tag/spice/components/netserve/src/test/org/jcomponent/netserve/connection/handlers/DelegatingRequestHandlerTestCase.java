package org.jcomponent.netserve.connection.handlers;

import java.net.Socket;

import com.mockobjects.dynamic.C;
import com.mockobjects.dynamic.Mock;
import junit.framework.TestCase;
import org.jcomponent.netserve.connection.RequestHandler;

public class DelegatingRequestHandlerTestCase
   extends TestCase
{
   public void testNullPassedIntoCtor()
      throws Exception
   {
      try
      {
         new DelegatingRequestHandler( null );
      }
      catch ( final NullPointerException npe )
      {
         assertEquals( "npe.getMessage()", "handler", npe.getMessage() );
         return;
      }
   }

   public void testDelegateHandlerInvoked()
      throws Exception
   {
      final Mock mockHandler = new Mock( RequestHandler.class );
      final Socket socket = new Socket();
      final Long timeout = new Long( 23 );
      mockHandler.expect( "handleConnection", C.args( C.eq( socket ) ) );
      mockHandler.expect( "shutdown", C.args( C.eq( timeout ) ) );
      final RequestHandler handler = (RequestHandler) mockHandler.proxy();
      final DelegatingRequestHandler delegatingHandler =
         new DelegatingRequestHandler( handler );

      delegatingHandler.handleConnection( socket );
      delegatingHandler.shutdown( 23 );
   }
}
