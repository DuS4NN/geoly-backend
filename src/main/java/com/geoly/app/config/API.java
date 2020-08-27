package com.geoly.app.config;

import com.geoly.app.dao.Response;
import com.geoly.app.models.StatusMessage;
import io.sentry.Sentry;
import org.springframework.http.HttpStatus;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.scheduling.annotation.Async;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;


import javax.persistence.Query;
import java.util.List;

@Component
public class API {

    private JavaMailSender mailSender;
    public static String questImageUrl = "static/images/quest/";
    public static String userImageUrl = "static/images/user/";

    public API(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public static void setBindParameterValues(Query hibernateQuery, org.jooq.Query jooqQuery){
        List<Object> values = jooqQuery.getBindValues();
        for(int i = 0; i < values.size(); i++){
            hibernateQuery.setParameter(i + 1, values.get(i));
        }
    }

    public static Response catchException(Exception e){
        Sentry.capture(e);
        e.printStackTrace();
        return new Response(StatusMessage.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR, null);
    }

    @Async
    public void sendEmail(String text, String email, String subject){
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setFrom("noreply@geoly.com");
        mailMessage.setTo(email);
        mailMessage.setSubject(subject);
        mailMessage.setText(text);
        mailSender.send(mailMessage);
    }
}
