package koredebank.example.bank.service.accountManagerService;

import koredebank.example.bank.dto.AccountManagerChangePassword;
import koredebank.example.bank.dto.AccountManagerForgotPassword;
import koredebank.example.bank.dto.AccountManagerSignUpRequestDto;
import koredebank.example.bank.dto.AccountManagerSignUpResponseDto;
import koredebank.example.bank.model.AccountManager;
import koredebank.example.bank.model.Roles;
import koredebank.example.bank.repository.AccountManagerRepository;
import koredebank.example.bank.repository.TransactionRepository;
import koredebank.example.bank.repository.UserRepository;
import koredebank.example.bank.security.exceptions.AccountCreationException;
import koredebank.example.bank.security.exceptions.AuthorizationException;
import koredebank.example.bank.security.exceptions.GeneralServiceException;
import koredebank.example.bank.security.securityServices.UserPrincipalService;
import koredebank.example.bank.serviceUtil.IdGenerator;
import koredebank.example.bank.serviceUtil.StringUtil;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AccountManagerServiceImpl implements AccountManagerServices {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AccountManagerRepository accountManagerRepository;

    @Autowired
    TransactionRepository transactionRepository;

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
}
