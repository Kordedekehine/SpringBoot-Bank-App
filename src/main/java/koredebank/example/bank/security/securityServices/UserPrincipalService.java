package koredebank.example.bank.security.securityServices;


import koredebank.example.bank.dto.UserLoginDto;
import koredebank.example.bank.model.UserEntity;
import koredebank.example.bank.repository.UserRepository;
import koredebank.example.bank.security.exceptions.AuthorizationException;
import koredebank.example.bank.security.exceptions.GeneralServiceException;
import koredebank.example.bank.security.exceptions.IncorrectPasswordException;
import koredebank.example.bank.security.securityUtils.JWTToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class UserPrincipalService implements UserDetailsService {
    @Autowired
    UserRepository userRepository;
    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    private AppAuthenticationProvider authenticationManager;

    @Autowired
    TokenProviderServiceImpl tokenProviderService;



    @Override
    public UserDetails loadUserByUsername(String firstname) throws UsernameNotFoundException {

        Optional<UserEntity> optionalUser = userRepository.findUserByEmail(firstname);
        if(optionalUser.isEmpty()){
            throw new UsernameNotFoundException("User with given email not found");
        }
        else{
            UserEntity userEntity =  optionalUser.get();
            return ApplicationUser.create(userEntity);
        }
    }

    public JWTToken loginUser(UserLoginDto userLoginDto) throws UsernameNotFoundException, IncorrectPasswordException, GeneralServiceException {
        Optional<UserEntity> users = userRepository.findUserByEmail(userLoginDto.getEmail());


        if(users.isPresent()){
            if(!users.get().getEnabled()){
                throw new GeneralServiceException("Account has not been enabled");
            }
            boolean matchingResult=passwordEncoder.matches(userLoginDto.getPassword(), users.get().getPassword());

            if(!matchingResult){
                throw new IncorrectPasswordException("The password is Incorrect");
            }
            final Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            userLoginDto.getEmail(), userLoginDto.getPassword()
                    )
            );

            SecurityContextHolder.getContext().setAuthentication(authentication);
            users = userRepository.findUserByEmail(userLoginDto.getEmail());

            JWTToken jwtToken = new JWTToken(tokenProviderService.generateLoginToken(authentication, users.get()));


            return jwtToken;
        }
        throw new UsernameNotFoundException("User Not Found");

    }

    public String signUpUser(UserEntity users) {
        StringBuilder stringBuilder= new StringBuilder("Validates ");
        boolean userExists=userRepository.findUserByEmail(users.getEmail()).isPresent();
        if(userExists){
            throw new IllegalStateException("user with this email already exists");
        }
        userRepository.save(users);
        String encodedPassword=passwordEncoder.encode(users.getPassword());
        users.setPassword(encodedPassword);
        String token= UUID.randomUUID().toString();

        stringBuilder.append(token);
        return stringBuilder.toString();
    }

    public String sendRegistrationToken(UserEntity usersEntity){
        //mailsender
        String token= UUID.randomUUID().toString().replace("-","").substring(0,6);
        return token;
    }

    public String sendRecoveryToken(UserEntity users){
        //mailsender
        String token= UUID.randomUUID().toString().replace("-","").substring(0,6);
        return token;
    }

    public String getUserEmailAddressFromToken(String token) throws AuthorizationException {
        return tokenProviderService.getEmailFromToken(token);
    }
    public boolean passwordMatches(String password,String password2){
       return passwordEncoder.matches(password, password2);

    }

}
