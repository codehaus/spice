package net.sf.componentfarm.sqlexecutor.impl;

import net.sf.componentfarm.sqlexecutor.api.ResultSetHandler;

import java.sql.ResultSet;
import java.sql.SQLException;

public class CountingResultSetHandler implements ResultSetHandler {
    private long count = 0;

    public void handleResultSet(ResultSet resultSet) throws SQLException {
        while(resultSet.next()) {
            count++;
        }
    }

    public long getCount() {
        return count;
    }
}
