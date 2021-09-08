package org.qamock.app.main.jdbc;

import org.apache.commons.dbcp2.BasicDataSource;
import org.qamock.app.main.domain.Connection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class JDBCPoolFactoryImpl implements JDBCPoolFactory {

    private static final Logger logger = LoggerFactory.getLogger(JDBCPoolFactoryImpl.class);

    private long maxConnLifetimeMillis = 0;
    private int maxIdle = 1;

/*    @Autowired
    ConnectionDao connectionDao;*/

    /*@Autowired
    @Qualifier("jdbcTx")
    ResourceTransactionManager transactionManager;*/

    private Map<Long, BasicDataSource> dataSourcesPool = new HashMap<Long, BasicDataSource>();

    @Override
    public BasicDataSource getJdbcDataSource(Connection connection) throws SQLException{
        BasicDataSource dataSource = dataSourcesPool.get(connection.getId());
        if(dataSource == null){
            dataSource = createBasicDataSource(connection);
        }
        return dataSource;
    }

    //public ConnectionDao getConnectionsDao(){return connectionsDao;}
    //public void setConnectionsDao(ConnectionDao connectionsDao){this.connectionsDao = connectionsDao;}

    public void close() throws SQLException{
        for(Map.Entry<Long, BasicDataSource> dataSource : dataSourcesPool.entrySet()){
            dataSource.getValue().close();
        }
        logger.info("all pool closed");
    }


    private BasicDataSource createBasicDataSource(Connection connection) throws SQLException{

        BasicDataSource jdbcDataSource = new BasicDataSource();
        //((DataSourceTransactionManager)transactionManager).setDataSource(jdbcDataSource);
        jdbcDataSource.setMaxConnLifetimeMillis(maxConnLifetimeMillis);
        jdbcDataSource.setMaxIdle(maxIdle);

        switch (connection.getType()){
            case 0:
//                jdbcDataSource.setDriver(new OracleDriver());
                break;
            default:
//                jdbcDataSource.setDriver(new OracleDriver());
                break;
        }
        jdbcDataSource.setUrl(connection.getUrl());
        jdbcDataSource.setUsername(connection.getLogin());
        jdbcDataSource.setPassword(connection.getPassword());

        dataSourcesPool.put(connection.getId(), jdbcDataSource);

        return jdbcDataSource;
    }

    public void setMaxConnLifetimeMillis(long millis){this.maxConnLifetimeMillis = millis;}
    public void setMaxIdle(int max){this.maxIdle = max;}

}
