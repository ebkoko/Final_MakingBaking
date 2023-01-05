package com.ezen.makingbaking.configuration.handler;

import java.io.UnsupportedEncodingException;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;

public class MailHandler {
	   private JavaMailSender sender;
	   private MimeMessage message;
	   private MimeMessageHelper messageHelper;

	   public MailHandler(JavaMailSender sender) throws MessagingException {
	      this.sender = sender;
	      this.message = this.sender.createMimeMessage();
	      this.messageHelper = new MimeMessageHelper(message, true, "UTF-8");
	   }

	   public void setFrom(String mail, String name) throws UnsupportedEncodingException, MessagingException {
	      messageHelper.setFrom(mail, name);
	   }

	   public void setTo(String mail) throws MessagingException {
	      messageHelper.setTo(mail);
	   }

	   public void setSubject(String subject) throws MessagingException {
	      messageHelper.setSubject(subject);
	   }

	   public void setText(String text) throws MessagingException {
	      messageHelper.setText(text, true);
	   }

	   public void send() {
	      try {
	         sender.send(message);
	      } catch (Exception e) {
	         e.printStackTrace();
	      }
	   }

}
