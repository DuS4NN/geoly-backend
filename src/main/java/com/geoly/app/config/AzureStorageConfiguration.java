package com.geoly.app.config;

import com.microsoft.azure.storage.CloudStorageAccount;
import com.microsoft.azure.storage.StorageException;
import com.microsoft.azure.storage.blob.CloudBlobClient;
import com.microsoft.azure.storage.blob.CloudBlobContainer;

import com.microsoft.azure.storage.file.CloudFileClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import java.net.URISyntaxException;
import java.security.InvalidKeyException;

@Configuration
public class AzureStorageConfiguration {

    @Value("${azure.url}")
    private String connectionString;
    @Value("${azure.container}")
    private String containerName;

    private CloudStorageAccount cloudStorageAccount;

    @PostConstruct
    public void postConstruct()  throws URISyntaxException, InvalidKeyException{
         this.cloudStorageAccount = CloudStorageAccount.parse(connectionString);
    }

    @Bean
    public CloudFileClient cloudFileClient(){
        return this.cloudStorageAccount.createCloudFileClient();
    }

    @Bean
    public CloudBlobClient cloudBlobClient(){
        return this.cloudStorageAccount.createCloudBlobClient();
    }

    @Bean
    public CloudBlobContainer cloudBlobContainer() throws URISyntaxException, StorageException {
        return cloudBlobClient().getContainerReference(containerName);
    }
}
