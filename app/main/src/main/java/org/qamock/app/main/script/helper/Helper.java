package org.qamock.app.main.script.helper;

import org.qamock.app.main.script.model.ScriptStep;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Helper {

    private static Logger logger = LoggerFactory.getLogger(Helper.class);

    public static String replaceAlias(String text, ScriptStep step){

        Pattern pattern = Pattern.compile("\\$\\{(\\w*)\\}");

        Matcher matcher = pattern.matcher(text);

        while (matcher.find()){
            String prop = matcher.group(1);
            String value = step.getProperties().get(prop);
            text = text.replace("${" + prop + "}", value);
            logger.info("Find prop=${" + prop + "} to replace, value=" + value);
        }

        return text;
    }

}
