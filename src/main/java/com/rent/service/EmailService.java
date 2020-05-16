package com.rent.service;

import com.rent.entity.User;
import javax.mail.internet.MimeMessage;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class EmailService {
    private final Log log = LogFactory.getLog(this.getClass());
    
	@Value("${spring.mail.username}")
	private String MESSAGE_FROM;
	
	private JavaMailSender javaMailSender;

	@Autowired
	public void setJavaMailSender(JavaMailSender javaMailSender) {
		this.javaMailSender = javaMailSender;
	}


	public void sendMessage(User user) {
//		SimpleMailMessage message = null;
                MimeMessage message = javaMailSender.createMimeMessage();
                String email = user.getEmail();
                String activation = user.getActivation();
		
		try {
//			message = new SimpleMailMessage();
//			message.setFrom(MESSAGE_FROM);
//			message.setTo(email);
//			message.setSubject("Sikeres regisztrálás");
                        MimeMessageHelper helper = new MimeMessageHelper(message, true);
			helper.setFrom(MESSAGE_FROM);
			helper.setTo(email);
			helper.setSubject("Sikeres regisztrálás");
			String msgContent = "Kedves " + user.getFullName() + "! <br/><br/> Köszönjük, hogy regisztráltál az oldalunkra!<br/><br/>" +
                                            "Regisztrációdat <a href=\"http://localhost:8080/activation/" + activation + "\">ide</a> kattintva aktiválhatod.";
                        helper.setText(msgContent, true);
			javaMailSender.send(message);
			
		} catch (Exception e) {
			log.error("Hiba e-mail küldéskor az alábbi címre: " + email + "  " + e);
		}
		

	}
	
	
}
