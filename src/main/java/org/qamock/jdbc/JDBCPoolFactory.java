package org.qamock.jdbc;

import org.apache.commons.dbcp2.BasicDataSource;
import org.qamock.domain.Connection;

import java.sql.SQLException;

public interface JDBCPoolFactory {

    BasicDataSource getJdbcDataSource(Connection connection) throws SQLException;

}
