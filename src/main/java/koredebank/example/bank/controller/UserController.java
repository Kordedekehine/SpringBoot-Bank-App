package koredebank.example.bank.controller;

import koredebank.example.bank.dto.*;
import koredebank.example.bank.service.userService.UserServices;
import koredebank.example.bank.util.ApiRoutes;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping(ApiRoutes.BANK)
public class UserController {

    @Autowired
    UserServices userServices;

    @PostMapping(ApiRoutes.User+"/validate")
    public ResponseEntity<?> Registration(@RequestBody UserLoginValidationRequestDto userLoginValidationRequestDto){
        try{
            return new ResponseEntity<>(userServices.validateUserAccount(userLoginValidationRequestDto), HttpStatus.OK);
        }
        catch (Exception exception){
            return new ResponseEntity<>(exception.getMessage(),HttpStatus.BAD_REQUEST);
        }
    }


    @PostMapping(ApiRoutes.User+"/signUp")
    public ResponseEntity<?> Registration(@RequestBody UserSignUpRequestDto userSignUpRequestDto){
        log.info(userSignUpRequestDto.toString());
        try{
            return new ResponseEntity<>(userServices.signUp(userSignUpRequestDto),HttpStatus.OK);
        }
        catch (Exception exception){
            return new ResponseEntity<>(exception.getMessage(),HttpStatus.BAD_REQUEST);
        }
    }


    @PostMapping(ApiRoutes.User+"/login")
    public ResponseEntity<?> SignIn(@RequestBody UserLoginDto userLoginDto){
        try{
            return new ResponseEntity<>(userServices.login(userLoginDto),HttpStatus.OK);
        }
        catch (Exception exception){
            return new ResponseEntity<>(exception.getMessage(),HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping(ApiRoutes.User+"/validateTokenAndPassword")
    public ResponseEntity<?> RetrievePassword(@RequestBody UserRetrieveForgotPasswordRequestDto userRetrieveForgotPasswordRequestDto){
        try{
            //entry point
            return new ResponseEntity<>(userServices.validateForgotTokenAndNewForgotPassword(userRetrieveForgotPasswordRequestDto),HttpStatus.OK);
        }
        catch (Exception exception){
            return new ResponseEntity<>(exception.getMessage(),HttpStatus.BAD_REQUEST);
        }
    }


    @PostMapping(ApiRoutes.User+"/change-password")
    public ResponseEntity<?> ChangePassword(@RequestHeader("Authorization")String authentication, @RequestBody UserChangePasswordRequestDto userChangePasswordRequestDto){
        try{
            return new ResponseEntity<>(userServices.userChangePassword(userChangePasswordRequestDto ,authentication),HttpStatus.OK);
        }
        catch (Exception exception){
            return new ResponseEntity<>(exception.getMessage(),HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping(ApiRoutes.User+"/forgot-password")
    public ResponseEntity<?> forgotPassword(UserForgotPasswordRequestDto forgotPasswordRequestDto){
        try {
            return new ResponseEntity<>(userServices.userForgotPassword(forgotPasswordRequestDto),HttpStatus.OK);
        }
        catch (Exception exception){
            return new ResponseEntity<>(exception.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping(ApiRoutes.User+"/create-account")
    public ResponseEntity<?> createAccount( @RequestHeader("Authorization")String authentication, @RequestBody UserCreateAccountRequestDto userCreateBankAccountRequestDto){
        try{
            return new ResponseEntity<>(userServices.createAccounts(userCreateBankAccountRequestDto,authentication),HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(),HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping(ApiRoutes.User+"/check-accBal")
    public ResponseEntity<?> checkAccBalance( @RequestHeader("Authorization")String authentication, @RequestBody UserCheckAccountBalanceRequestDto userCheckAccountBalanceRequestDto){
        try{
            return new ResponseEntity<>(userServices.checkAccBalance(userCheckAccountBalanceRequestDto,authentication),HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(),HttpStatus.BAD_REQUEST);
        }
    }


    @PostMapping(ApiRoutes.User+"/transfer-funds")
    public ResponseEntity<?> transferFunds( @RequestHeader("Authorization")String authentication, @RequestBody UserTransferFundsRequestDto userTransferFundsRequestDto){
        try{
            return new ResponseEntity<>(userServices.transferFunds(userTransferFundsRequestDto,authentication),HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(),HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping(ApiRoutes.User+"/deposit-funds")
    public ResponseEntity<?> depositFunds( @RequestHeader("Authorization")String authentication, @RequestBody UserDepositsFundsRequestDto userDepositsFundsRequestDto){
        try{
            return new ResponseEntity<>(userServices.depositFunds(userDepositsFundsRequestDto,authentication),HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(),HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping(ApiRoutes.User+"/withdraw-funds")
    public ResponseEntity<?> withdrawFunds( @RequestHeader("Authorization")String authentication, @RequestBody UserWithdrawFundsRequestDto userWithdrawFundsRequestDto){
        try{
            return new ResponseEntity<>(userServices.withdrawFunds(userWithdrawFundsRequestDto,authentication),HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(),HttpStatus.BAD_REQUEST);
        }
    }


    @PostMapping(ApiRoutes.User+"/user-complaint")
    public ResponseEntity<?> userComplain( @RequestHeader("Authorization")String authentication, @ModelAttribute UserCompliantFormRequestDto userCompliantFormRequestDto){
        try{
            return new ResponseEntity<>(userServices.usersCompliant(authentication,userCompliantFormRequestDto),HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(),HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping(ApiRoutes.User+"/transaction-history")
    public ResponseEntity<?> transactionHistory( @RequestHeader("Authorization")String authentication){
        try{
            return new ResponseEntity<>(userServices.getAllTransactionHistory(authentication),HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(),HttpStatus.BAD_REQUEST);
        }
    }

}
