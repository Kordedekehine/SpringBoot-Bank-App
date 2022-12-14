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

    @PutMapping(ApiRoutes.User+"/change-password")
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
    public ResponseEntity<?> createAccount( @RequestHeader("Authorization")String authentication, @ModelAttribute UserCreateAccountRequestDto userCreateBankAccountRequestDto){
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

    @GetMapping(ApiRoutes.User+"/list-all-transactions")
    public ResponseEntity<?> listAllTransactions(@RequestHeader("Authorization")String authentication,@RequestParam(value = "page",defaultValue = "1") int page,
                                             @RequestParam(value = "size",defaultValue = "3") int size) {
        try {

            return new ResponseEntity<>(userServices.listTransactions(authentication,page,size), HttpStatus.OK);
        } catch (Exception exception) {
            return new ResponseEntity<>(exception.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

}

//{
//  "id": "3ec85cd2-ee67-4d1a-9bd1-08248d7816b9",
//  "firstName": "taiwo",
//  "lastName": "yemisi",
//  "phoneNumber": "07065435621",
//  "email": "salamtaye0@gmail.com",
//  "roles": "BASE_USER",
//  "token": {
//    "accessToken": "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJzYWxhbXRheWUwQGdtYWlsLmNvbSIsInJvbGVzIjoiQkFTRV9VU0VSIiwiaXNzIjoiQVVUT1giLCJpYXQiOjE2NjQ0Mzk3NzUsImV4cCI6MTY2NDUyNjE3NX0.Gcdeqks4I919OKvn9F16AbZWTlCpnFzn4bDP7X0Gw2VTHbdmc2kqZKgF6YISf-R7Rd8L9aj-S8sClkDbnQ7Exg",
//    "tokenType": "BEARER_TOKEN"
//  }
//}
