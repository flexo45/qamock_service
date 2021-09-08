package org.qamock.app.main.jdbc;

import org.apache.commons.dbcp2.BasicDataSource;
import org.qamock.app.main.domain.Connection;

import java.sql.SQLException;

public interface JDBCPoolFactory {

    BasicDataSource getJdbcDataSource(Connection connection) throws SQLException;

}
