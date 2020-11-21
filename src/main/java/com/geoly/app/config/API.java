package com.geoly.app.config;

import com.geoly.app.dao.Response;
import com.geoly.app.models.StatusMessage;
import com.microsoft.azure.storage.blob.CloudBlobContainer;
import com.microsoft.azure.storage.blob.CloudBlockBlob;
import io.sentry.Sentry;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import javax.mail.internet.MimeMessage;
import javax.persistence.Query;
import java.util.List;

@Component
public class API {

    public static String questImageUrl = "quest/";
    public static String userImageUrl = "user/";
    public static String qrCodeImageUrl = "qrCode";

    @Value("${azure.images_link}")
    private String imagesLink;

    private JavaMailSenderImpl mailSender;
    private CloudBlobContainer cloudBlobContainer;

    public API(JavaMailSenderImpl mailSender, CloudBlobContainer cloudBlobContainer) {
        this.mailSender = mailSender;
        this.cloudBlobContainer = cloudBlobContainer;
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
        try{
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setTo(email);
            helper.setSubject(subject);
            helper.setFrom("noreply@geoly.com");
            helper.setText(text, true);
            mailSender.send(message);
        }catch (Exception e){
            Sentry.capture(e);
            e.printStackTrace();
        }
    }

    public String uploadImage(String url, byte[] file){
        CloudBlockBlob image = null;
        try{
            image = cloudBlobContainer.getBlockBlobReference(url);
            image.uploadFromByteArray(file, 0, file.length);
            return image.getUri().toString().replace(imagesLink, "");
        }catch (Exception e){
            Sentry.capture(e);
            e.printStackTrace();
            return null;
        }
    }
}
