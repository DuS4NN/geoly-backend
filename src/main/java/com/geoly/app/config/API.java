package com.geoly.app.config;

import com.geoly.app.dao.Response;
import com.geoly.app.models.StatusMessage;
import com.google.common.hash.Hashing;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageConfig;
import com.google.zxing.common.BitArray;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import com.microsoft.azure.storage.blob.CloudBlobContainer;
import com.microsoft.azure.storage.blob.CloudBlockBlob;
import io.sentry.Sentry;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import javax.imageio.ImageIO;
import javax.mail.internet.MimeMessage;
import javax.persistence.Query;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

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

    public byte[] generateQrCode() throws WriterException, IOException {
        Map<EncodeHintType, ErrorCorrectionLevel> hashMap = new HashMap<>();
        hashMap.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.L);

        Random rand = new Random();
        BitMatrix matrix = new MultiFormatWriter().encode(new String(Hashing.sha256().hashString(""+rand.nextInt(999999), Charset.defaultCharset()).asBytes(), "UTF-8"), BarcodeFormat.QR_CODE, 500, 500);

        int height = matrix.getHeight();
        int width = matrix.getWidth();

        MatrixToImageConfig config = new MatrixToImageConfig();
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        int onColor = config.getPixelOnColor();
        int offColor = config.getPixelOffColor();

        int[] rowPixels = new int[width];
        BitArray row = new BitArray(width);
        for (int y = 0; y < height; y++) {
            row = matrix.getRow(y, row);
            for (int x = 0; x < width; x++) {
                rowPixels[x] = row.get(x) ? onColor : offColor;
            }
            image.setRGB(0, y, width, 1, rowPixels, 0, width);
        }


        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(image, "jpg", baos);
        return baos.toByteArray();
    }
}
