package com.school.configuration;

import com.school.service.EnvironmentService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.Base64;
import java.util.Properties;

@Configuration
public class FeedbackMailConfig {
    private final EnvironmentService environmentService;
    @Value("${spring.email.password}")
    private String emailPassword;
    @Value("${support-email}")
    private String supportEmail;

    public FeedbackMailConfig(EnvironmentService environmentService) {
        this.environmentService = environmentService;
    }

    @Bean
    public JavaMailSender javaMailSender() {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();

        mailSender.setHost("smtp.gmail.com");
        mailSender.setPort(587);
        mailSender.setUsername(supportEmail);
        mailSender.setPassword(new String(Base64.getDecoder().decode(emailPassword)));

        Properties props = mailSender.getJavaMailProperties();
        props.put("mail.transport.protocol", "smtp");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.starttls.required", "true");
        if (environmentService.currentProfileIsDevel()) {
            props.put("mail.debug", "true");
        }

        return mailSender;
    }
}
