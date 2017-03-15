package main.java.mysqlconnector.qa.smppclient;

import main.java.mysqlconnector.qa.smppclient.pdu.CommandId;

public class CommandParser {

    public static String parseStatus(long statusId){
        return "";
    }

    public static String parse(long commandId){

        if(commandId == CommandId.ALERT_NOTIFICATION){
            return "ALERT_NOTIFICATION";
        }
        else if(commandId == CommandId.BIND_RECEIVER){
            return "BIND_RECEIVER";
        }
        else if(commandId == CommandId.BIND_RECEIVER_RESP){
            return "BIND_RECEIVER_RESP";
        }
        else if(commandId == CommandId.BIND_TRANSCEIVER){
            return "BIND_TRANSCEIVER";
        }
        else if(commandId == CommandId.BIND_TRANSCEIVER_RESP){
            return "BIND_TRANSCEIVER_RESP";
        }
        else if(commandId == CommandId.BIND_TRANSMITTER){
            return "BIND_TRANSMITTER";
        }
        else if(commandId == CommandId.BIND_TRANSMITTER_RESP){
            return "BIND_TRANSMITTER_RESP";
        }
        else if(commandId == CommandId.DELIVER_SM){
            return "DELIVER_SM";
        }
        else if(commandId == CommandId.DELIVER_SM_RESP){
            return "DELIVER_SM_RESP";
        }
        else if(commandId == CommandId.SUBMIT_SM){
            return "SUBMIT_SM";
        }
        else if(commandId == CommandId.SUBMIT_SM_RESP){
            return "SUBMIT_SM_RESP";
        }
        else if(commandId == CommandId.ENQUIRE_LINK){
            return "ENQUIRE_LINK";
        }
        else if(commandId == CommandId.ENQUIRE_LINK_RESP){
            return "ENQUIRE_LINK_RESP";
        }
        else if(commandId == CommandId.UNBIND){
            return "UNBIND";
        }
        else if(commandId == CommandId.UNBIND_RESP){
            return "UNBIND_RESP";
        }
        else {
            return "UNKNOWN";
        }
    }

}
