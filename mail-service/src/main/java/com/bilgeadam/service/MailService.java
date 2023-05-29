package com.bilgeadam.service;

import com.bilgeadam.dto.response.ForgotPasswordMailResponseDto;
import com.bilgeadam.rabbitmq.model.RegisterMailModel;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class MailService {
    private final JavaMailSender javaMailSender;
    public MailService(JavaMailSender javaMailSender){
        this.javaMailSender = javaMailSender;
    }

    public void sendActivationCode(RegisterMailModel model) {
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setSubject("Aktivasyon Kodu");
        mailMessage.setFrom("${spring.mail.username}");
        mailMessage.setTo(model.getEmail());
        mailMessage.setText(model.getUsername() + "\nAktivasyon Kodunuz: " + model.getActivationCode());
        javaMailSender.send(mailMessage);
    }

    public Boolean sendMailForgotPassword(ForgotPasswordMailResponseDto dto) {
        try{
            SimpleMailMessage mailMessage = new SimpleMailMessage();
            mailMessage.setSubject("Forgot Password");
            mailMessage.setFrom("${spring.mail.username}");
            mailMessage.setTo(dto.getEmail());
            mailMessage.setText("Yeni şifreniz: " + dto.getPassword() +
                    "\nGiriş yaptıktan sonra güveniliğiniz nedeniyle şifrenizi değiştiriniz." );
            javaMailSender.send(mailMessage);
        }catch (Exception e){
            e.getMessage();
        }
        return true;
    }
}
