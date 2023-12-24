package koredebank.example.bank.service.userService;

import koredebank.example.bank.dto.*;
import koredebank.example.bank.model.Transaction;
import koredebank.example.bank.model.UserAccount;
import koredebank.example.bank.security.exceptions.*;

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



}
