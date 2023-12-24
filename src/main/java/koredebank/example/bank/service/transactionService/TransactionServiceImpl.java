package koredebank.example.bank.service.transactionService;

import koredebank.example.bank.Email.EmailService;
import koredebank.example.bank.cloudinaryService.CloudStorageService;
import koredebank.example.bank.dto.*;
import koredebank.example.bank.model.*;
import koredebank.example.bank.payload.UserAccountGeneratorDto;
import koredebank.example.bank.repository.CustomerCompliantFormRepository;
import koredebank.example.bank.repository.TransactionRepository;
import koredebank.example.bank.repository.UserAccountRepository;
import koredebank.example.bank.repository.UserRepository;
import koredebank.example.bank.security.exceptions.AccountCreationException;
import koredebank.example.bank.security.exceptions.AuthorizationException;
import koredebank.example.bank.security.exceptions.GeneralServiceException;
import koredebank.example.bank.security.exceptions.ImageUploadException;
import koredebank.example.bank.security.securityServices.UserPrincipalService;
import koredebank.example.bank.serviceUtil.IdGenerator;
import koredebank.example.bank.serviceUtil.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.mail.MessagingException;
import java.io.IOException;
import java.time.*;
import java.util.*;


@Service
@Slf4j
public class TransactionServiceImpl implements TransactionServices{


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
    CloudStorageService cloudStorageService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    EmailService emailService;

    @Autowired
    UserPrincipalService userPrincipalService;



    private static final String USER_NOT_FOUND = "User not found";

    @Override
    public UserCreateAccountResponseDto createAccounts(UserCreateAccountRequestDto userCreateAccountRequestDto, String authentication) throws AccountCreationException, AuthorizationException, MessagingException {

        String userEmail = userPrincipalService.getUserEmailAddressFromToken(authentication);

        Optional<UserEntity> user = userRepository.findUserByEmail(userEmail);
        if (user.isEmpty()) {
            throw new AccountCreationException(USER_NOT_FOUND);
        }

        if (userCreateAccountRequestDto.getBankName() == null && userCreateAccountRequestDto.getOwnerName() == null) {
            throw new AccountCreationException("Account cannot be empty");
        }

        UserAccount userAccount = createAccounts(userCreateAccountRequestDto.getBankName(),userCreateAccountRequestDto.getOwnerName());
        userAccount.setUserEntity(user.get());

        modelMapper.map(userCreateAccountRequestDto,userAccount);

         emailService.sendAccountCreationSuccessfulEmail(userAccount);

        userAccountRepository.save(userAccount);

        log.info(userAccount.toString());
        UserCreateAccountResponseDto userCreateAccountResponseDto = new UserCreateAccountResponseDto();

        modelMapper.map(userCreateAccountRequestDto,userCreateAccountResponseDto);
        return userCreateAccountResponseDto;
    }


    private UserSignUpResponseDto generateRegistrationResponse(UserSignUpRequestDto userSignUpRequestDto, String id){
        UserSignUpResponseDto userSignUpResponseDto=new UserSignUpResponseDto();

        modelMapper.map(userSignUpRequestDto,userSignUpResponseDto);

        userSignUpResponseDto.setId(id);
        userSignUpResponseDto.setRole(Roles.BASE_USER);

        return userSignUpResponseDto;
    }


    @Override
    public UserCheckAccountBalanceResponseDto checkAccBalance(UserCheckAccountBalanceRequestDto userCheckAccountBalanceRequestDto, String authentication) throws AccountCreationException, AuthorizationException, MessagingException, GeneralServiceException {

        String userEmail = userPrincipalService.getUserEmailAddressFromToken(authentication);

        Optional<UserEntity> user = userRepository.findUserByEmail(userEmail);
        if (user.isEmpty()) {
            throw new AccountCreationException(USER_NOT_FOUND);
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

        Optional<UserEntity> user = userRepository.findUserByEmail(userEmail);
        if (user.isEmpty()) {
            throw new AuthorizationException(USER_NOT_FOUND);
        }

        Optional<UserAccount> sourceAccountNumberAndSortCode = userAccountRepository.
                findBySortCodeAndAccountNumber(userTransferFundsRequestDto.getSourceAccount().getSortCode(),
                        userTransferFundsRequestDto.getSourceAccount().getAccountNumber());

        Optional<UserAccount> targetAccountNumberAndSortCode = userAccountRepository.
                findBySortCodeAndAccountNumber(userTransferFundsRequestDto.getTargetAccount().getSortCode(),
                        userTransferFundsRequestDto.getTargetAccount().getAccountNumber());

        if (sourceAccountNumberAndSortCode.isPresent() || !sourceAccountNumberAndSortCode.isEmpty()
                && targetAccountNumberAndSortCode.isPresent() || !targetAccountNumberAndSortCode.isEmpty()){
            throw new GeneralServiceException("source and target account number does not exist");
        }

        UserAccount accountSourceNumAndCode = sourceAccountNumberAndSortCode.get();
        UserAccount accountTargetNumAndCode = targetAccountNumberAndSortCode.get();

        isAmountAvailable(userTransferFundsRequestDto.getAmount(), accountSourceNumAndCode.getCurrentBalance());

        Transaction transaction = new Transaction();

        transaction.setAmount(userTransferFundsRequestDto.getAmount());
        transaction.setSourceAccountId(accountSourceNumAndCode.getId());
        transaction.setTargetAccountId(accountTargetNumAndCode.getId());
        transaction.setTargetOwnerName(accountTargetNumAndCode.getOwnerName());
        transaction.setInitiationDate(LocalDateTime.now());
        transaction.setCompletionDate(LocalDateTime.now());
        transaction.setReference(userTransferFundsRequestDto.getReference());
        transaction.setTargetEmail(userTransferFundsRequestDto.getTargetEmail());
        transaction.setUserEntity(user.get());

        updateAccountBalance(sourceAccountNumberAndSortCode.get(),userTransferFundsRequestDto.getAmount(),
                Usage.TRANSFER);

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

        Optional<UserEntity> user = userRepository.findUserByEmail(userEmail);
        if (user.isEmpty()) {
            throw new AuthorizationException(USER_NOT_FOUND);
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

        Optional<UserEntity> user = userRepository.findUserByEmail(userEmail);
        if (user.isEmpty()) {
            throw new AuthorizationException(USER_NOT_FOUND);
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
    public UserCompliantFormResponseDto usersCompliant(String loginToken,UserCompliantFormRequestDto userCompliantFormRequestDto) throws AuthorizationException, GeneralServiceException, MessagingException, ImageUploadException {

        String userEmail=userPrincipalService.getUserEmailAddressFromToken(loginToken);

        CustomerCompliantForm customerCompliantForm = new CustomerCompliantForm();

        Optional<UserEntity> user = userRepository.findUserByEmail(userEmail);

        modelMapper.map(userCompliantFormRequestDto,customerCompliantForm);

        if(user.isEmpty()) {
            throw new GeneralServiceException("Bank user must create an account before scheduling a section");
        }
        customerCompliantForm.setUser(user.get());

        customerCompliantForm.setTitle(userCompliantFormRequestDto.getTitle());
        customerCompliantForm.setDescription(userCompliantFormRequestDto.getDescription());
        customerCompliantForm.setModeOfMeeting(userCompliantFormRequestDto.getModeOfMeeting());
        customerCompliantForm.setImage1(imageUrlFromCloudinary(userCompliantFormRequestDto.getImage1()));
        customerCompliantForm.setImage2(imageUrlFromCloudinary(userCompliantFormRequestDto.getImage2()));
        customerCompliantForm.setImage3(imageUrlFromCloudinary(userCompliantFormRequestDto.getImage3()));

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
    public TransactionListResponseDto listTransactions(String loginToken, int page, int size) throws AuthorizationException {

        //confirmUserToken
        userPrincipalService.getUserEmailAddressFromToken(loginToken);
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

    private String imageUrlFromCloudinary(MultipartFile image) throws ImageUploadException {
        String imageUrl="";
        if(image!=null && !image.isEmpty()){
            Map<Object,Object> params=new HashMap<>();
            params.put("public_id","BANK/"+extractFileName(image.getName()));
            params.put("overwrite",true);

            try{
                Map<?,?> uploadResult = cloudStorageService.uploadImage(image,params);
                imageUrl= String.valueOf(uploadResult.get("url"));
            }catch (IOException e){
                e.printStackTrace();
                throw new ImageUploadException("Error uploading images,file upload failed");
            }
        }
        return imageUrl;
    }

    private String extractFileName(String fileName){
        return fileName.split("\\.")[0];
    }

    public UserAccount createAccounts(String bankName, String ownerName) {
        UserAccountGeneratorDto codeGenerator = new UserAccountGeneratorDto();
        UserAccount newAccount = new UserAccount(bankName, ownerName, codeGenerator.generateSortCode(), codeGenerator.generateAccountNumber(), 0.00);
        //  Maybe i need to map it
        return newAccount;
    }

    private UserAccount getAccountByDate(String sortCode, String accountNumber) throws GeneralServiceException {
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

    @Override
    public UserAccount getAccount(String accountNumber) {
        Optional<UserAccount> account = userAccountRepository
                .findByAccountNumber(accountNumber);

        return account.orElse(null);
    }



    public void updateAccountBalance(UserAccount account, double amount, Usage action) {
        if (action == Usage.TRANSFER){
            account.setCurrentBalance(account.getCurrentBalance() - amount - 10);
            //10 naira is the extra charges for the transfer
        }
        else if (action == Usage.WITHDRAW) {
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



