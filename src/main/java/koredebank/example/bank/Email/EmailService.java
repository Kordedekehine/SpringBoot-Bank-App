package koredebank.example.bank.Email;


import koredebank.example.bank.model.CustomerCompliantForm;
import koredebank.example.bank.model.Transaction;
import koredebank.example.bank.model.UserEntity;
import koredebank.example.bank.model.UserAccount;

import javax.mail.MessagingException;
import java.util.Optional;

public interface EmailService {
    void sendRegistrationSuccessfulEmail(UserEntity userEntityEmail, String token)throws MessagingException;
    void sendVerificationMessage(Optional<UserEntity> user) throws MessagingException;

    void sendChangePasswordMessage(Optional<UserEntity> usersEntity) throws MessagingException;

    void sendForgotPasswordMessage(UserEntity userEntityEmail, String token) throws MessagingException;

    void sendAccountCreationSuccessfulEmail(UserAccount userAccount) throws MessagingException;

   void sendTransactionSuccessfulMessage(Transaction transaction, UserEntity userEntity) throws MessagingException;

   void sendAlertReceivedMessage(Transaction transaction) throws MessagingException;

   void sendDepositSuccessfulMessage(UserEntity userEntity, UserAccount userAccount) throws MessagingException;

    void sendWithdrawSuccessfulMessage(UserEntity userEntity, UserAccount userAccount) throws MessagingException;

   void sendCompliantNotification(CustomerCompliantForm customerCompliantForm) throws MessagingException;

   void sendAccountSuspendedNotification(UserEntity userEntity) throws MessagingException;

    void sendAccountSuspendedRevertNotification(UserEntity userEntity) throws MessagingException;


}

