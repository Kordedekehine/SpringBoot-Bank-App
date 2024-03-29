package koredebank.example.bank.controller;

import koredebank.example.bank.dto.*;
import koredebank.example.bank.security.exceptions.AccountCreationException;
import koredebank.example.bank.security.exceptions.AuthorizationException;
import koredebank.example.bank.security.exceptions.GeneralServiceException;
import koredebank.example.bank.service.accountManagerService.AccountManagerServices;
import koredebank.example.bank.util.ApiRoutes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;

@RestController
@RequestMapping(ApiRoutes.BANK)
public class AccountManagerController {
    private static Logger log = LoggerFactory.getLogger(AccountManagerController.class);

    @Autowired
    AccountManagerServices accountManagerServices;


    @PostMapping(ApiRoutes.ACCOUNTMANAGER +"/register")
    public ResponseEntity<?> Registration(@RequestBody AccountManagerSignUpRequestDto accountManagerSignUpRequestDto) throws AccountCreationException {
        log.info(accountManagerSignUpRequestDto.toString());

            return new ResponseEntity<>(accountManagerServices.registerAccountManager(accountManagerSignUpRequestDto), HttpStatus.OK);

    }


    @PutMapping(ApiRoutes.ACCOUNTMANAGER+"/change-password")
    public ResponseEntity<?> ChangePassword(@RequestParam(name = "id") String id, @RequestBody AccountManagerChangePassword accountManagerChangePassword) throws AuthorizationException, GeneralServiceException {

            return new ResponseEntity<>(accountManagerServices.changeAccountManagerPassword(accountManagerChangePassword,id), HttpStatus.OK);

    }

    @PutMapping(ApiRoutes.ACCOUNTMANAGER+ "/forgot-password")
    public ResponseEntity<?> ForgotPassword(@RequestBody AccountManagerForgotPassword accountManagerForgotPassword) throws GeneralServiceException {

            return new ResponseEntity<>(accountManagerServices.forgotAccountManagerPassword(accountManagerForgotPassword), HttpStatus.OK);

    }

    @PostMapping(ApiRoutes.ACCOUNTMANAGER+ "/block-user")
    public ResponseEntity<?> BlockUser(@RequestBody AccountManagerBlockUserRequestDto accountManagerBlockUserRequestDto) throws MessagingException, GeneralServiceException {

            return new ResponseEntity<>(accountManagerServices.blockAccountUser(accountManagerBlockUserRequestDto), HttpStatus.OK);
    }

    @PostMapping(ApiRoutes.ACCOUNTMANAGER+ "/unblock-user")
    public ResponseEntity<?> UnBlockUser(@RequestBody AccountManagerUnblockUserRequestDto accountManagerUnblockUserRequestDto) throws MessagingException, GeneralServiceException {

            return new ResponseEntity<>(accountManagerServices.unblockAccountUser(accountManagerUnblockUserRequestDto), HttpStatus.OK);

    }


    @GetMapping(ApiRoutes.ACCOUNTMANAGER+"/list-all-transactions")
    public ResponseEntity<?> listAllTransactions(@RequestParam(value = "page",defaultValue = "1") int page,
                                                 @RequestParam(value = "size",defaultValue = "3") int size) throws AuthorizationException {

            return new ResponseEntity<>(accountManagerServices.listTransactions(page,size), HttpStatus.OK);

    }

    @GetMapping(ApiRoutes.ACCOUNTMANAGER+"/list-all-userAccounts-History")
    public ResponseEntity<?> listAllUserAccountsHistory(@RequestParam(value = "page",defaultValue = "1") int page, @RequestParam(value = "size",defaultValue = "3") int size) throws AuthorizationException {

            return new ResponseEntity<>(accountManagerServices.listUsersAccounts(page,size), HttpStatus.OK);

    }

    @GetMapping(ApiRoutes.ACCOUNTMANAGER+"/list-all-usersComplain-Form")
    public ResponseEntity<?> listAllUsersComplainForm(@RequestParam(value = "page",defaultValue = "1") int page, @RequestParam(value = "size",defaultValue = "3") int size) throws AuthorizationException {

            return new ResponseEntity<>(accountManagerServices.listUserCompliantAndSchedules(page,size), HttpStatus.OK);

    }
}
