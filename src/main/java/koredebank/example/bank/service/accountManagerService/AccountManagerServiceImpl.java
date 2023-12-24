package koredebank.example.bank.service.accountManagerService;

import koredebank.example.bank.Email.EmailService;
import koredebank.example.bank.dto.*;
import koredebank.example.bank.model.*;
import koredebank.example.bank.repository.*;
import koredebank.example.bank.security.exceptions.AccountCreationException;
import koredebank.example.bank.security.exceptions.AuthorizationException;
import koredebank.example.bank.security.exceptions.GeneralServiceException;
import koredebank.example.bank.security.securityServices.UserPrincipalService;
import koredebank.example.bank.serviceUtil.IdGenerator;
import koredebank.example.bank.serviceUtil.StringUtil;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class AccountManagerServiceImpl implements AccountManagerServices {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AccountManagerRepository accountManagerRepository;

    @Autowired
    CustomerCompliantFormRepository customerCompliantFormRepository;

    @Autowired
    TransactionRepository transactionRepository;

    @Autowired
    EmailService emailService;

    @Autowired
    UserAccountRepository userAccountRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserPrincipalService userPrincipalService;



    @Override
    public AccountManagerSignUpResponseDto registerAccountManager(AccountManagerSignUpRequestDto accountManagerSignUpRequestDto) throws AccountCreationException {

        checkAllParameters(accountManagerSignUpRequestDto);

        userWithEmailExists(accountManagerSignUpRequestDto);

       AccountManager accountManager = createAccountMangerDetails(accountManagerSignUpRequestDto);

       accountManagerRepository.save(accountManager);

       return generateRegistrationResponse(accountManagerSignUpRequestDto, accountManager.getId());
    }

    @Override
    public boolean changeAccountManagerPassword(AccountManagerChangePassword accountManagerChangePassword, String id) throws AuthorizationException, GeneralServiceException {

        if(!accountManagerChangePassword.getNewPassword().equals(accountManagerChangePassword.getConfirmPassword())){
            throw new GeneralServiceException("New Passwords do not match");
        }

        Optional<AccountManager> accountManager = accountManagerRepository.findById(id);
        if(accountManager.isEmpty()){
            throw new GeneralServiceException("User not found");
        }

        boolean matches = userPrincipalService.passwordMatches(
                accountManagerChangePassword.getOldPassword(),accountManager.get().getPassword());

        if (!matches) {
            throw new GeneralServiceException("old password is incorrect");
        }else

            accountManager.get().setPassword(passwordEncoder.encode(accountManagerChangePassword.getNewPassword()));
        accountManagerRepository.save(accountManager.get());
        return true;
    }

    @Override
    public boolean forgotAccountManagerPassword(AccountManagerForgotPassword accountManagerForgotPassword) throws GeneralServiceException {

        //check if email is not empty
        if(accountManagerForgotPassword.getEmail().isEmpty()){
            throw new GeneralServiceException("User email is empty");
        }
        if(accountManagerForgotPassword.getNewPassword().isEmpty()){
            throw new GeneralServiceException("Password is empty");
        }
        //find user by email
        Optional<AccountManager> accountManager=accountManagerRepository.findAdminByEmail(accountManagerForgotPassword.getEmail());
        if(accountManager.isEmpty()){
            throw new GeneralServiceException("User not found");
        }

        accountManager.get().setPassword(passwordEncoder.encode(accountManagerForgotPassword.getNewPassword()));

        accountManagerRepository.save(accountManager.get());
        return true;
    }

    @Override
    public boolean blockAccountUser(AccountManagerBlockUserRequestDto accountManagerBlockUserRequestDto) throws GeneralServiceException, MessagingException {
      if (accountManagerBlockUserRequestDto.getAccountNumber().isEmpty()){
          throw new GeneralServiceException("Kindly input the acc number of the user");
      }

      Optional<UserAccount> user = userAccountRepository.findByAccountNumber(accountManagerBlockUserRequestDto.getAccountNumber());

      if(user.isEmpty()){
          throw new GeneralServiceException("User cannot be found");
      }
      user.get().getUserEntity().setEnabled(false);

      userAccountRepository.save(user.get());

      emailService.sendAccountSuspendedNotification(user.get().getUserEntity());

      return true;
    }

    @Override
    public boolean unblockAccountUser(AccountManagerUnblockUserRequestDto accountManagerUnblockUserRequestDto) throws GeneralServiceException, MessagingException {

        if (accountManagerUnblockUserRequestDto.getAccountNumber().isEmpty()){
            throw new GeneralServiceException("Kindly input the acc number of the user");
        }

        Optional<UserAccount> user = userAccountRepository.findByAccountNumber(accountManagerUnblockUserRequestDto.getAccountNumber());

        if(user.isEmpty()){
            throw new GeneralServiceException("User cannot be found");
        }
        user.get().getUserEntity().setEnabled(true);

        userAccountRepository.save(user.get());

        emailService.sendAccountSuspendedRevertNotification(user.get().getUserEntity());

        return true;
    }

    private AccountManagerSignUpResponseDto generateRegistrationResponse(
            AccountManagerSignUpRequestDto accountManagerSignUpRequestDto, String id){

        AccountManagerSignUpResponseDto accountManagerSignUpResponseDto = new AccountManagerSignUpResponseDto();

        modelMapper.map(accountManagerSignUpRequestDto,accountManagerSignUpResponseDto);

        accountManagerSignUpResponseDto.setId(id);
       accountManagerSignUpResponseDto.setRole(Roles.ACCOUNT_MANAGER);

        return accountManagerSignUpResponseDto;
    }

    private AccountManager createAccountMangerDetails(AccountManagerSignUpRequestDto accountManagerSignUpRequestDto){
        AccountManager accountManager = new AccountManager();
        modelMapper.map(accountManagerSignUpRequestDto,accountManager);
        String password=passwordEncoder.encode(accountManagerSignUpRequestDto.getPassword());
        accountManager.setPassword(password);
        accountManager.setId(IdGenerator.generateId());
        accountManager.setRoles(Roles.ACCOUNT_MANAGER);
        return accountManager;
    }

    private void userWithEmailExists(AccountManagerSignUpRequestDto accountManagerSignUpRequestDto) throws AccountCreationException {
        Optional<AccountManager> accountManager =accountManagerRepository.findAdminByEmail(accountManagerSignUpRequestDto.getEmail());
        if(accountManager.isPresent()){
            throw new AccountCreationException("Account Manager with this email already exists");
        }
    }

    private void checkAllParameters(AccountManagerSignUpRequestDto accountManagerSignUpRequestDto) throws AccountCreationException {
        if (StringUtil.isBlank(accountManagerSignUpRequestDto.getEmail())) {
            throw new AccountCreationException("Email Cannot be empty");
        }
        if (StringUtil.isBlank(accountManagerSignUpRequestDto.getPassword())) {
            throw new AccountCreationException("Password Cannot be empty");
        }
        if (StringUtil.isBlank(accountManagerSignUpRequestDto.getConfirmPassword())) {
            throw new AccountCreationException("Confirm Password Cannot be empty");
        }
        if (!(accountManagerSignUpRequestDto.getConfirmPassword().equals(accountManagerSignUpRequestDto.getPassword()))) {
            throw new AccountCreationException("Passwords do not match");
        }
    }


    @Override
    public TransactionListResponseDto listTransactions( int page, int size) throws AuthorizationException {

        //create pageable
        Pageable pageable= PageRequest.of((page-1),size);
        //find all return page
        Page<Transaction> transactions = transactionRepository.findAll(pageable);
        //get total size of list
        int totalSizeOfList=transactionRepository.findAll().size();
        //get the contents from page
        List<Transaction> transactionList= transactions.getContent();
        //create a dto list for contents
        List<TransactionResponseDto> transactionResponseDtoList= new ArrayList<>();

        for (Transaction transaction: transactionList){

            TransactionResponseDto transactionResponseDto = new TransactionResponseDto();
            //map content to dtos
            modelMapper.map(transaction,transactionResponseDto);
            //add dto to dto list
            transactionResponseDtoList.add(transactionResponseDto);
        }
        //create response object
        TransactionListResponseDto transactionListResponseDto = new TransactionListResponseDto();
        //set data into response object
        transactionListResponseDto.setTransactionResponseDtoList(transactionResponseDtoList);
        //set data into response object
        transactionListResponseDto.setSizeOfList(totalSizeOfList);
        //return response object
        return transactionListResponseDto;
    }

    @Override
    public AccountListResponseDto listUsersAccounts( int page, int size) throws AuthorizationException {

        Pageable pageable= PageRequest.of((page-1),size);

        Page<UserAccount> userAccounts = userAccountRepository.findAll(pageable);

        int totalSizeOfList=userAccountRepository.findAll().size();

        List<UserAccount> userAccountList= userAccounts.getContent();

        List<AccountResponseDto> accountResponseDtoList= new ArrayList<>();

        for (UserAccount userAccount: userAccountList){

            AccountResponseDto accountResponseDto = new AccountResponseDto();

            modelMapper.map(userAccount,accountResponseDto);

            accountResponseDtoList.add(accountResponseDto);
        }

        AccountListResponseDto accountListResponseDto = new AccountListResponseDto();

        accountListResponseDto.setAccountResponseDtoList(accountResponseDtoList);

        accountListResponseDto.setSizeOfList(totalSizeOfList);

        return accountListResponseDto;
    }

    @Override
    public UserCompliantListResponseDto listUserCompliantAndSchedules(int page, int size) throws AuthorizationException {

        Pageable pageable= PageRequest.of((page-1),size);

        Page<CustomerCompliantForm> customerCompliantForms = customerCompliantFormRepository.findAll(pageable);

        int totalSizeOfList=customerCompliantFormRepository.findAll().size();

        List<CustomerCompliantForm> customerCompliantFormList= customerCompliantForms.getContent();

        List<UserCompliantFormResponseDto> userCompliantFormResponseDtoList= new ArrayList<>();

        for (CustomerCompliantForm customerCompliantForm: customerCompliantFormList){

            UserCompliantFormResponseDto userCompliantFormResponseDto = new UserCompliantFormResponseDto();

            modelMapper.map(customerCompliantForm,userCompliantFormResponseDto);

            userCompliantFormResponseDtoList.add(userCompliantFormResponseDto);
        }

        UserCompliantListResponseDto userCompliantListResponseDto = new UserCompliantListResponseDto();

        userCompliantListResponseDto.setUserCompliantFormResponseDtoList(userCompliantFormResponseDtoList);

        userCompliantListResponseDto.setSizeOfList(totalSizeOfList);

        return userCompliantListResponseDto;
    }
}
