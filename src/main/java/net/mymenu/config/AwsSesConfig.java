package net.mymenu.config;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.simpleemail.AmazonSimpleEmailService;
import com.amazonaws.services.simpleemail.AmazonSimpleEmailServiceClientBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AwsSesConfig {

    @Value("${aws.ses.region}")
    private String region;

    @Value("${aws.ses.access-key}")
    private String accessKey;

    @Value("${aws.ses.secret-key}")
    private String secretKey;

    @Bean
    public AmazonSimpleEmailService amazonSimpleEmailService() {
        BasicAWSCredentials basicAWSCredentials = new BasicAWSCredentials(accessKey, secretKey);
        return AmazonSimpleEmailServiceClientBuilder
                .standard()
                .withRegion(region)
                .withCredentials(new AWSStaticCredentialsProvider(basicAWSCredentials))
                .build();
    }
}
