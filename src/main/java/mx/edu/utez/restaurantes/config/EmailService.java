package mx.edu.utez.restaurantes.config;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;


@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    public void sendHtmlEmail(String to, String subject, String htmlContent) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true); // true para permitir contenido HTML
        try {
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(htmlContent, true); // true indica que es contenido HTML
            mailSender.send(message);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }
}