package koredebank.example.bank.controller;

import koredebank.example.bank.dto.*;
import koredebank.example.bank.security.exceptions.AccountCreationException;
import koredebank.example.bank.security.exceptions.AuthorizationException;
import koredebank.example.bank.security.exceptions.GeneralServiceException;
import koredebank.example.bank.security.exceptions.ImageUploadException;
import koredebank.example.bank.service.transactionService.TransactionServices;
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
public class TransactionController {

    @Autowired
    TransactionServices transacts;


    @GetMapping(ApiRoutes.Transaction+"/check-accBal")
    public ResponseEntity<?> checkAccBalance(@RequestHeader("Authorization")String authentication, @RequestBody UserCheckAccountBalanceRequestDto userCheckAccountBalanceRequestDto) throws AuthorizationException, MessagingException, GeneralServiceException, AccountCreationException {

        return new ResponseEntity<>(transacts.checkAccBalance(userCheckAccountBalanceRequestDto,authentication), HttpStatus.OK);
    }

    @PostMapping(ApiRoutes.Transaction+"/transfer-funds")
    public ResponseEntity<?> transferFunds( @RequestHeader("Authorization")String authentication, @RequestBody UserTransferFundsRequestDto userTransferFundsRequestDto) throws AuthorizationException, MessagingException, GeneralServiceException {

        return new ResponseEntity<>(transacts.transferFunds(userTransferFundsRequestDto,authentication),HttpStatus.OK);

    }

    @PostMapping(ApiRoutes.Transaction+"/deposit-funds")
    public ResponseEntity<?> depositFunds( @RequestHeader("Authorization")String authentication, @RequestBody UserDepositsFundsRequestDto userDepositsFundsRequestDto) throws AuthorizationException, MessagingException, GeneralServiceException {

        return new ResponseEntity<>(transacts.depositFunds(userDepositsFundsRequestDto,authentication),HttpStatus.OK);

    }

    @GetMapping(ApiRoutes.Transaction+"/withdraw-funds")
    public ResponseEntity<?> withdrawFunds( @RequestHeader("Authorization")String authentication, @RequestBody UserWithdrawFundsRequestDto userWithdrawFundsRequestDto) throws AuthorizationException, MessagingException, GeneralServiceException {

        return new ResponseEntity<>(transacts.withdrawFunds(userWithdrawFundsRequestDto,authentication),HttpStatus.OK);
    }

    @PostMapping(ApiRoutes.Transaction+"/user-complaint")
    public ResponseEntity<?> userComplain( @RequestHeader("Authorization")String authentication, @ModelAttribute UserCompliantFormRequestDto userCompliantFormRequestDto) throws ImageUploadException, AuthorizationException, MessagingException, GeneralServiceException {

        return new ResponseEntity<>(transacts.usersCompliant(authentication,userCompliantFormRequestDto),HttpStatus.OK);
    }

    @GetMapping(ApiRoutes.Transaction+"/get-account")
    public ResponseEntity<?> userComplain( String accountNumber)  {

        return new ResponseEntity<>(transacts.getAccount(accountNumber),HttpStatus.OK);
    }

    @GetMapping(ApiRoutes.Transaction+"/list-all-transactions")
    public ResponseEntity<?> listAllTransactions(@RequestHeader("Authorization")String authentication,@RequestParam(value = "page",defaultValue = "1") int page,
                                                 @RequestParam(value = "size",defaultValue = "3") int size) throws AuthorizationException {


        return new ResponseEntity<>(transacts.listTransactions(authentication,page,size), HttpStatus.OK);

    }
}
