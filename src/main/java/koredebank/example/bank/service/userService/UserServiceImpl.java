package koredebank.example.bank.service.userService;

import koredebank.example.bank.Email.EmailService;
import koredebank.example.bank.dto.*;
import koredebank.example.bank.model.*;
import koredebank.example.bank.payload.UserAccountGeneratorDto;
import koredebank.example.bank.repository.*;
import koredebank.example.bank.security.exceptions.AccountCreationException;
import koredebank.example.bank.security.exceptions.AuthorizationException;
import koredebank.example.bank.security.exceptions.GeneralServiceException;
import koredebank.example.bank.security.exceptions.IncorrectPasswordException;
import koredebank.example.bank.security.securityServices.UserPrincipalService;
import koredebank.example.bank.security.securityUtils.JWTToken;
import koredebank.example.bank.serviceUtil.IdGenerator;
import koredebank.example.bank.serviceUtil.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import java.time.*;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class UserServiceImpl implements UserServices {

    @Autowired
   private UserRepository userRepository;

    @Autowired
    UserAccountRepository userAccountRepository;

    @Autowired
    TransactionRepository transactionRepository;

    @Autowired
     CustomerCompliantFormRepository compliantFormRepository;

    @Autowired
   private ModelMapper modelMapper;

    @Autowired
   private PasswordEncoder passwordEncoder;

    @Autowired
    EmailService emailService;

    @Autowired
    UserPrincipalService userPrincipalService;

    @Override
    public UserLoginResponseDto login(UserLoginDto userLoginDto) throws IncorrectPasswordException, GeneralServiceException {
        UserLoginResponseDto userLoginResponseDto=new UserLoginResponseDto();
        JWTToken jwtToken= userPrincipalService.loginUser(userLoginDto);
        if(jwtToken!=null){
            Optional<User> usersEntity=userRepository.findUserByEmail(userLoginDto.getEmail());
            if(usersEntity.isPresent()){
                if(!usersEntity.get().getEnabled()){
                    throw new GeneralServiceException("User account has not been activated");
                }
            }
            usersEntity.ifPresent(entity -> modelMapper.map(entity, userLoginResponseDto));
            userLoginResponseDto.setToken(jwtToken);
        }
        return userLoginResponseDto;

    }

    @Override
    public UserSignUpResponseDto signUp(UserSignUpRequestDto userSignUpRequestDto) throws GeneralServiceException, AccountCreationException, MessagingException {
        checkAllParameters(userSignUpRequestDto);

        userWithEmailExists(userSignUpRequestDto);

        User users= createUserEntityFromDetails(userSignUpRequestDto);

        String confirmation=userPrincipalService.sendRegistrationToken(users);

        users.setValidationToken(confirmation);

        emailService.sendRegistrationSuccessfulEmail(users, confirmation);

        userRepository.save(users);

        return generateRegistrationResponse(userSignUpRequestDto, users.getId());
    }

    @Override
    public boolean validateUserAccount(UserLoginValidationRequestDto userLoginValidationRequestDto) throws AccountCreationException, GeneralServiceException, MessagingException {
        if(StringUtil.isBlank(userLoginValidationRequestDto.getEmail())){
            throw new GeneralServiceException("Email cannot be empty");
        }
        Optional<User> usersEntity=userRepository.findUserByEmail(userLoginValidationRequestDto.getEmail());
        if(usersEntity.isPresent()){
            if(usersEntity.get().getValidationToken().equals(userLoginValidationRequestDto.getToken())){
                usersEntity.get().setEnabled(true);
                usersEntity.get().setValidationToken(null);

                log.info(usersEntity.get().getPassword());
                System.out.println(usersEntity.get().getPassword());
                emailService.sendVerificationMessage(usersEntity);
                userRepository.save(usersEntity.get());
                return true;
            }else {
                throw new GeneralServiceException("Invalid validation token");
            }
        }
        throw new GeneralServiceException("User With this email does not exist");
    }

    @Override
    public boolean userChangePassword(UserChangePasswordRequestDto userChangePasswordRequestDto, String authentication) throws AuthorizationException, GeneralServiceException, MessagingException {

        String userEmail = userPrincipalService.getUserEmailAddressFromToken(authentication);

        if (!userChangePasswordRequestDto.getNewPassword().equals(userChangePasswordRequestDto.getConfirmPassword())) {
            throw new GeneralServiceException("New Passwords do not match");
        }

        Optional<User> user = userRepository.findUserByEmail(userEmail);
        if (user.isEmpty()) {
            throw new GeneralServiceException("User not found");
        }

        boolean matches = userPrincipalService.passwordMatches(
                userChangePasswordRequestDto.getOldPassword(),user.get().getPassword());

        if (!matches) {
            throw new GeneralServiceException("Old password is incorrect");
        }else

            user.get().setPassword(passwordEncoder.encode(userChangePasswordRequestDto.getNewPassword()));

        emailService.sendChangePasswordMessage(Optional.of(user.get()));

        userRepository.save(user.get());

        return true;
    }

    @Override
    public boolean userForgotPassword(UserForgotPasswordRequestDto userForgotPasswordRequestDto) throws GeneralServiceException, MessagingException, AuthorizationException {
        if(StringUtil.isBlank(userForgotPasswordRequestDto.getEmail())){
            throw new GeneralServiceException("Email cannot be empty!");
        }
        Optional<User> optionalBaseUser = userRepository.findUserByEmail(userForgotPasswordRequestDto.getEmail());
        if (optionalBaseUser.isEmpty()) {
            throw new GeneralServiceException("No such user exists!");
        }

        String confirmation=userPrincipalService.sendRecoveryToken(optionalBaseUser.get());

        optionalBaseUser.get().setValidationToken(confirmation);


        emailService.sendForgotPasswordMessage(optionalBaseUser.get(),confirmation);
        //persist
        userRepository.save(optionalBaseUser.get());
        return true;
    }

    @Override
    public boolean validateForgotTokenAndNewForgotPassword(UserRetrieveForgotPasswordRequestDto userRetrieveForgotPasswordRequestDto) throws GeneralServiceException, MessagingException, AuthorizationException {

        if(StringUtil.isBlank(userRetrieveForgotPasswordRequestDto.getEmail())){
            throw new GeneralServiceException("Email cannot be empty");
        }
        if (!userRetrieveForgotPasswordRequestDto.getNewPassword().equals(userRetrieveForgotPasswordRequestDto.getConfirmPassword())) {
            throw new GeneralServiceException("New Passwords do not match");
        }
        Optional<User> users = userRepository.findUserByEmail(userRetrieveForgotPasswordRequestDto.getEmail());
        if (users.isPresent()){
            if (users.get().getValidationToken().equals(userRetrieveForgotPasswordRequestDto.getToken())){
                users.get().setEnabled(true);
                users.get().setValidationToken(null);

                users.get().setPassword(passwordEncoder.encode(userRetrieveForgotPasswordRequestDto.getNewPassword()));

                emailService.sendVerificationMessage(users);
                userRepository.save(users.get());
                return true;
            }else {
                throw new GeneralServiceException("Invalid validation token");
            }
        }
        throw new GeneralServiceException("User With this email does not exist");
    }

    @Override
    public UserCreateAccountResponseDto createAccounts(UserCreateAccountRequestDto userCreateAccountRequestDto,String authentication) throws AccountCreationException, AuthorizationException, MessagingException {

        String userEmail = userPrincipalService.getUserEmailAddressFromToken(authentication);

        Optional<User> user = userRepository.findUserByEmail(userEmail);
        if (user.isEmpty()) {
            throw new AccountCreationException("User not found");
        }

        if (userCreateAccountRequestDto.getBankName() == null && userCreateAccountRequestDto.getOwnerName() == null) {
            throw new AccountCreationException("Account cannot be empty");
        }

      UserAccount userAccount = createAccounts(userCreateAccountRequestDto.getBankName(),userCreateAccountRequestDto.getOwnerName());
       userAccount.setUser(user.get());
        //TODO you can't use same email to open two account

        modelMapper.map(userCreateAccountRequestDto,userAccount);

        emailService.sendAccountCreationSuccessfulEmail(userAccount);

        userAccountRepository.save(userAccount);

        log.info(userAccount.toString());
       UserCreateAccountResponseDto userCreateAccountResponseDto = new UserCreateAccountResponseDto();

       modelMapper.map(userCreateAccountRequestDto,userCreateAccountResponseDto);
       return userCreateAccountResponseDto;
    }


    private UserSignUpResponseDto generateRegistrationResponse(UserSignUpRequestDto userSignUpRequestDto,String id){
        UserSignUpResponseDto userSignUpResponseDto=new UserSignUpResponseDto();

        modelMapper.map(userSignUpRequestDto,userSignUpResponseDto);

        userSignUpResponseDto.setId(id);
        userSignUpResponseDto.setRole(Roles.BASE_USER);

        return userSignUpResponseDto;
    }

    private User createUserEntityFromDetails(UserSignUpRequestDto userSignUpRequestDto){
        User user= new User();
        modelMapper.map(userSignUpRequestDto,user);
        String password=passwordEncoder.encode(userSignUpRequestDto.getPassword());
        user.setPassword(password);
        user.setId(IdGenerator.generateId());
        user.setRoles(Roles.BASE_USER);
        return user;
    }

    private void checkAllParameters(UserSignUpRequestDto userSignUpRequestDto) throws AccountCreationException {

        if(StringUtil.isBlank(userSignUpRequestDto.getFirstName())){
            throw new AccountCreationException("FirstName Cannot be empty");
        }
        if(StringUtil.isBlank(userSignUpRequestDto.getLastName())){
            throw new AccountCreationException("LastName Cannot be empty");
        }
        if(StringUtil.isBlank(userSignUpRequestDto.getPhoneNumber())){
            throw new AccountCreationException("Phone number Cannot be empty");
        }
        if(StringUtil.isBlank(userSignUpRequestDto.getEmail())){
            throw new AccountCreationException("Email Cannot be empty");
        }
        if(StringUtil.isBlank(userSignUpRequestDto.getPassword())){
            throw new AccountCreationException("Password Cannot be empty");
        }
        if(StringUtil.isBlank(userSignUpRequestDto.getConfirmPassword())){
            throw new AccountCreationException("Confirm Password Cannot be empty");
        }
        if(!(userSignUpRequestDto.getConfirmPassword().equals(userSignUpRequestDto.getPassword()))){
            throw new AccountCreationException("Passwords do not match");
        }

    }
    private void userWithEmailExists(UserSignUpRequestDto userSignUpRequestDto) throws AccountCreationException {

        Optional<User> userByEmail = userRepository.findUserByEmail(userSignUpRequestDto.getEmail());
        if(userByEmail.isPresent()){
            throw new AccountCreationException("Users with this email already exists");
        }

    }

    @Override
    public UserCheckAccountBalanceResponseDto checkAccBalance(UserCheckAccountBalanceRequestDto userCheckAccountBalanceRequestDto, String authentication) throws AccountCreationException, AuthorizationException, MessagingException, GeneralServiceException {

        String userEmail = userPrincipalService.getUserEmailAddressFromToken(authentication);

        Optional<User> user = userRepository.findUserByEmail(userEmail);
        if (user.isEmpty()) {
            throw new AccountCreationException("User not found");
        }

      UserAccount userAccount = getAccountByDate(userCheckAccountBalanceRequestDto.getSortCode(),userCheckAccountBalanceRequestDto.getAccountNumber());


        if (userAccount == null){
            throw new AccountCreationException("Kindly fill in all the required details to create account");
        }

        modelMapper.map(userAccount, userCheckAccountBalanceRequestDto);

        userAccountRepository.save(userAccount);

        UserCheckAccountBalanceResponseDto userCheckAccountBalanceResponseDto = new UserCheckAccountBalanceResponseDto();

        modelMapper.map(userCheckAccountBalanceRequestDto,userCheckAccountBalanceResponseDto);

        return userCheckAccountBalanceResponseDto;
    }

    @Override
    public boolean transferFunds(UserTransferFundsRequestDto userTransferFundsRequestDto, String authentication) throws AuthorizationException, GeneralServiceException, MessagingException {

        String userEmail = userPrincipalService.getUserEmailAddressFromToken(authentication);

        Optional<User> user = userRepository.findUserByEmail(userEmail);
        if (user.isEmpty()) {
            throw new AuthorizationException("User not found");
        }


        Optional<UserAccount> sourceAccountNumberAndSortCode = userAccountRepository.
                findBySortCodeAndAccountNumber(userTransferFundsRequestDto.getSourceAccount().getSortCode(),
                        userTransferFundsRequestDto.getSourceAccount().getAccountNumber());

       Optional<UserAccount> targetAccountNumberAndSortCode = userAccountRepository.
               findBySortCodeAndAccountNumber(userTransferFundsRequestDto.getTargetAccount().getSortCode(),
                       userTransferFundsRequestDto.getTargetAccount().getAccountNumber());

               if (sourceAccountNumberAndSortCode.isPresent() && targetAccountNumberAndSortCode.isPresent()){
                 isAmountAvailable(userTransferFundsRequestDto.getAmount(),sourceAccountNumberAndSortCode.get()
                         .getCurrentBalance());
               }

               Transaction transaction = new Transaction();

        transaction.setAmount(userTransferFundsRequestDto.getAmount());
        transaction.setSourceAccountId(sourceAccountNumberAndSortCode.get().getId());
        transaction.setTargetAccountId(targetAccountNumberAndSortCode.get().getId());
        transaction.setTargetOwnerName(targetAccountNumberAndSortCode.get().getOwnerName());
        transaction.setInitiationDate(LocalDateTime.now());
        transaction.setCompletionDate(LocalDateTime.now());
        transaction.setReference(userTransferFundsRequestDto.getReference());
        transaction.setTargetEmail(userTransferFundsRequestDto.getTargetEmail());
        transaction.setUser(user.get());

       updateAccountBalance(sourceAccountNumberAndSortCode.get(),userTransferFundsRequestDto.getAmount(),
              Usage.WITHDRAW);

       updateAccountBalance(targetAccountNumberAndSortCode.get(),userTransferFundsRequestDto.getAmount(),
               Usage.DEPOSIT);

       emailService.sendTransactionSuccessfulMessage(transaction,user.get());

       emailService.sendAlertReceivedMessage(transaction);
       transactionRepository.save(transaction);
       log.info(transaction.toString());

       return true;

    }

    @Override
    public boolean depositFunds(UserDepositsFundsRequestDto userDepositsFundsRequestDto, String authentication) throws AuthorizationException, GeneralServiceException, MessagingException {

        String userEmail = userPrincipalService.getUserEmailAddressFromToken(authentication);

        Optional<User> user = userRepository.findUserByEmail(userEmail);
        if (user.isEmpty()) {
            throw new AuthorizationException("User not found");
        }


        Optional<UserAccount> account = userAccountRepository.findByAccountNumber
                (userDepositsFundsRequestDto.getTargetAccountNo());

        if (account.isEmpty()){
            throw new GeneralServiceException("Account does not exist");
        }

        updateAccountBalance(account.get(),userDepositsFundsRequestDto.getAmount(),Usage.DEPOSIT);

        emailService.sendDepositSuccessfulMessage(user.get(),account.get());

      return true;
    }

    @Override
    public boolean withdrawFunds(UserWithdrawFundsRequestDto userWithdrawFundsRequestDto, String authentication) throws AuthorizationException, GeneralServiceException, MessagingException {

        String userEmail = userPrincipalService.getUserEmailAddressFromToken(authentication);

        Optional<User> user = userRepository.findUserByEmail(userEmail);
        if (user.isEmpty()) {
            throw new AuthorizationException("User not found");
        }

        UserAccount account = getAccountByDate(userWithdrawFundsRequestDto.getSortCode(),
                userWithdrawFundsRequestDto.getAccountNumber());

        if (account == null){
            throw new GeneralServiceException("Account does not exist");
        }

      isAmountAvailable(userWithdrawFundsRequestDto.getAmount(),account.getCurrentBalance());
        updateAccountBalance(account,userWithdrawFundsRequestDto.getAmount(), Usage.WITHDRAW);

        emailService.sendWithdrawSuccessfulMessage(user.get(),account);

        return true;
    }

    @Override
    public UserCompliantFormResponseDto usersCompliant(String loginToken,UserCompliantFormRequestDto userCompliantFormRequestDto) throws AuthorizationException, GeneralServiceException, MessagingException {


        String userEmail=userPrincipalService.getUserEmailAddressFromToken(loginToken);

       CustomerCompliantForm customerCompliantForm = new CustomerCompliantForm();

        Optional<User> user = userRepository.findUserByEmail(userEmail);

        modelMapper.map(userCompliantFormRequestDto,customerCompliantForm);

        if(user.isEmpty()) {
            throw new GeneralServiceException("Bank user must create an account before scheduling a section");
        }
         customerCompliantForm.setUser(user.get());

         customerCompliantForm.setTitle(userCompliantFormRequestDto.getTitle());
         customerCompliantForm.setDescription(userCompliantFormRequestDto.getDescription());
         customerCompliantForm.setModeOfMeeting(userCompliantFormRequestDto.getModeOfMeeting());

        ZoneId zone = ZoneId.of("Africa/Lagos");

        Instant now = Instant.now(Clock.system(zone));
       Instant expirationTime = now.plus(Duration.ofDays(userCompliantFormRequestDto.getCustomerCompliantDaysLength().getDays()))
               .plus(Duration.ofHours(userCompliantFormRequestDto.getCustomerCompliantDaysLength().getHours()));

       customerCompliantForm.setExpirationDate(expirationTime);

       //email here
        emailService.sendCompliantNotification(customerCompliantForm);

       compliantFormRepository.save(customerCompliantForm);

       UserCompliantFormResponseDto userCompliantFormResponseDto = new UserCompliantFormResponseDto();

       modelMapper.map(userCompliantFormRequestDto,userCompliantFormResponseDto);

       return userCompliantFormResponseDto;
    }

    @Override
    public List<Transaction> getAllTransactionHistory(String loginToken) throws AuthorizationException, GeneralServiceException {

        String userEmail=userPrincipalService.getUserEmailAddressFromToken(loginToken);

        Optional<User> user = userRepository.findUserByEmail(userEmail);

        if(user.isEmpty()) {
            throw new GeneralServiceException("Bank user must create an account before scheduling a section");
        }

        UserAccount userAccount = new UserAccount();

      return userAccount.getTransactionList();
    }


    public UserAccount createAccounts(String bankName, String ownerName) {
        UserAccountGeneratorDto codeGenerator = new UserAccountGeneratorDto();
        UserAccount newAccount = new UserAccount(bankName, ownerName, codeGenerator.generateSortCode(), codeGenerator.generateAccountNumber(), 0.00);
      //  Maybe i need to map it
        return newAccount;
    }

    public UserAccount getAccountByDate(String sortCode, String accountNumber) throws GeneralServiceException {
        Optional<UserAccount> account = userAccountRepository
                .findBySortCodeAndAccountNumber(sortCode, accountNumber);

        account.ifPresent(value ->
                value.setTransactionList(transactionRepository
                        .findBySourceAccountIdOrderByInitiationDate(value.getId())));

       // return account.orElse(null);

        if (account.isEmpty()){
            throw new GeneralServiceException("Account is null");
        }
        return account.get();
    }

    public UserAccount getAccount(String accountNumber) {
        Optional<UserAccount> account = userAccountRepository
                .findByAccountNumber(accountNumber);

        return account.orElse(null);
    }



    public void updateAccountBalance(UserAccount account, double amount, Usage action) {
        if (action == Usage.WITHDRAW) {
            account.setCurrentBalance((account.getCurrentBalance() - amount));
        } else if (action == Usage.DEPOSIT) {
            account.setCurrentBalance((account.getCurrentBalance() + amount));
        }
        userAccountRepository.save(account);
        log.info(account.toString());
    }

    public boolean isAmountAvailable(double amount, double accountBalance) {
        return (accountBalance - amount) > 0;
    }
}
//{
//  "id": "5e06d64b-c3bc-47f1-881c-9613002170c7",
//  "firstName": "taiwo",
//  "lastName": "salami",
//  "phoneNumber": "08067543218",
//  "email": "salamtaye0@gmail.com",
//  "roles": "BASE_USER",
//  "token": {
//    "accessToken": "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJzYWxhbXRheWUwQGdtYWlsLmNvbSIsInJvbGVzIjoiQkFTRV9VU0VSIiwiaXNzIjoiQVVUT1giLCJpYXQiOjE2NjMzMjE5MzcsImV4cCI6MTY2MzQwODMzN30.1IntVF7FJ2V76LYB8R7diNYuTxislPfUHZPNH5TsVk4aGXVgsA0MAgBl0QE1cwx4Hf1wzJP3muGVoGUysDIjAQ",
//    "tokenType": "BEARER_TOKEN"
//  }
//}



//{
//  "id": "5acf90a6-400d-4835-9f86-0027a7647c9c",
//  "firstName": "esther",
//  "lastName": "salami",
//  "phoneNumber": "08045672341",
//  "email": "abdulsalamesther345@gmail.com",
//  "roles": "BASE_USER",
//  "token": {
//    "accessToken": "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJhYmR1bHNhbGFtZXN0aGVyMzQ1QGdtYWlsLmNvbSIsInJvbGVzIjoiQkFTRV9VU0VSIiwiaXNzIjoiQVVUT1giLCJpYXQiOjE2NjMzMjM5NjMsImV4cCI6MTY2MzQxMDM2M30.fP-MnZimsXG_Qo5mVctqHaqd2f8PRQxBwOo1nHHNFcokP4tq7crtPZ4HvQ2DTJJ-PN34krn_-fjujrSlfR1fbQ",
//    "tokenType": "BEARER_TOKEN"
//  }
//}

//{
//  "id": "14bbd6f1-a6da-4c17-b6ea-4e048cee7559",
//  "firstName": "esther",
//  "lastName": "rukayat",
//  "phoneNumber": "08076542133",
//  "email": "abdulsalamesther345@gmail.com",
//  "roles": "BASE_USER",
//  "token": {
//    "accessToken": "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJhYmR1bHNhbGFtZXN0aGVyMzQ1QGdtYWlsLmNvbSIsInJvbGVzIjoiQkFTRV9VU0VSIiwiaXNzIjoiQVVUT1giLCJpYXQiOjE2NjQ0NDA5MzMsImV4cCI6MTY2NDUyNzMzM30.ji4BkBkvYUkDy5WTM22Z2ZrtjUz1e0mBQVMeudzTxy2noqEPsVRCbPbJA1yrXObngt4XDTtdbfOEI7kG6PqjaQ",
//    "tokenType": "BEARER_TOKEN"
//  }
//}