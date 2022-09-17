package koredebank.example.bank.service.accountManagerService;

import koredebank.example.bank.dto.AccountManagerChangePassword;
import koredebank.example.bank.dto.AccountManagerForgotPassword;
import koredebank.example.bank.dto.AccountManagerSignUpRequestDto;
import koredebank.example.bank.dto.AccountManagerSignUpResponseDto;
import koredebank.example.bank.security.exceptions.AccountCreationException;
import koredebank.example.bank.security.exceptions.AuthorizationException;
import koredebank.example.bank.security.exceptions.GeneralServiceException;

public interface AccountManagerServices {

    AccountManagerSignUpResponseDto registerAccountManager(AccountManagerSignUpRequestDto accountManagerSignUpRequestDto) throws AccountCreationException;

    boolean changeAccountManagerPassword(AccountManagerChangePassword accountManagerChangePassword,String id) throws AuthorizationException, GeneralServiceException;

    boolean forgotAccountManagerPassword(AccountManagerForgotPassword accountManagerForgotPassword) throws GeneralServiceException;
}
