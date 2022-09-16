package koredebank.example.bank.Email;


import koredebank.example.bank.dto.UserDepositsFundsRequestDto;
import koredebank.example.bank.model.Transaction;
import koredebank.example.bank.model.User;
import koredebank.example.bank.model.UserAccount;

import javax.mail.MessagingException;
import java.util.Optional;

public interface EmailService {
    void sendRegistrationSuccessfulEmail(User userEmail, String token)throws MessagingException;
    void sendVerificationMessage(Optional<User> user) throws MessagingException;

    void sendChangePasswordMessage(Optional<User> usersEntity) throws MessagingException;

    void sendForgotPasswordMessage(User userEmail, String token) throws MessagingException;

    void sendAccountCreationSuccessfulEmail(UserAccount userAccount) throws MessagingException;

   void sendTransactionSuccessfulMessage(Transaction transaction) throws MessagingException;

   void sendAlertReceivedMessage(Transaction transaction) throws MessagingException;

   void sendDepositSuccessfulMessage(Optional<UserAccount> userAccount, UserDepositsFundsRequestDto userDepositsFundsRequestDto) throws MessagingException;
}

