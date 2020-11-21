package com.geoly.app.controler;

import com.microsoft.azure.storage.blob.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URL;

@RestController
public class ImageController {

    private CloudBlobContainer cloudBlobContainer;
    private CloudBlobClient cloudBlobClient;
    @Value("${azure.sas}")
    private String sasToken;
    @Value("${azure.images_link}")
    private String imagesLink;

    public ImageController(CloudBlobContainer cloudBlobContainer, CloudBlobClient cloudBlobClient) {
        this.cloudBlobContainer = cloudBlobContainer;
        this.cloudBlobClient = cloudBlobClient;
    }

    @GetMapping(value = "/image", produces = MediaType.IMAGE_JPEG_VALUE)
    public @ResponseBody
    byte[] getImage(@RequestParam(name = "url") String url){
        try{
            URL image = new URL(imagesLink+url+sasToken);
            BufferedImage bufferedImage = ImageIO.read(image);
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            ImageIO.write(bufferedImage, "jpg", bos);
            return bos.toByteArray();
        }catch (Exception e){
            return new byte[0];
        }
    }
}
