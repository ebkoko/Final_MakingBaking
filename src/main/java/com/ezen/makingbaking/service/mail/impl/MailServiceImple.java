package com.ezen.makingbaking.service.mail.impl;

import java.util.Map;

import org.apache.groovy.util.Maps;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import com.ezen.makingbaking.configuration.handler.MailHandler;
import com.ezen.makingbaking.service.mail.MailService;

@Service
public class MailServiceImple  implements MailService{
   @Autowired
   private JavaMailSender sender;

   public Map<String, Object> send(String email, String title, String body) {

      MailHandler mail;
      try {
         mail = new MailHandler(sender);
         mail.setFrom("leerj0824@gmail.com", "이젠 파이널 테스트");
         mail.setTo(email);
         mail.setSubject(title);
         mail.setText(body);
         mail.send();
      } catch (Exception e) {
         e.printStackTrace();
      }

      String resultCode = "S-1";
      String msg = "메일이 발송되었습니다.";
      return Maps.of("resultCode", resultCode, "msg", msg);
   }

}
