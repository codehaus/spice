package net.sf.componentfarm.sqlexecutor.api;

import java.util.Iterator;
import java.sql.SQLException;

/**
 * Abstracts away the tedious JDBC calls associated with doing SQL work in Java.
 * You pass the sql you want executed, the bind variables for that sql, and a
 * callback to deal with the result set.
 *
 * @version 0.1
 * @author Mike Hogan
 */
public interface SqlExecutor {
    int execute(String sql, Iterator bindVariables, ResultSetHandler handler) throws SQLException;
}
