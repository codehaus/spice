package net.sf.componentfarm.sqlexecutor.api;

import java.sql.ResultSet;
import java.sql.SQLException;

public interface ResultSetHandler {
    void handleResultSet(ResultSet resultSet) throws SQLException;
}
