package koredebank.example.bank.controller;

import koredebank.example.bank.dto.*;
import koredebank.example.bank.security.exceptions.*;
import koredebank.example.bank.service.userService.UserServices;
import koredebank.example.bank.util.ApiRoutes;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;

@Slf4j
@RestController
@RequestMapping(ApiRoutes.BANK)
public class UserController {

    @Autowired
    UserServices userServices;

    @PostMapping(ApiRoutes.User+"/validate")
    public ResponseEntity<?> Registration(@RequestBody UserLoginValidationRequestDto userLoginValidationRequestDto) throws MessagingException, GeneralServiceException, AccountCreationException {

            return new ResponseEntity<>(userServices.validateUserAccount(userLoginValidationRequestDto), HttpStatus.OK);

    }

    @PostMapping(ApiRoutes.User+"/signUp")
    public ResponseEntity<?> Registration(@RequestBody UserSignUpRequestDto userSignUpRequestDto) throws MessagingException, GeneralServiceException, AccountCreationException {
        log.info(userSignUpRequestDto.toString());

            return new ResponseEntity<>(userServices.signUp(userSignUpRequestDto),HttpStatus.OK);
    }

    @PostMapping(ApiRoutes.User+"/login")
    public ResponseEntity<?> SignIn(@RequestBody UserLoginDto userLoginDto) throws IncorrectPasswordException, GeneralServiceException {

            return new ResponseEntity<>(userServices.login(userLoginDto),HttpStatus.OK);

    }

    @PostMapping(ApiRoutes.User+"/validateTokenAndPassword")
    public ResponseEntity<?> RetrievePassword(@RequestBody UserRetrieveForgotPasswordRequestDto userRetrieveForgotPasswordRequestDto) throws MessagingException, AuthorizationException, GeneralServiceException {

            return new ResponseEntity<>(userServices.validateForgotTokenAndNewForgotPassword(userRetrieveForgotPasswordRequestDto),HttpStatus.OK);
    }

    @PutMapping(ApiRoutes.User+"/change-password")
    public ResponseEntity<?> ChangePassword(@RequestHeader("Authorization")String authentication, @RequestBody UserChangePasswordRequestDto userChangePasswordRequestDto) throws AuthorizationException, MessagingException, GeneralServiceException {

            return new ResponseEntity<>(userServices.userChangePassword(userChangePasswordRequestDto ,authentication),HttpStatus.OK);

    }

    @PostMapping(ApiRoutes.User+"/forgot-password")
    public ResponseEntity<?> forgotPassword(UserForgotPasswordRequestDto forgotPasswordRequestDto) throws MessagingException, AuthorizationException, GeneralServiceException {

            return new ResponseEntity<>(userServices.userForgotPassword(forgotPasswordRequestDto),HttpStatus.OK);

    }

}


