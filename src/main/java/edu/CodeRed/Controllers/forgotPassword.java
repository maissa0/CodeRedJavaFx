package edu.CodeRed.Controllers;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.*;

import com.twilio.rest.verify.v2.service.Verification;
import com.twilio.Twilio;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import edu.CodeRed.services.userservice;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class forgotPassword {

    @FXML
    private ResourceBundle resources;
    @FXML
    private URL location;
    @FXML
    private TextField email_input;
    @FXML
    private TextField generatedcode_input;
    @FXML
    private Text generatedcode_text;
    @FXML
    private Button verify_button;
    @FXML
    private TextField newpass_input;
    @FXML
    private TextField retypenewpass_input;
    @FXML
    private Text newpassword_text;
    @FXML
    private Text retypenewpassword_text;
    @FXML
    private Button submitpass_button;
    userservice userService = new userservice();
    private static final String generatedToken = generateRandomCode();

    @FXML
    void back_to_login(ActionEvent event) throws IOException {
     }




    @FXML
    // Find your Account Sid and Token at twilio.com/console
    public static final String ACCOUNT_SID = "ACe45a852cf11ad218e6325e8525cc17e0";
    public static final String AUTH_TOKEN = "6ec6a9e003eb7f8150d1e48b23621f44";

    public void sendVerificationCode(String phoneNumber) {
        Twilio.init(ACCOUNT_SID, AUTH_TOKEN);
        Verification verification = Verification.creator(
                        "VA8670a8a8dbffa9a4133fdfb8dc6fc985",
                        "+21653721027",
                        "sms")
                .create();

        System.out.println(verification.getSid());



    }
    @FXML
    void send_email(ActionEvent event) throws SQLException {
        if(userService.isEmailTaken(email_input.getText())) {
            sendEmail(email_input.getText());
            generatedcode_input.setVisible(true);
            generatedcode_text.setVisible(true);
            verify_button.setVisible(true);
        }
        else
            System.out.println("Email not registered");
    }

    @FXML
    void initialize() {
        List<Node> components = Arrays.asList(
                generatedcode_input, newpass_input, retypenewpass_input,
                generatedcode_text, newpassword_text, retypenewpassword_text,
                verify_button, submitpass_button
        );
        components.forEach(component -> component.setVisible(false));
    }

    @FXML
    void verify_button(ActionEvent event) {
        if(generatedcode_input.getText().equals(generatedToken)){
            System.out.println("jawk bhy");
            List<Node> components = Arrays.asList(
                    newpass_input, retypenewpass_input,
                    newpassword_text, retypenewpassword_text,
                    submitpass_button
            );
            components.forEach(component -> component.setVisible(true));
        }
    }

    @FXML
    void submitpass_button(ActionEvent event) {
        String newpassword = newpass_input.getText();
        String retype_newpassword = retypenewpass_input.getText();
        if(newpassword.equals(retype_newpassword))
            userService.updateforgottenpassword(email_input.getText(), newpassword);
    }

    public static void sendEmail(String to) {
        String subject = "Reset your password";

        // HTML-formatted email body
        String body = "<p style='font-size:16px; font-family: Arial, sans-serif;'>"
                + "Cher Utilisateur,<br><br>"
                + "Veuillez saisir le code à 4 chiffres suivant dans l'application pour réinitialiser votre mot de passe\n:<br><br>"
                + "<strong>" + generatedToken + "</strong><br><br>"
                + "Nos meilleurs voeux ,<br>"
                + "Opti'health Team"
                + "</p>";

        // Setup mail server properties
        Properties properties = new Properties();
        properties.put("mail.smtp.host", "smtp.gmail.com");
        properties.put("mail.smtp.port", "587");
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");
        // Create a Session object
        Session session = Session.getInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication("boularesisraa@gmail.com", "knek sotq lzic pnxd");
            }
        });

        try {
            // Create a MimeMessage object
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress("boularesisraa@gmail.com"));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
            message.setSubject(subject);
            message.setContent(body, "text/html"); // Set the content type to HTML

            // Send the message
            Transport.send(message);

            System.out.println("Email est envoyé avec succées!");

        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }


    public static String generateRandomCode() {
        // Create a Random object
        Random random = new Random();

        // Generate a random 4-digit code
        int code = 1000 + random.nextInt(9000);

        // Convert the code to a string
        return String.valueOf(code);
    }



}
