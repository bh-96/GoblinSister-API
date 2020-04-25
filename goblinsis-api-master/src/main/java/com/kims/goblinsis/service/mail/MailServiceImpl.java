package com.kims.goblinsis.service.mail;

import com.kims.goblinsis.service.CommonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Date;
import java.util.Properties;

@Service
public class MailServiceImpl extends CommonService implements MailService {

    @Autowired
    private JavaMailSender javaMailSender;

    @Value("${spring.mail.host}")
    private String host;

    @Value("${spring.mail.port}")
    private String port;

    @Value("${spring.mail.username}")
    private String sender;

    @Value("${spring.mail.password}")
    private String password;

    @Value("${spring.mail.properties.mail.smtp.auth}")
    private String auth;

    @Value("${spring.mail.properties.mail.smtp.starttls.enable}")
    private String starttls;


    @Override
    public boolean sendMail(String receiver, String subject, String content) {
        try {
            Properties props = new Properties();

            // smtp에 필요한 인증부
            props.put("mail.smtp.starttls.enable", starttls);
            props.put("mail.smtp.ssl.trust", host);
            props.put("mail.smtp.auth", auth);

            // 호스트 / 포트
            props.put("mail.smtp.host", host);
            if (port != null) {
                props.put("mail.smtp.port", port);
                props.put("mail.smtp.socketFactory.port", port);
                props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
            }

            // 인증을 포함한 메시지 만들기
            MimeMessage message = new MimeMessage(Session.getInstance(props, new Authenticator() {
                public PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(sender, password);
                }
            }));

            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setFrom(new InternetAddress(sender, "도깨비언니"));
            helper.setTo(new InternetAddress(receiver));
            helper.setSubject(subject);
            helper.setText(content, true);
            helper.setSentDate(new Date());

            javaMailSender.send(message);
            return true;
        } catch (Exception e) {
            logger.error("MailService -> sendMail : " + e.getMessage());
        }

        return false;
    }
}
