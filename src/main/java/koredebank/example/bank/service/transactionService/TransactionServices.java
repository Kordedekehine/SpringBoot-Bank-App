package koredebank.example.bank.service.transactionService;

import koredebank.example.bank.dto.*;
import koredebank.example.bank.model.UserAccount;
import koredebank.example.bank.security.exceptions.AccountCreationException;
import koredebank.example.bank.security.exceptions.AuthorizationException;
import koredebank.example.bank.security.exceptions.GeneralServiceException;
import koredebank.example.bank.security.exceptions.ImageUploadException;

import javax.mail.MessagingException;

public interface TransactionServices {

    UserCreateAccountResponseDto createAccounts(UserCreateAccountRequestDto userCreateAccountRequestDto, String authentication) throws AccountCreationException, AuthorizationException, MessagingException;

    UserCheckAccountBalanceResponseDto checkAccBalance(UserCheckAccountBalanceRequestDto userCheckAccountBalanceRequestDto, String authentication) throws AccountCreationException, AuthorizationException, MessagingException, GeneralServiceException;

    boolean transferFunds(UserTransferFundsRequestDto userTransferFundsRequestDto, String authentication) throws AuthorizationException, GeneralServiceException, MessagingException;

    boolean depositFunds(UserDepositsFundsRequestDto userDepositsFundsRequestDto,String authentication) throws AuthorizationException, GeneralServiceException, MessagingException;

    boolean withdrawFunds(UserWithdrawFundsRequestDto userWithdrawFundsRequestDto,String authentication) throws AuthorizationException, GeneralServiceException, MessagingException;

    UserCompliantFormResponseDto usersCompliant(String loginToken, UserCompliantFormRequestDto userCompliantFormRequestDto) throws AuthorizationException, GeneralServiceException, MessagingException, ImageUploadException;

    TransactionListResponseDto listTransactions(String loginToken, int page, int size) throws AuthorizationException;

     UserAccount getAccount(String accountNumber);
}
