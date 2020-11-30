package org.qamock.xml;

import org.springframework.oxm.Marshaller;
import org.springframework.oxm.Unmarshaller;

import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import java.io.*;

public class XmlProcessorImpl implements XmlProcessor {

    private Marshaller marshaller;
    private Unmarshaller unmarshaller;

    public void setMarshaller(Marshaller marshaller) {
        this.marshaller = marshaller;
    }

    public void setUnmarshaller(Unmarshaller unmarshaller) {
        this.unmarshaller = unmarshaller;
    }

    /**
     * Convert object to String
     */
    @Override
    public String objectToString(Object graph) throws IOException{

        StringWriter stringWriter = new StringWriter();

        try {
            marshaller.marshal(graph, new StreamResult(stringWriter));
            return stringWriter.toString();
        }
        finally {
            stringWriter.close();
        }

    }

    /**
     * Convert String to object
     */
    @Override
    public Object stringToObject(String string) throws IOException{
        return unmarshaller.unmarshal(new StreamSource(new StringReader(string)));
    }

    /**
     * Converts Object to XML file
     */
    @Override
    public void objectToXML(String fileName, Object graph) throws IOException{

        FileOutputStream fos = null;

        try {
            fos = new FileOutputStream(fileName);
            marshaller.marshal(graph, new StreamResult(fos));
        }
        finally {
            fos.close();
        }
    }

    /**
     * Converts XML to Java Object
     */
    @Override
    public Object xmlToObject(String fileName) throws IOException {
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(fileName);
            return unmarshaller.unmarshal(new StreamSource(fis));
        } finally {
            fis.close();
        }
    }

}
