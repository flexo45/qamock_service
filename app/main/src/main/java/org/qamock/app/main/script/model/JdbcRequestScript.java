package org.qamock.app.main.script.model;

import org.apache.commons.dbcp2.BasicDataSource;
import org.qamock.app.main.service.ScriptService;
import org.qamock.app.main.jdbc.JDBCPoolFactory;
import org.qamock.app.main.script.exception.ScriptExecutionException;
import org.qamock.app.main.script.exception.ScriptExtractionException;
import org.qamock.app.main.script.handler.ScriptSuiteProcessor;
import org.qamock.app.main.script.helper.Helper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class JdbcRequestScript implements ScriptStep, Serializable {

    private static final Logger logger = LoggerFactory.getLogger(JdbcRequestScript.class);

    private static final long serialVersionUID = 4576495647435352835L;

    public JdbcRequestScript(String type, String connectionName){
        this.type = type;
        this.connectionName = connectionName;
        extractors = new ArrayList<JdbcSelectExtractor>();
    }

    @Autowired
    JDBCPoolFactory jdbcPoolFactory;

    @Autowired
    ScriptService scriptService;

    @Autowired
    JdbcTemplate jdbcTemplate;

    private ScriptSuiteProcessor scriptSuiteProcessor;
    private String query;
    private List<String> params;
    private String connectionName;
    private String type;
    private List<JdbcSelectExtractor> extractors;
    private List<Map<String, Object>> result;

    public void addExtractor(String target, String value, String to){
        extractors.add(new JdbcSelectExtractor(target, value, to));
    }

    public void setScriptSuiteProcessor(ScriptSuiteProcessor v){scriptSuiteProcessor = v;}

    public void addSelectStatement(String query, List<String> params){
        this.query = query;
        this.params = params;
    }

    @Override
    public String getType() {
        return JdbcRequestScript.class.getSimpleName();
    }

    @Override
    public void run() throws ScriptExecutionException {
        try {
            if(type.equals("select")){
                select();
            }
            else if(type.equals("update")){

            }
            else if(type.equals("procedure")){

            }
        }
        catch (Exception e){
            throw new ScriptExecutionException(e);
        }
    }

    @Override
    public void extract() throws ScriptExtractionException {
        for(JdbcSelectExtractor extractor : extractors){
            try {
                extractor.extractFirst();
            }
            catch (Exception e){
                throw new ScriptExtractionException(e);
            }
        }
    }

    @Override
    public Map<String, String> getProperties() {
        return scriptSuiteProcessor.getProperties();
    }

    public String getStatementType(){return type;}


    //@Transactional(value = "jdbcTx")
    private void select() throws SQLException{

        Connection connection = null;

        Statement statement = null;

        result = new ArrayList<Map<String, Object>>();

        try {

            org.qamock.app.main.domain.Connection conn_metadata = scriptService.getConnection(connectionName);

            if(conn_metadata == null){
                throw new SQLException("Connection data not found");
            }

            BasicDataSource dataSource =  jdbcPoolFactory.getJdbcDataSource(conn_metadata);

            jdbcTemplate.setDataSource(dataSource);

            result = jdbcTemplate.queryForList(Helper.replaceAlias(query, this));

            /*connection = jdbcPoolFactory.getJdbcDataSource(conn_metadata).getConnection();

            statement = connection.createStatement();

            ResultSet resultSet = statement.executeQuery(Helper.replaceAlias(query, this));

            logger.info("select statement success: " + Helper.replaceAlias(query, this));

            while (resultSet.next()){
                Map<String, String> row = new HashMap<String, String>();
                for(String s : params){
                    row.put(s, resultSet.getString(s));
                    logger.info("data received: " + row);
                }
                result.add(row);
            }

            resultSet.close();
            */
        }
        catch (SQLException e){
            logger.error(e.toString(), e);
        }
        finally {
            logger.info("success statement");
            //if(statement != null){statement.close();}
            //if(connection != null){/*JdbcConnectionPool.getInstance().closeConnection(connection);*/} //TODO connection pool
        }
    }

    private class JdbcSelectExtractor implements Serializable{

        private static final long serialVersionUID = 4563423462345435835L;

        public JdbcSelectExtractor(String param, String value, String to){
            this.target = param;
            this.value = value;
            this.to = to;
        }

        public void extractFirst(){
            if(target.equals("param")){
                getProperties().put(to, result.get(0).get(value).toString());
            }
        }

        private String target;
        private String value;
        private String to;

    }


}
