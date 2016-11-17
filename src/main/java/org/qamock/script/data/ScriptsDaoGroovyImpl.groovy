package org.qamock.script.data

import org.qamock.config.XmlFileScriptsDataSource
import org.qamock.script.model.HttpRequestScript
import org.qamock.script.model.JdbcRequestScript
import org.qamock.script.model.ScriptSuite
import org.qamock.script.model.SleepStep

class ScriptsDaoGroovyImpl {

    private static String location = "resources" + File.separator + "scripts";

    public static void setScriptLocation(String location) {
        this.location = location;
    }

    public static ScriptSuite readSuite(String name) throws Exception{

        Node suite;

        ScriptSuite scriptSuite = null;

        File file = new File(this.location)

        suite = new XmlParser().parse(file)

        scriptSuite = new ScriptSuite(suite.attribute("name") as String)

        Node conf = suite.find{ Node n -> n.name().equals("conf") } as Node

        if(conf != null){
            conf.each {
                Node n ->
                    if(n.name().equals("connection")){
                        scriptSuite
                                .getConnectionMap()
                                .put(Integer.parseInt(n.attribute("id") as String),
                                (n.attribute("value") as String).replace("PASS_VALUE", n.attribute("pass") as String))
                    }
            }
        }

        Node properties = suite.find{ Node n -> n.name().equals("properties") } as Node
        if(properties != null){
            properties.each {
                Node n ->
                    scriptSuite.getProperties().put(n.attribute("key") as String, n.attribute("value") as String)
            }
        }

        Node steps = suite.find{ Node n -> n.name().equals("steps") } as Node

        if(steps != null){
            steps.each {
                Node n ->
                    if((n.attribute("type") as String).equals("http")){
                        scriptSuite.stepList.add(Integer.parseInt(n.attribute("id") as String), createHttpScript(n, scriptSuite))
                    }
                    else if((n.attribute("type") as String).equals("jdbc")){
                        scriptSuite.stepList.add(Integer.parseInt(n.attribute("id") as String), createJdbcScript(n, scriptSuite))
                    }
                    else if((n.attribute("type") as String).equals("sleep")){
                        scriptSuite.stepList.add(Integer.parseInt(n.attribute("id") as String), createSleepStep(n, scriptSuite))
                    }
            }
        }

        return scriptSuite;
    }

    public static String[] listScripts() throws Exception {
        return new File(location).list(new FilenameFilter() {
            @Override
            boolean accept(File dir, String name) {
                return dir.isDirectory() && name.contains(".xml");
            }
        })
    }

    private static HttpRequestScript createHttpScript(Node step, ScriptSuite scriptSuite){

        Node conf = step.find {Node n -> n.name().equals("conf")} as Node

        HttpRequestScript script =
                new HttpRequestScript(conf.attribute("method") as String, (conf.attribute("url") as String).replace("%AND%", "&"))

        script.setScriptSuite(scriptSuite)

        conf.each {
            Node h ->
                if(h.name().equals("header")){
                    script.headers.put(h.attribute("key") as String, h.attribute("value") as String)
                }
                else if(h.name().equals("content")){
                    script.body = (h.value() as String).trim().replace("\r", "").replace("\n", "").replace("[", "").replace("]", "")
                }
        }

        Node extract = step.find {Node n -> n.name().equals("extract")} as Node

        if(extract != null){
            extract.each {
                Node t ->
                    if(t.name().equals("target")){
                        script.addExtractor(t.attribute("name") as String,
                                t.attribute("value") as String,
                                t.attribute("to") as String)
                    }
            }
        }

        return script
    }

    private static JdbcRequestScript createJdbcScript(Node step, ScriptSuite scriptSuite){
        Node conf = step.find {Node n -> n.name().equals("conf")} as Node

        String connection = scriptSuite.connectionMap.get(Integer.parseInt(conf.attribute("connection") as String))

        JdbcRequestScript script =
                new JdbcRequestScript(conf.attribute("type") as String, connection)

        script.setScriptSuite(scriptSuite)

        String query = ""
        String[] params = null
        conf.each {
            Node n ->
                if(n.name().equals("query")){
                    query = (n.value() as String).trim().replace("\r", "").replace("\n", "").replace("[", "").replace("]", "")
                }
                else if(n.name().equals("params")){
                    params = new String[n.children().size()]
                    for(def i = 0; i < params.length; i++){
                        params[i] = (n.children().get(i) as Node).attribute("key") as String
                    }
                }
        }

        if(script.getStatementType().equals("select")){
            script.addSelectStatement(query, params)
        }

        Node extract = step.find {Node n -> n.name().equals("extract")} as Node

        if(extract != null){
            extract.each {
                Node t ->
                    if(t.name().equals("target")){
                        script.addExtractor(t.attribute("name") as String, t.attribute("to") as String)
                    }
            }
        }

        return script
    }

    private static SleepStep createSleepStep(Node step, ScriptSuite scriptSuite){
        SleepStep sleepStep = new SleepStep(Long.parseLong(step.attribute("value") as String))
        sleepStep.setScriptSuite(scriptSuite)
        return sleepStep
    }

}
