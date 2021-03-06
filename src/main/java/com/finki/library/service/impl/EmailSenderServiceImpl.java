package com.finki.library.service.impl;

import com.finki.library.service.EmailSenderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailSenderServiceImpl implements EmailSenderService {
    @Autowired
    private JavaMailSender mailSender;

    @Override
    public String sendMail(String toEmail,
                           String subject,
                           String body)
    {
        SimpleMailMessage message=new SimpleMailMessage();
        message.setFrom("marijaspasovska545@gmail.com");
        message.setTo(toEmail);
        message.setText(body);
        message.setSubject(subject);
        mailSender.send(message);
        return String.valueOf(message.getTo());
    }
}
