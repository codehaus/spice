/*
 * Copyright (C) The Spice Group. All rights reserved.
 *
 * This software is published under the terms of the Spice
 * Software License version 1.1, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 */
 package org.jcomponent.netserve.connection.impl;

/**
 * Monitor interface for Connection.  
 * Provides a facade to support different types of events, including logging.
 * 
 * @author <a href="mailto:mauro.talevi at aquilonia.org">Mauro Talevi</a>
 */
public interface ConnectionMonitor
{

  void acceptorClosed( String name );

  void acceptorCreated( ConnectionAcceptor acceptor );

  void acceptorDisconnected( ConnectionAcceptor acceptor, 
                              boolean tearDown );

  void acceptorStopping( String name );
 
  void connectionEnding( String name, String hostAddress );

  void connectionStarting( String name, String hostAddress );

  void runnerAlreadyDisposed( ConnectionRunner runner );

  void runnerCreating( String name );

  void runnerDisposing( ConnectionRunner runner );

  void serverSocketClosing( String name );
  
  void serverSocketListening( String name );

  void unexpectedError( String message, 
                         Throwable t );


}
