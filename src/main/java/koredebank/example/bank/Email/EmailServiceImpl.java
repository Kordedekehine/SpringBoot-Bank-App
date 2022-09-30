package koredebank.example.bank.Email;


import koredebank.example.bank.dto.UserDepositsFundsRequestDto;
import koredebank.example.bank.model.CustomerCompliantForm;
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

    private final String WITHDRAW_FAILURE = "Fail to withdraw Funds";

    private final String SCHEDULE_NOT_SENT= "Schedule not sent! Kindly retry";


    public EmailServiceImpl(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    @Override
    public void sendRegistrationSuccessfulEmail(User userEmail, String token) throws MessagingException {
        String link = "http://localhost:9090" + token;

        simpleMailMessage.setTo(userEmail.getEmail());
        simpleMailMessage.setSubject("Welcome To People's Bank");
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
        simpleMailMessage.setText("People's Bank");
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
    public void sendTransactionSuccessfulMessage(Transaction transaction,User user) throws MessagingException {

        simpleMailMessage.setTo(user.getEmail());
        simpleMailMessage.setSubject("Transaction Successful");
        simpleMailMessage.setFrom("salamikehinde345@gmail.com");
        String template = "Dear [[name]],\n"
                + "Transaction Successful!\n"
                + "You have successfully transfer " +transaction.getAmount() + " to " +transaction.getTargetOwnerName()
                + " owner of the ID " + transaction.getTargetAccountId()
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
        simpleMailMessage.setSubject("Alert Mail");
        simpleMailMessage.setFrom("salamikehinde345@gmail.com");
        String template = "Dear [[name]],\n"
                +  "You have successfully received " +transaction.getAmount() + " from \n"
                + transaction.getUser().getFirstname() + " the owner of the id " + transaction.getSourceAccountId()
                + "Congratulations! Your account has successfully being credited\n"
                + "Thank you.\n"
                + "Bank team";
        template = template.replace("[[name]]", transaction.getTargetOwnerName());
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
    public void sendDepositSuccessfulMessage(User user,UserAccount userAccount) throws MessagingException {

        simpleMailMessage.setTo(user.getEmail());
        simpleMailMessage.setSubject("Deposit Successful");
        simpleMailMessage.setFrom("salamikehinde345@gmail.com");
        String template = "Dear [[name]],\n"
                + "You have successfully deposit some funds into your account\n"
                + "into Your account \n"
                + "Balance : " + userAccount.getCurrentBalance() + " \n"
                + " Thank you.\n"
                + "Bank team";
        template = template.replace("[[name]]", userAccount.getOwnerName());
        simpleMailMessage.setText(template);
        try {
            javaMailSender.send(simpleMailMessage);
        } catch (Exception exception) {
            exception.printStackTrace();
            throw new MessagingException(String.format(DEPOSIT_FAILURE));
        }
    }

    @Override
    public void sendWithdrawSuccessfulMessage(User user, UserAccount userAccount) throws MessagingException {

        simpleMailMessage.setTo(user.getEmail());
        simpleMailMessage.setSubject("Withdraw Successful");
        simpleMailMessage.setFrom("salamikehinde345@gmail.com");
        String template = "Dear [[name]],\n"
                + "You have successfully withdraw some funds into your account\n"
                + "into Your account \n"
                + "Balance : " + userAccount.getCurrentBalance() + " \n"
                + " Thank you.\n"
                + "Bank team";
        template = template.replace("[[name]]", userAccount.getOwnerName());
        simpleMailMessage.setText(template);
        try {
            javaMailSender.send(simpleMailMessage);
        } catch (Exception exception) {
            exception.printStackTrace();
            throw new MessagingException(String.format(WITHDRAW_FAILURE));
        }
    }

    @Override
    public void sendCompliantNotification(CustomerCompliantForm customerCompliantForm) throws MessagingException {

        simpleMailMessage.setTo(customerCompliantForm.getUser().getEmail());
        simpleMailMessage.setSubject("Com[liant Request Successful");
        simpleMailMessage.setFrom("salamikehinde345@gmail.com");
        String template = "Dear [[name]],\n"
                + "You have successfully book a session with us\n"
                + "Kindly show up before and after 30 minutes of the booked time \n"
                + "Thank you.\n"
                + "Bank team";
        template = template.replace("[[name]]", customerCompliantForm.getUser().getFirstname());
        simpleMailMessage.setText(template);

        try {
            javaMailSender.send(simpleMailMessage);
        } catch (Exception exception) {
            exception.printStackTrace();
            throw new MessagingException(String.format(SCHEDULE_NOT_SENT));
        }
    }

    @Override
    public void sendAccountSuspendedNotification(User user) throws MessagingException {

        simpleMailMessage.setTo(user.getEmail());
        simpleMailMessage.setSubject("Account Suspension");
        simpleMailMessage.setFrom("salamikehinde345@gmail.com");
        String template = "Dear [[name]],\n"
                + "Your account has been suspended till further notice! "
                +"As your recent activities is not inline with our principles \n"
                + "Thank you.\n"
                + "Bank team";
        template = template.replace("[[name]]", user.getFirstname());
        simpleMailMessage.setText(template);

        try {
            javaMailSender.send(simpleMailMessage);
        } catch (Exception exception) {
            exception.printStackTrace();
            throw new MessagingException(String.format(MESSAGE_NOT_SENT_MESSAGE));
        }
    }

    @Override
    public void sendAccountSuspendedRevertNotification(User user) throws MessagingException {

        simpleMailMessage.setTo(user.getEmail());
        simpleMailMessage.setSubject("Suspension Reversion");
        simpleMailMessage.setFrom("salamikehinde345@gmail.com");
        String template = "Dear [[name]],\n"
                + "The suspension on your account has been reverted! "
                +"Kindly stick to our rules and regulations to avoid such harsh judgement from henceforth\n"
                + "Thank you.\n"
                + "Bank team";
        template = template.replace("[[name]]", user.getFirstname());
        simpleMailMessage.setText(template);

        try {
            javaMailSender.send(simpleMailMessage);
        } catch (Exception exception) {
            exception.printStackTrace();
            throw new MessagingException(String.format(MESSAGE_NOT_SENT_MESSAGE));
        }
    }
}
//{
//  "id": "3ec85cd2-ee67-4d1a-9bd1-08248d7816b9",
//  "firstName": "taiwo",
//  "lastName": "yemisi",
//  "phoneNumber": "07065435621",
//  "email": "salamtaye0@gmail.com",
//  "roles": "BASE_USER",
//  "token": {
//    "accessToken": "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJzYWxhbXRheWUwQGdtYWlsLmNvbSIsInJvbGVzIjoiQkFTRV9VU0VSIiwiaXNzIjoiQVVUT1giLCJpYXQiOjE2NjQ1MjgyMjgsImV4cCI6MTY2NDYxNDYyOH0.ZBPM1J-vPVWTMKBbjvgtAiVDmslbLjrKpFU8Vg5Sx7Fk-_12k5SHsAAo_iWhiMXtO-vm-vQvz9n_zShIRk0-4A",
//    "tokenType": "BEARER_TOKEN"
//  }
//}