package org.qamock.app.main.xml;

import java.io.IOException;

public interface XmlProcessor {

    String objectToString(Object graph) throws IOException;

    Object stringToObject(String string) throws IOException;

    void objectToXML(String fileName, Object graph) throws IOException;

    Object xmlToObject(String fileName) throws IOException;

}
