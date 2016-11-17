package org.qamock.jdbc;

import org.apache.commons.dbcp2.BasicDataSource;
import org.springframework.beans.factory.annotation.Autowired;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class JDBCPoolFactory {

    private long maxConnLifetimeMillis = 0;
    private int maxIdle = 1;

    @Autowired
    DataSource jdbcDataSource;

    private Map<String, DataSource> dataSourcesPool = new HashMap<String, DataSource>();

    private DataSource createOracleDataSource(Object connectionPoolData) throws SQLException{

        /*BasicDataSource dataSource = new BasicDataSource();

        dataSource.setDriverClassName("connectionPoolData.class");
        dataSource.setUrl("connectionPoolData.url");
        dataSource.setUsername("connectionPoolData.username");
        dataSource.setPassword("connectionPoolData.password");
        dataSource.setMaxConnLifetimeMillis(maxConnLifetimeMillis);
        dataSource.setMaxIdle(maxIdle);

        dataSourcesPool.put("connectionPoolData.name", dataSource);

        return dataSource;*/


        //((BasicDataSource)jdbcDataSource).setDriverClassName("connectionPoolData.class");
        ((BasicDataSource)jdbcDataSource).setUrl("connectionPoolData.url");
        ((BasicDataSource)jdbcDataSource).setUsername("connectionPoolData.username");
        ((BasicDataSource)jdbcDataSource).setPassword("connectionPoolData.password");

        dataSourcesPool.put("connectionPoolData.name", jdbcDataSource);

        return jdbcDataSource;
    }

    public void setMaxConnLifetimeMillis(long millis){this.maxConnLifetimeMillis = millis;}
    public void setMaxIdle(int max){this.maxIdle = max;}

    public DataSource getOracleDataSource(Object connectionPoolData) throws SQLException{
        DataSource dataSource = dataSourcesPool.get("connectionPoolData.name");
        if(dataSource == null){
            dataSource = createOracleDataSource(connectionPoolData);
        }
        return dataSource;
    }

}
