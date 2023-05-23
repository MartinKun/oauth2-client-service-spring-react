package com.example.oauth2clientservice.email.config;

import com.example.oauth2clientservice.email.model.EmailSender;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class EmailConfig {

    @Value("${email.username}")
    private String USERNAME;
    @Value("${email.password}")
    private String PASSWORD;
    @Value("${email.host}")
    private String HOST;

    @Bean
    public EmailSender emailSenderSettings() {
        return EmailSender.builder()
                .username(USERNAME)
                .password(PASSWORD)
                .host(HOST)
                .build();
    }

}
