package org.qamock.script.model;

import org.qamock.script.exception.ScriptExecutionException;
import org.qamock.script.exception.ScriptExtractionException;
import org.qamock.script.helper.Helper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JdbcRequestScript implements ScriptStep, Serializable {

    private static final Logger logger = LoggerFactory.getLogger(JdbcRequestScript.class);

    private static final long serialVersionUID = 4576495647435352835L;

    public JdbcRequestScript(int type, String connectionString){
        this.type = type;
        this.connectionString = connectionString;
        extractors = new ArrayList<JdbcSelectExtractor>();
    }

    public void addExtractor(String param, String to){
        extractors.add(new JdbcSelectExtractor(param, to));
    }

    public void setScriptSuite(ScriptSuite v){scriptSuite = v;}

    public void addSelectStatement(String query, String[] params){
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
            switch (type){
                case 0:
                    select();
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
        return scriptSuite.getProperties();
    }

    public int getStatementType(){return type;}



    private void select() throws SQLException{

        Connection connection = null;

        Statement statement = null;

        result = new ArrayList<Map<String, String>>();

        try {
            //connection = JdbcConnectionPool.getInstance().getConnection(connectionString);

            statement = connection.createStatement();

            ResultSet resultSet = statement.executeQuery(Helper.replaceAlias(query, this));

            logger.info("select statement success: " + Helper.replaceAlias(query, this));

            while (resultSet.next()){
                Map<String, String> row = new HashMap<String, String>();
                for(int i = 0; i < params.length; i++){
                    row.put(params[i], resultSet.getString(params[i]));
                    logger.info("data received: " + row);
                }
                result.add(row);
            }

            resultSet.close();
        }
        catch (SQLException e){
            logger.error(e.toString(), e);
        }
        finally {
            if(statement != null){statement.close();}
            if(connection != null){/*JdbcConnectionPool.getInstance().closeConnection(connection);*/} //TODO connection pool
        }
    }



    private ScriptSuite scriptSuite;

    private String query;

    private String[] params;

    private String connectionString;

    private int type; //**0=select*/

    private List<JdbcSelectExtractor> extractors;

    private List<Map<String, String>> result;

    private class JdbcSelectExtractor implements Serializable{

        private static final long serialVersionUID = 4563423462345435835L;

        public JdbcSelectExtractor(String param, String to){
            this.param = param;
            this.to = to;
        }

        public void extractFirst(){
            getProperties().put(to, result.get(0).get(param));
        }



        private String param;

        private String to;

    }


}
