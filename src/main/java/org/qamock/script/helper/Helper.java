package org.qamock.script.helper;

import org.qamock.script.model.ScriptStep;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Helper {

    public static String replaceAlias(String text, ScriptStep step){

        Pattern pattern = Pattern.compile("\\$\\{(\\w*)\\}");

        Matcher matcher = pattern.matcher(text);

        while (matcher.find()){
            String prop = matcher.group(1);
            String value = step.getProperties().get(prop);
            text = text.replace("${" + prop + "}", value);
        }

        return text;
    }

}
