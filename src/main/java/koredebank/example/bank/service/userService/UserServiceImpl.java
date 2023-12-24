package koredebank.example.bank.service.userService;

import koredebank.example.bank.Email.EmailService;
import koredebank.example.bank.cloudinaryService.CloudStorageService;
import koredebank.example.bank.dto.*;
import koredebank.example.bank.model.*;
import koredebank.example.bank.payload.UserAccountGeneratorDto;
import koredebank.example.bank.repository.*;
import koredebank.example.bank.security.exceptions.*;
import koredebank.example.bank.security.securityServices.UserPrincipalService;
import koredebank.example.bank.security.securityUtils.JWTToken;
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
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.mail.MessagingException;
import java.io.IOException;
import java.time.*;
import java.util.*;

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
    CloudStorageService cloudStorageService;

    @Autowired
   private PasswordEncoder passwordEncoder;

    @Autowired
    EmailService emailService;

    @Autowired
    UserPrincipalService userPrincipalService;


    private static final String USER_NOT_FOUND = "User not found";

    @Override
    public UserLoginResponseDto login(UserLoginDto userLoginDto) throws IncorrectPasswordException, GeneralServiceException {
        UserLoginResponseDto userLoginResponseDto=new UserLoginResponseDto();
        JWTToken jwtToken= userPrincipalService.loginUser(userLoginDto);
        if(jwtToken!=null){
            Optional<UserEntity> usersEntity=userRepository.findUserByEmail(userLoginDto.getEmail());
            if(usersEntity.isPresent()) {
                if (!usersEntity.get().getEnabled()) {
                    throw new GeneralServiceException("User account has not been activated");
                }
            }
            usersEntity.ifPresent(entity -> modelMapper.map(entity, userLoginResponseDto));
            userLoginResponseDto.setToken(jwtToken);
        }
        return userLoginResponseDto;

    }

    @Transactional
    @Override
    public UserSignUpResponseDto signUp(UserSignUpRequestDto userSignUpRequestDto) throws GeneralServiceException, AccountCreationException, MessagingException {
        checkAllParameters(userSignUpRequestDto);

        userWithEmailExists(userSignUpRequestDto);

        UserEntity users= createUserEntityFromDetails(userSignUpRequestDto);

        String confirmation=userPrincipalService.sendRegistrationToken(users);

        users.setValidationToken(confirmation);

        emailService.sendRegistrationSuccessfulEmail(users, confirmation);

        log.info(confirmation);

        userRepository.save(users);

        return generateRegistrationResponse(userSignUpRequestDto, users.getId());
    }

    @Override
    public boolean validateUserAccount(UserLoginValidationRequestDto userLoginValidationRequestDto) throws AccountCreationException, GeneralServiceException, MessagingException {
        if(StringUtil.isBlank(userLoginValidationRequestDto.getEmail())){
            throw new GeneralServiceException("Email cannot be empty");
        }
        Optional<UserEntity> usersEntity=userRepository.findUserByEmail(userLoginValidationRequestDto.getEmail());
        if(usersEntity.isPresent()){
            if(usersEntity.get().getValidationToken().equals(userLoginValidationRequestDto.getToken())){
                usersEntity.get().setEnabled(true);
                usersEntity.get().setValidationToken(null);

                log.info(usersEntity.get().getPassword());
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

        Optional<UserEntity> user = userRepository.findUserByEmail(userEmail);
        if (user.isEmpty()) {
            throw new GeneralServiceException(USER_NOT_FOUND);
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
        Optional<UserEntity> optionalBaseUser = userRepository.findUserByEmail(userForgotPasswordRequestDto.getEmail());
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
        Optional<UserEntity> users = userRepository.findUserByEmail(userRetrieveForgotPasswordRequestDto.getEmail());
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


    private UserSignUpResponseDto generateRegistrationResponse(UserSignUpRequestDto userSignUpRequestDto,String id){
        UserSignUpResponseDto userSignUpResponseDto=new UserSignUpResponseDto();

        modelMapper.map(userSignUpRequestDto,userSignUpResponseDto);

        userSignUpResponseDto.setId(id);
        userSignUpResponseDto.setRole(Roles.BASE_USER);

        return userSignUpResponseDto;
    }

    private UserEntity createUserEntityFromDetails(UserSignUpRequestDto userSignUpRequestDto){
        UserEntity userEntity = new UserEntity();
        modelMapper.map(userSignUpRequestDto, userEntity);
        String password=passwordEncoder.encode(userSignUpRequestDto.getPassword());
        userEntity.setPassword(password);
        userEntity.setId(IdGenerator.generateId());
        userEntity.setCreatedAt(Instant.now());
        userEntity.setRoles(Roles.BASE_USER);
        return userEntity;
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

        Optional<UserEntity> userByEmail = userRepository.findUserByEmail(userSignUpRequestDto.getEmail());
        if(userByEmail.isPresent()){
            throw new AccountCreationException("Users with this email already exists");
        }

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

}
