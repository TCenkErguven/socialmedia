package com.bilgeadam.controller;

import com.bilgeadam.dto.response.ForgotPasswordMailResponseDto;
import com.bilgeadam.service.MailService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.bilgeadam.constant.ApiUrls.*;

@RestController
@RequestMapping("/api/v1/mail")
@RequiredArgsConstructor
public class MailController {
    private final MailService mailService;

    @PostMapping(FORGOT_PASSWORD)
    public ResponseEntity<Boolean> sendMailForgotPassword(@RequestBody ForgotPasswordMailResponseDto dto){
        return ResponseEntity.ok(mailService.sendMailForgotPassword(dto));
    }
}
