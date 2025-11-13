package com.satoriviacafe.framework.web.service;

import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.concurrent.ExecutorService;

/**
 * @author shy
 * @since 2025年06月01日
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class MailService {

    private final JavaMailSender mailSender;

    // 虚拟线程池
    @Qualifier("threadPoolTaskExecutor")
    private final ExecutorService mailExecutor;

    public void sendMailAsync(MimeMessage message) {
        mailExecutor.submit(() -> {
            try {
                mailSender.send(message);
            } catch (Exception e){
                log.error(e.getMessage());
            }
        });
    }

    public MimeMessage createMimeMessage() {
        return mailSender.createMimeMessage();
    }
}
