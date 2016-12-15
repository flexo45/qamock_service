package org.qamock.script.model;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.qamock.dynamic.domain.DynamicResourceRequest;
import org.qamock.script.exception.ScriptExecutionException;
import org.qamock.script.exception.ScriptExtractionException;
import org.qamock.script.handler.ScriptSuiteProcessor;
import org.qamock.script.helper.Helper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;

import javax.servlet.ServletInputStream;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HttpRequestScript implements ScriptStep, Serializable {

    private static final Logger logger = LoggerFactory.getLogger(HttpRequestScript.class);

    private static final long serialVersionUID = 7423567925694923655L;

    private ScriptHttpResponse response;
    private ScriptSuiteProcessor scriptSuiteProcessor;
    private String method;
    private String url;
    private String body;
    private Map<String, String> headers;
    private List<HttpExtractor> extractors;

    public HttpRequestScript(String method, String url){
        this.method = method;
        this.url = url;
        headers = new HashMap<String, String>();
        extractors = new ArrayList<HttpExtractor>();
    }

    public String getBody(){return body;}

    public Map<String, String> getHeaders(){return headers;}

    public void addExtractor(String target, String value, String to){
        extractors.add(new HttpExtractor(target, value , to));
    }

    public void setBody(String v){body = v;}

    public void setHeaders(Map<String, String> v){headers = v;}

    public void setScriptSuiteProcessor(ScriptSuiteProcessor v){scriptSuiteProcessor = v;}

    @Override
    public String getType() {
        return HttpRequestScript.class.getSimpleName();
    }

    @Override
    public void run() throws ScriptExecutionException {
        try {
            send();
        }
        catch (Exception e){
            throw new ScriptExecutionException(e);
        }
    }

    @Override
    public void extract() throws ScriptExtractionException {
        for(HttpExtractor extractor : extractors){
            try {
                extractor.extract();
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

    protected void send() throws Exception{

        HttpURLConnection connection = (HttpURLConnection) new URL(Helper.replaceAlias(url, this)).openConnection(); //TODO pool http connection

        if(headers != null){
            for(Map.Entry<String, String> it : headers.entrySet()){
                connection.setRequestProperty(it.getKey(), Helper.replaceAlias(it.getValue(), this));
            }
        }

        if(method.toLowerCase().equals("post")){
            connection.setRequestMethod("POST");

            if(body != null){
                connection.setDoOutput(true);
                DataOutputStream dos = new DataOutputStream(connection.getOutputStream());

                try {
                    dos.writeBytes(URLDecoder.decode(Helper.replaceAlias(body, this), "UTF-8"));
                }
                catch (IOException e){
                    logger.error("Error on write content", e);
                }
                finally {
                    dos.flush();
                    dos.close();
                }
            }
        }
        else {
            connection.setRequestMethod("GET");
        }

        response = new ScriptHttpResponse(connection.getResponseCode());

        logger.info("Request sanded to: " + Helper.replaceAlias(url, this));

        if(response.getCode() < 200 || response.getCode() > 299){
            throw new Exception("Response code was unsuccessful: " + response.getCode());
        }

        for(Map.Entry<String, List<String>> it : connection.getHeaderFields().entrySet()){
            String value = "";
            for(String v : it.getValue()){
                value += v + ";";
            }
            value = value.substring(0, value.length() - 1);
            String key = it.getKey();
            response.getHeaders().put(key == null ? "CODE" : key, value);
        }

        response.setContent(DynamicResourceRequest.getContent(connection.getInputStream()));

        logger.info("Response received: " + response.toString());
    }

    private class HttpExtractor implements Serializable {

        private static final long serialVersionUID = 4237546275435764335L;

        private final Logger logger = LoggerFactory.getLogger(HttpExtractor.class);

        public HttpExtractor(String target, String value, String to){
            this.target = target;
            this.value = value;
            this.to = to;
        }

        private String target;
        private String value;
        private String to;

        public void extract() throws Exception{
            String result = "";
            if(response != null){
                if(target.equals("header")){
                    try {
                        result = response.getHeaders().get(value);
                    }
                    catch (Exception e){
                        logger.error("Extract header=" + value + " error: ", e);
                    }
                }
                else if(target.equals("xml")){
                    try {
                        DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
                        DocumentBuilder builder = builderFactory.newDocumentBuilder();
                        Document document = builder.parse(response.getContent());
                        XPath xPath = XPathFactory.newInstance().newXPath();
                        result = xPath.compile(value).evaluate(document);
                    }
                    catch (XPathExpressionException xe){
                        logger.error("Extract xml error: XPath navigation error:\r\n" + value, xe);
                    }
                    catch (Exception e){
                        logger.error("Extract xml error:\r\n" + value, e);
                    }
                }
                else if(target.equals("json")){
                    ObjectMapper mapper = new ObjectMapper();
                    JsonNode root = mapper.readTree(response.getContent());
                    result = root.findValue(value).asText();
                }

                getProperties().put(to, result);
            }
        }
    }
}
