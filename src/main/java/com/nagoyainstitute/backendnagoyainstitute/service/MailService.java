package com.nagoyainstitute.backendnagoyainstitute.service;//package service;
//
//import exception.SpringCustomException;
//import lombok.AllArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import model.NotificationEmail;
//import org.springframework.mail.MailException;
//import org.springframework.mail.javamail.JavaMailSender;
//import org.springframework.mail.javamail.MimeMessageHelper;
//import org.springframework.mail.javamail.MimeMessagePreparator;
//import org.springframework.scheduling.annotation.Async;
//import org.springframework.stereotype.Service;
//
//@Service
//@AllArgsConstructor
//@Slf4j
//public class MailService {
//
//    private final MailContentBuilder mailContentBuilder;
//    private final JavaMailSender mailSender;
//
//    @Async
//    public void sendEmail(NotificationEmail notificationEmail){
//        MimeMessagePreparator messagePreparator = mimeMessage -> {
//            MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage);
//            messageHelper.setFrom("ichathux@gmail.com");
//            messageHelper.setTo(notificationEmail.getRecipient());
//            messageHelper.setSubject(notificationEmail.getSubject());
////            messageHelper.setText(mailContentBuilder.build(notificationEmail.getBody()));
//            messageHelper.setText(notificationEmail.getBody());
//        };
//        try {
//            mailSender.send(messagePreparator);
//            log.info("Activation email sent!!");
//        }catch (MailException e){
//            throw new SpringCustomException("Exception occured while sending activation email");
//        }
//    }
//}
