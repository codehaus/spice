package net.sf.componentfarm.sqlexecutor.impl;

import net.sf.componentfarm.sqlexecutor.api.SqlExecutor;
import net.sf.componentfarm.sqlexecutor.api.ResultSetHandler;

import java.util.Iterator;
import java.sql.SQLException;
import java.sql.ResultSet;
import java.sql.Connection;

/**
 * Simple implementation of SqlExecutor that requires a ConnectionFactory to
 * provide it JDBC connections.</p>
 *
 * Of course, this does nothing at all at the moment.  Its purpose is to act
 * as a test component for ComponentHaus work.  But it might be turned into
 * a real component one day.
 *
 * @version 0.1
 * @author Mike Hogan
 * @author Another Author
 */
public class SqlExecutorImpl implements SqlExecutor {
    private final ConnectionFactory connectionFactory;

    public SqlExecutorImpl(ConnectionFactory connectionFactory) {
        this.connectionFactory = connectionFactory;
    }

    public int execute(String sql, Iterator bindVariables, ResultSetHandler handler) throws SQLException {
        // do the query
        final Connection connection = connectionFactory.getConnection();
        final ResultSet resultSet = null;
        handler.handleResultSet(resultSet);
        resultSet.close();
        connection.close();
        return 0;
    }
}
