package koredebank.example.bank.controller;

import koredebank.example.bank.dto.AccountManagerChangePassword;
import koredebank.example.bank.dto.AccountManagerForgotPassword;
import koredebank.example.bank.dto.AccountManagerSignUpRequestDto;
import koredebank.example.bank.dto.AccountManagerSignUpResponseDto;
import koredebank.example.bank.service.accountManagerService.AccountManagerServices;
import koredebank.example.bank.util.ApiRoutes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(ApiRoutes.BANK)
public class AccountManagerController {
    private static Logger log = LoggerFactory.getLogger(AccountManagerController.class);

    @Autowired
    AccountManagerServices accountManagerServices;


    @PostMapping(ApiRoutes.ACCOUNTMANAGER +"/register")
    public ResponseEntity<?> Registration(@RequestBody AccountManagerSignUpRequestDto accountManagerSignUpRequestDto) {
        log.info(accountManagerSignUpRequestDto.toString());
        try {
            //starting point
            return new ResponseEntity<>(accountManagerServices.registerAccountManager(accountManagerSignUpRequestDto), HttpStatus.OK);
        } catch (Exception exception) {
            return new ResponseEntity<>(exception.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }


    @PostMapping(ApiRoutes.ACCOUNTMANAGER+"/change-password")
    public ResponseEntity<?> ChangePassword(@RequestParam(name = "id") String id, @RequestBody AccountManagerChangePassword accountManagerChangePassword) {
        try {
            //starting point
            return new ResponseEntity<>(accountManagerServices.changeAccountManagerPassword(accountManagerChangePassword,id), HttpStatus.OK);
        } catch (Exception exception) {
            return new ResponseEntity<>(exception.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping(ApiRoutes.ACCOUNTMANAGER+ "/forgot-password")
    public ResponseEntity<?> ForgotPassword(@RequestBody AccountManagerForgotPassword
                                                    accountManagerForgotPassword) {
        try {
            //starting point
            return new ResponseEntity<>(accountManagerServices.forgotAccountManagerPassword(accountManagerForgotPassword), HttpStatus.OK);
        } catch (Exception exception) {
            return new ResponseEntity<>(exception.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
}
