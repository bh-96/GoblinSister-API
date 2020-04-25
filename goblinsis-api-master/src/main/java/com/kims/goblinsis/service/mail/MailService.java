package com.kims.goblinsis.service.mail;

public interface MailService {

    boolean sendMail(String receiver, String subject, String content);
}
