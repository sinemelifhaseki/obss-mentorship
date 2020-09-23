package com.obsssummerintern.mentorship.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

@Component
public class MailService {
    private JavaMailSender javaMailSender;

    @Autowired
    public MailService(JavaMailSender javaMailSender){
        this.javaMailSender = javaMailSender;
    }

    public void sendReminderMail(String receiver, String topic, String phaseName, String personName, String subtopic){
        System.out.println("girdim");
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(receiver);
        mailMessage.setSubject("Faz Bitişi Hatırlatma");
        mailMessage.setText(personName+ " ile ilerlettiğiniz " + topic + ": "+ subtopic+ " konusundaki mentorluğunuzun "+phaseName+" fazının bitmesine 1 saat kaldı.\n\n\n\n OBSS MENTORSHIP\n Öğrenirken öğretmeye devam edin.");
        javaMailSender.send(mailMessage);
    }

    public void sendFinishingMail(String receiver, String topic, String phaseName, String personName, String subtopic){
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(receiver);
        mailMessage.setSubject("Mentorluğu Tamamladınız");
        mailMessage.setText(personName+ " ile ilerlettiğiniz " + topic + ": "+ subtopic+ " konusundaki mentorluğunuzu tamamladınız. Sırada hangi konu var? Hemen öğrenin.\n\n\n\n OBSS MENTORSHIP\n Öğrenirken öğretmeye devam edin.");
        javaMailSender.send(mailMessage);
    }
}
