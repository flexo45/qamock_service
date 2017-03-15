package main.java.mysqlconnector.qa.smppclient;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class Application {

    public static void main(String[] args) throws Exception{

        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        int port = 8181;

        if(args.length > 0){
            if(args[0].equals("smsc")){
                //start smsc
                if(args.length > 1){

                    try {
                        port = Integer.parseInt(args[1]);
                    }
                    catch (RuntimeException e){
                        //error
                    }

                }

                ComplexSmscStub smsc = new ComplexSmscStub(port);

                smsc.start();

                System.out.println("press any key for stop SMSC...");

                br.readLine();

                smsc.stop();

            }
            else if(args[0].equals("client")){
                //start client
                PropertiesReader.readProperties();
                Thread thread = new Thread(new EsmeStub());
                thread.start();
            }
        }
    }

}
