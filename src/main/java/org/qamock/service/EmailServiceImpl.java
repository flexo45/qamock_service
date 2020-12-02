package org.qamock.service;

import org.apache.commons.lang3.StringEscapeUtils;
import org.qamock.xml.object.Email;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Array;
import java.util.Objects;

@Service
public class EmailServiceImpl implements EmailService {

    private static Logger logger = LoggerFactory.getLogger(EmailServiceImpl.class);

    @Autowired
    MailSender mailSender;

    @Override
    public void sendMail(Email email){
        SimpleMailMessage mailMessage = new SimpleMailMessage();

        mailMessage.setTo(email.getTo());
        mailMessage.setFrom(email.getFrom() == null ? "no_reply@temamedia.ru" : email.getFrom().getFromAddress());

        if(isUnicode(email.getSubject())){
            mailMessage.setSubject(convertUnicodeToUtf(email.getSubject()));
        }
        else {
            mailMessage.setSubject(email.getSubject());
        }

        if(isUnicode(email.getMessage())){
            mailMessage.setText(convertUnicodeToUtf(email.getMessage()));
        }
        else {
            mailMessage.setText(email.getMessage());
        }

        mailSender.send(mailMessage);
    }

    private boolean isUnicode(String text){
        return text.contains("\\u");
    }

    private String convertUnicodeToUtf(String unicodeString){

        String result = "";

        unicodeString = unicodeString.replace("\\", "");
        String[] arr = unicodeString.split("u");
        for(int i = 0; i < arr.length; i++){
            if(!arr[i].equals("")){
                try {
                    result += (char) (Integer.parseInt(arr[i]));

                }
                catch (RuntimeException re){
                    result += arr[i];
                }
            }
        }

        return result;
    }

}
