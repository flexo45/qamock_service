package org.qamock.service;

import org.qamock.xml.object.Email;

import javax.mail.MessagingException;
import java.io.UnsupportedEncodingException;

public interface EmailService {

    void sendMail(Email email);

}
