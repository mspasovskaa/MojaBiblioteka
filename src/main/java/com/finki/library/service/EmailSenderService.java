package com.finki.library.service;



public interface EmailSenderService {
    public String sendMail(String toEmail,String subject,String body);

}
