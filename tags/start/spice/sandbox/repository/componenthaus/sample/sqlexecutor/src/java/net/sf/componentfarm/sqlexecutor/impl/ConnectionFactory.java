package net.sf.componentfarm.sqlexecutor.impl;

import java.sql.Connection;

/**
 * This is a plug-interface for the SqlExecutorImpl implementation of SqlExecutor.</p>
 *
 * Those integrating SqlExecutorImpl into their systems must provide an implementation
 * of this interface so that SqlExecutorImpl can get a connection to the database.  Of course
 * these is no impl for this right now, because I only want the interface to be present
 * to see what we can do with plug-interfaces in Component Haus.
 *
 * @version 0.1
 * @author Mike Hogan
 */
public interface ConnectionFactory {
    Connection getConnection();
}
