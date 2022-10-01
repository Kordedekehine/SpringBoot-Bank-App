package koredebank.example.bank.service.accountManagerService;

import koredebank.example.bank.dto.*;
import koredebank.example.bank.security.exceptions.AccountCreationException;
import koredebank.example.bank.security.exceptions.AuthorizationException;
import koredebank.example.bank.security.exceptions.GeneralServiceException;

import javax.mail.MessagingException;

public interface AccountManagerServices {

    AccountManagerSignUpResponseDto registerAccountManager(AccountManagerSignUpRequestDto accountManagerSignUpRequestDto) throws AccountCreationException;

    boolean changeAccountManagerPassword(AccountManagerChangePassword accountManagerChangePassword,String id) throws AuthorizationException, GeneralServiceException;

    boolean forgotAccountManagerPassword(AccountManagerForgotPassword accountManagerForgotPassword) throws GeneralServiceException;

    boolean blockAccountUser(AccountManagerBlockUserRequestDto accountManagerBlockUserRequestDto) throws GeneralServiceException, MessagingException;

    boolean unblockAccountUser(AccountManagerUnblockUserRequestDto accountManagerUnblockUserRequestDto) throws GeneralServiceException, MessagingException;

    TransactionListResponseDto listTransactions( int page, int size) throws AuthorizationException;

    AccountListResponseDto listUsersAccounts( int page, int size) throws AuthorizationException;

    UserCompliantListResponseDto listUserCompliantAndSchedules(int page, int size) throws AuthorizationException;

}
