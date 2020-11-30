package org.qamock.script.model;

import org.qamock.script.exception.ScriptExecutionException;
import org.qamock.script.exception.ScriptExtractionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Deprecated
public class ScriptSuite implements Runnable, Serializable {

    private static final Logger logger = LoggerFactory.getLogger(ScriptSuite.class);

    private static final long serialVersionUID = 1654378563754352835L;

    public ScriptSuite(String name){
        this.name = name;
        properties = new HashMap<String, String>();
        sequences = new HashMap<String, Long>();
        connectionMap = new HashMap<Integer, String>();
        stepList = new ArrayList<ScriptStep>();

    }

    public List<ScriptStep> getStepList(){return stepList;}

    public Map<String, String> getProperties(){return properties;}

    public Map<String, Long> getSequences(){return sequences;}

    public Long sequenceGetNext(String name){
        long v = sequences.get(name) + 1;
        sequences.put(name, v);
        return v;
    }

    public Map<Integer, String> getConnectionMap(){return connectionMap;}

    public String getName(){return name;}

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

    public ScriptSuite copy() throws Exception{
        if(original == null){

            ByteArrayOutputStream baos = null;

            ObjectOutputStream ous = null;

            try {
                baos = new ByteArrayOutputStream();
                ous = new ObjectOutputStream(baos);
                ous.writeObject(this);
                ous.close();
                original = baos.toByteArray();
                baos.close();
            }
            catch (Exception e){
                if(baos != null){
                    try {
                        baos.close();
                    }
                    catch (Exception ex){
                        //supressed
                    }
                }
                if(ous != null) {
                    try {
                        ous.close();
                    }
                    catch (Exception ex){
                        //supressed
                    }
                }
                throw e;
            }
        }

        ByteArrayInputStream bais = null;

        ObjectInputStream ois = null;

        try {
            bais = new ByteArrayInputStream(original);
            ois = new ObjectInputStream(bais);
            return (ScriptSuite) ois.readObject();
        }
        catch (Exception e){
            if(bais != null){
                try {
                    bais.close();
                }
                catch (Exception ex){
                    //supressed
                }
            }
            if(ois != null){
                try {
                    ois.close();
                }
                catch (Exception ex){
                    //supressed
                }
            }
            throw e;
        }
    }


    private Map<Integer, String> connectionMap;

    private String name;

    private List<ScriptStep> stepList;

    private Map<String, String> properties;

    private Map<String, Long> sequences;

    private byte[] original;

}
