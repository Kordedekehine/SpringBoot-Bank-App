package koredebank.example.bank.Email;


import koredebank.example.bank.dto.UserDepositsFundsRequestDto;
import koredebank.example.bank.model.Transaction;
import koredebank.example.bank.model.User;
import koredebank.example.bank.model.UserAccount;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import java.util.Optional;

@Service
public class EmailServiceImpl implements EmailService {


    private final JavaMailSender javaMailSender;


    private final SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
    private final String REGISTRATION_ERROR_MESSAGE = "Registration Not Successful";

    private final String FORGOT_PASSWORD_ERROR_MESSAGE = "Password Recovery Not Successful";

    private final String MESSAGE_NOT_SENT_MESSAGE = "Message Not sent from bank";

    private final String ACCOUNT_CREATION_ERROR = "Account not created due to some issues";

    private final String  TRANSACTION_FAILURE = "Cannot send funds!";

    private final String DEPOSIT_FAILURE = "Fail to deposit Funds";


    public EmailServiceImpl(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    @Override
    public void sendRegistrationSuccessfulEmail(User userEmail, String token) throws MessagingException {
        String link = "http://localhost:9090" + token;

        simpleMailMessage.setTo(userEmail.getEmail());
        simpleMailMessage.setSubject("Account Activation");
        simpleMailMessage.setFrom("salamikehinde345@gmail.com");
        String template = "Dear [[name]],\n"
                + "Thanks for registering on people bank.\n"
                + "Kindly use the code below to validate your account and activate your account:\n"
                + "[[code]]\n"
                + "Thank you.\n"
                + "Bank team";
        template = template.replace("[[name]]", userEmail.getFirstname());
        template = template.replace("[[code]]", token);
        simpleMailMessage.setText(template);

        try {
            javaMailSender.send(simpleMailMessage);
        } catch (Exception exception) {
            exception.printStackTrace();
            throw new MessagingException(String.format(REGISTRATION_ERROR_MESSAGE));
        }
    }

    @Override
    public void sendVerificationMessage(Optional<User> user) throws MessagingException {

        SimpleMailMessage message = new SimpleMailMessage();
        simpleMailMessage.setText("Bank");
        simpleMailMessage.setFrom("salamikehinde345@gmail.com");
        simpleMailMessage.setTo(user.get().getEmail());
        simpleMailMessage.setSubject("Welcome to people bank");
        String template = "Dear [[name]],\n"
                + "Your account has been verified, kindly login to explore\n"
                + "[[URL]]\n"
                + "Thank you,\n\n"
                + "The Bank Team";
        //    template = template.replace("[[name]]", usersEntity.getFirstName());
        template = template.replace("[[name]]", user.get().getFirstname());
        template = template.replace("[[URL]]", "africa bank");
        // message.setText(template);
        simpleMailMessage.setText(template);
        try {
            // javaMailSender.send(message);
            javaMailSender.send(simpleMailMessage);
        } catch (Exception exception) {
            throw new MessagingException(MESSAGE_NOT_SENT_MESSAGE);
        }
    }


    @Override
    public void sendChangePasswordMessage(Optional<User> user) throws MessagingException {

        SimpleMailMessage message = new SimpleMailMessage();
        simpleMailMessage.setText("bank");
        // simpleMailMessage.setFrom("abraham.ariyo@autox.africa");
        simpleMailMessage.setFrom("salamikehinde345@gmail.com");
        //simpleMailMessage.setTo(usersEntity.getEmail());
        simpleMailMessage.setTo(user.get().getEmail());
        simpleMailMessage.setSubject("bank");
        String template = "Dear [[name]],\n"
                // + "Your account has been verified, kindly login to explore\n"
                + "You have successfully change your password!"
                + " Kindly reach out to us, if you have any incoherence from your side \n"
                + "[[URL]]\n"
                + "Thank you,\n\n"
                + "The Bank Team";
        //    template = template.replace("[[name]]", usersEntity.getFirstName());
        template = template.replace("[[name]]", user.get().getFirstname());
        template = template.replace("[[URL]]", "africa bank");
        // message.setText(template);
        simpleMailMessage.setText(template);
        try {
            // javaMailSender.send(message);
            javaMailSender.send(simpleMailMessage);
        } catch (Exception exception) {
            throw new MessagingException(MESSAGE_NOT_SENT_MESSAGE);
        }
    }

    @Override
    public void sendForgotPasswordMessage(User userEmail, String token) throws MessagingException {
        String link = "http://localhost:9090" + token;
        simpleMailMessage.setTo(userEmail.getEmail());
        simpleMailMessage.setSubject("Password Recovery");
        simpleMailMessage.setFrom("salamikehinde345@gmail.com");
        String template = "Dear [[name]],\n"
                + "Thanks for registering on people bank.\n"
                + "Kindly use the code below to validate your account, and recover your account:\n"
                + "[[code]]\n"
                + "Thank you.\n"
                + "Bank team";
        template = template.replace("[[name]]", userEmail.getFirstname());
        template = template.replace("[[code]]", token);
        simpleMailMessage.setText(template);

        try {
            javaMailSender.send(simpleMailMessage);
        } catch (Exception exception) {
            exception.printStackTrace();
            throw new MessagingException(String.format(FORGOT_PASSWORD_ERROR_MESSAGE));
        }
    }

    @Override
    public void sendAccountCreationSuccessfulEmail(UserAccount userAccount) throws MessagingException {
            simpleMailMessage.setTo(userAccount.getUser().getEmail());
            simpleMailMessage.setSubject("Account Creation Successful");
            simpleMailMessage.setFrom("salamikehinde345@gmail.com");
            String template = "Dear [[name]],\n"
                    + "Thanks for registering on people bank.\n"
                    + "Congratulations! Your account has successfully being created\n"
                    + "Thank you.\n"
                    + "Bank team";
            template = template.replace("[[name]]", userAccount.getUser().getFirstname());
//            template = template.replace("[[code]]", token);
            simpleMailMessage.setText(template);

            try {
                javaMailSender.send(simpleMailMessage);
            } catch (Exception exception) {
                exception.printStackTrace();
                throw new MessagingException(String.format(ACCOUNT_CREATION_ERROR));
            }
    }

    @Override
    public void sendTransactionSuccessfulMessage(Transaction transaction) throws MessagingException {

        simpleMailMessage.setTo(transaction.getUser().getEmail());
        simpleMailMessage.setSubject("Account Creation Successful");
        simpleMailMessage.setFrom("salamikehinde345@gmail.com");
        String template = "Dear [[name]],\n"
                + "Transaction Successful!\n"
                + "Thank you for using People's Bank.\n"
                + "Bank team";
        template = template.replace("[[name]]", transaction.getUser().getFirstname());
//            template = template.replace("[[code]]", token);
        simpleMailMessage.setText(template);

        try {
            javaMailSender.send(simpleMailMessage);
        } catch (Exception exception) {
            exception.printStackTrace();
            throw new MessagingException(String.format(TRANSACTION_FAILURE));
        }
    }

    @Override
    public void sendAlertReceivedMessage(Transaction transaction) throws MessagingException {

        simpleMailMessage.setTo(transaction.getTargetEmail());
        simpleMailMessage.setSubject("Account Creation Successful");
        simpleMailMessage.setFrom("salamikehinde345@gmail.com");
        String template = "Dear [[name]],\n"
                + "Thanks for registering on people bank.\n"
                + "Congratulations! Your account has successfully being created\n"
                + "Thank you.\n"
                + "Bank team";
        template = template.replace("[[name]]", "Client");
//            template = template.replace("[[code]]", token);
        simpleMailMessage.setText(template);

        try {
            javaMailSender.send(simpleMailMessage);
        } catch (Exception exception) {
            exception.printStackTrace();
            throw new MessagingException(String.format(""));
        }
    }

    @Override
    public void sendDepositSuccessfulMessage(Optional<UserAccount> userAccount, UserDepositsFundsRequestDto userDepositsFundsRequestDto) throws MessagingException {

        simpleMailMessage.setTo(userAccount.get().getAccountEmail());
        simpleMailMessage.setSubject("Account Creation Successful");
        simpleMailMessage.setFrom("salamikehinde345@gmail.com");
        String template = "Dear [[name]],\n"
                + "You have successfully deposit the sum of\n"
                + userDepositsFundsRequestDto.getAmount()
                + "into Your account \n"
                + "Thank you.\n"
                + "Bank team";
        template = template.replace("[[name]]", userAccount.get().getOwnerName().toString());
//            template = template.replace("[[code]]", token);
        simpleMailMessage.setText(template);

        try {
            javaMailSender.send(simpleMailMessage);
        } catch (Exception exception) {
            exception.printStackTrace();
            throw new MessagingException(String.format(DEPOSIT_FAILURE));
        }
    }
}
