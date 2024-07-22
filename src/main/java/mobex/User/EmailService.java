package mobex.User;

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

    public void sendSetPasswordEmail(String email, String token) throws MessagingException {
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);

        mimeMessageHelper.setTo(email);
        mimeMessageHelper.setSubject("Set Password for MobExchange");

        String htmlContent = String.format(
                "<html>" +
                        "<body>" +
                        "<p>Click the button below to set your password:</p>" +
                        "<a href=\"http://localhost:8080/auth/set-password/?token=%s\" style=\"" +
                        "display: inline-block; " +
                        "padding: 10px 20px; " +
                        "font-size: 16px; " +
                        "font-weight: bold; " +
                        "color: #ffffff; " +
                        "background-color: #007bff; " +
                        "text-align: center; " +
                        "text-decoration: none; " +
                        "border-radius: 5px; " +
                        "border: 1px solid #007bff; " +
                        "\" " +
                        "target=\"_blank\">Set Password</a>" +
                        "</body>" +
                        "</html>",
                token
        );

        mimeMessageHelper.setText(htmlContent, true);
        mailSender.send(mimeMessage);
    }


}
