package koredebank.example.bank.service.userService;

import koredebank.example.bank.dto.*;
import koredebank.example.bank.model.Transaction;
import koredebank.example.bank.model.UserAccount;
import koredebank.example.bank.security.exceptions.AccountCreationException;
import koredebank.example.bank.security.exceptions.AuthorizationException;
import koredebank.example.bank.security.exceptions.GeneralServiceException;
import koredebank.example.bank.security.exceptions.IncorrectPasswordException;

import javax.mail.MessagingException;
import java.util.List;
import java.util.Optional;

public interface UserServices {

    UserLoginResponseDto login(UserLoginDto userLoginDto) throws IncorrectPasswordException, GeneralServiceException;

    UserSignUpResponseDto signUp(UserSignUpRequestDto userSignUpRequestDto) throws GeneralServiceException, AccountCreationException, MessagingException;

    boolean validateUserAccount(UserLoginValidationRequestDto userLoginValidationRequestDto) throws AccountCreationException, GeneralServiceException, MessagingException;

    boolean userChangePassword(UserChangePasswordRequestDto userChangePasswordRequestDto, String authentication) throws AuthorizationException, GeneralServiceException, MessagingException;

    boolean userForgotPassword(UserForgotPasswordRequestDto userForgotPasswordRequestDto) throws GeneralServiceException, MessagingException, AuthorizationException;

    boolean validateForgotTokenAndNewForgotPassword(UserRetrieveForgotPasswordRequestDto userRetrieveForgotPasswordRequestDto) throws GeneralServiceException, MessagingException, AuthorizationException;

   UserCreateAccountResponseDto createAccounts(UserCreateAccountRequestDto userCreateAccountRequestDto,String authentication) throws AccountCreationException, AuthorizationException, MessagingException;

    UserCheckAccountBalanceResponseDto checkAccBalance(UserCheckAccountBalanceRequestDto userCheckAccountBalanceRequestDto,String authentication) throws AccountCreationException, AuthorizationException, MessagingException, GeneralServiceException;

    boolean transferFunds(UserTransferFundsRequestDto userTransferFundsRequestDto,String authentication) throws AuthorizationException, GeneralServiceException, MessagingException;

   boolean depositFunds(UserDepositsFundsRequestDto userDepositsFundsRequestDto,String authentication) throws AuthorizationException, GeneralServiceException, MessagingException;

   boolean withdrawFunds(UserWithdrawFundsRequestDto userWithdrawFundsRequestDto,String authentication) throws AuthorizationException, GeneralServiceException, MessagingException;

    UserCompliantFormResponseDto usersCompliant(String loginToken, UserCompliantFormRequestDto userCompliantFormRequestDto) throws AuthorizationException, GeneralServiceException, MessagingException;

    TransactionListResponseDto listTransactions(String loginToken, int page, int size) throws AuthorizationException;


}
