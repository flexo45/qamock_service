package main.java.mysqlconnector.qa.smppclient;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Properties;

public class PropertiesReader {

    private static int port = 41114;
    private static String server = "192.168.42.1";
    private static String system_id = "mts_lbs";
    private static String password = "mts123";

    public static void readProperties() throws Exception{
        Properties prop = new Properties();
        try {
            prop.load(new InputStreamReader(new FileInputStream("client.properties"),"UTF-8"));

            port = Integer.valueOf(prop.getProperty("port"));
            server = prop.getProperty("server");
            system_id = prop.getProperty("system_id");
            password = prop.getProperty("password");

        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public static int getPort(){
        return port;
    }

    public static String getServer(){
        return server;
    }

    public static String getSystem_id(){
        return system_id;
    }

    public static String getPassword(){
        return password;
    }

}
