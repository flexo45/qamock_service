package org.qamock.script.handler;

import org.qamock.domain.ScriptSuite;
import org.qamock.dynamic.context.TestContextService;
import org.qamock.script.exception.ScriptExecutionException;
import org.qamock.script.exception.ScriptExtractionException;
import org.qamock.script.exception.ScriptInitializationException;
import org.qamock.script.model.HttpRequestScript;
import org.qamock.script.model.JdbcRequestScript;
import org.qamock.script.model.ScriptStep;
import org.qamock.script.model.SleepStep;
import org.qamock.xml.XmlProcessor;
import org.qamock.xml.object.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ScriptSuiteProcessorImpl implements ScriptSuiteProcessor {

    private static final Logger logger = LoggerFactory.getLogger(ScriptSuiteProcessorImpl.class);

    private XmlProcessor xmlProcessor;
    private TestContextService testContextService;

    //private Map<Integer, String> connectionMap;
    private String name;
    private List<ScriptStep> stepList;
    private Map<String, String> properties;
    //private Map<String, Long> sequences;

    public ScriptSuiteProcessorImpl(){
        this.stepList = new ArrayList<ScriptStep>();
        this.properties = new HashMap<String, String>();
    }

    public void setXmlProcessor(XmlProcessor xmlProcessor){
        this.xmlProcessor = xmlProcessor;
    }

    @Override
    public void initiateScriptSuite(ScriptSuite scriptSuite, Map<String, String> params) throws IOException, ScriptInitializationException{
        this.properties = new HashMap<String, String>(params);
        initiateScriptSuite(scriptSuite);
    }

    @Override
    public void initiateScriptSuite(ScriptSuite scriptSuite) throws IOException, ScriptInitializationException{

        this.stepList.clear(); //TODO KASTIL!!!

        this.name = scriptSuite.getName();

        Suite suite = (Suite) xmlProcessor.stringToObject(scriptSuite.getText());

        for(Property property : suite.getProperties()){
            if(!properties.containsKey(property.getKey())){
                properties.put(property.getKey(), property.getValue());
            }
        }

        for(Step step : suite.getSteps()){
            if(step.getType().equals("http")){
                HttpRequestScript httpRequestScript =
                        new HttpRequestScript(step.getMethod()
                                , step.getPath() + (step.getQuery() == null ? "" : "?" + step.getQuery()));

                httpRequestScript.setBody(step.getContent());

                Map<String, String> headers = new HashMap<String, String>();
                for(Header headerJaxb : step.getHeaders()){
                    headers.put(headerJaxb.getKey(), headerJaxb.getValue());
                }

                httpRequestScript.setHeaders(headers);

                if(step.getExtractors() != null){
                    for(Extractor extractorJaxb : step.getExtractors()){
                        httpRequestScript.addExtractor(extractorJaxb.getTargetName(), extractorJaxb.getValue(), extractorJaxb.getTo());
                    }
                }

                httpRequestScript.setScriptSuiteProcessor(this);

                stepList.add(httpRequestScript);
            }
            else if(step.getType().equals("jdbc")){
                JdbcRequestScript jdbcRequestScript =
                        new JdbcRequestScript(step.getType(), step.getConnection());

                jdbcRequestScript.addSelectStatement(step.getQuery(), step.getParams());

                for(Extractor extractorJaxb : step.getExtractors()){
                    jdbcRequestScript.addExtractor(extractorJaxb.getTargetName(), extractorJaxb.getValue(), extractorJaxb.getTo());
                }

                jdbcRequestScript.setScriptSuiteProcessor(this);

                stepList.add(jdbcRequestScript);
            }
            else if(step.getType().equals("sleep")){
                SleepStep sleepStep = new SleepStep(step.getValue());
                sleepStep.setScriptSuiteProcessor(this);
                stepList.add(sleepStep);
            }
            else {
                throw new ScriptInitializationException("Unexpected step type: " + step.getType());
            }
        }

    }

    @Override
    public Map<String, String> getProperties() {
        return properties;
    }

    @Override
    public void run() {

        for(ScriptStep step : stepList){
            try {
                step.run();
                step.extract();
            }
            catch (ScriptExecutionException see){
                logger.error("Script=" + name + " runtime error occurred: " + see, see);
            }
            catch (ScriptExtractionException se){
                logger.error("Script=" + name + " extraction error occurred: " + se, se);
            }
        }

    }
}
