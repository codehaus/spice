package net.sf.componentfarm.sqlexecutor.api;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.ResultSet;
import java.sql.Statement;

/**
 * Small utility class that performs common operations
 * involved in using JDBC.
 */
public class SqlUtil {
    /**
     * Close the given Connection, swallowing any SQLException that might be
     * thrown.  The connection can be null, in which case no action will be taken.
     */
    public static void close(Connection connection) {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                //ignore
            }
        }
    }

    /**
     * Close the given ResultSet, swallowing any SQLException that might be
     * thrown.  The ResultSet can be null, in which case no action will be taken.
     */
    public static void close(ResultSet resultSet) {
        if (resultSet != null) {
            try {
                resultSet.close();
            } catch (SQLException e) {
                //ignore
            }
        }
    }

    /**
     * Close the given Statement, swallowing any SQLException that might be
     * thrown.  The Statement can be null, in which case no action will be taken.
     */
    public static void close(Statement statement) {
        if (statement != null) {
            try {
                statement.close();
            } catch (SQLException e) {
                //ignore
            }
        }
    }
}
