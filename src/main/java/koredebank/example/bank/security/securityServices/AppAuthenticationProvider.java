package koredebank.example.bank.security.securityServices;



import koredebank.example.bank.model.Roles;
import koredebank.example.bank.model.UserEntity;
import koredebank.example.bank.repository.UserRepository;
import koredebank.example.bank.security.exceptions.AuthorizationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@Primary
public class AppAuthenticationProvider implements AuthenticationManager {

    @Autowired
    UserRepository userRepository;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        UsernamePasswordAuthenticationToken token = (UsernamePasswordAuthenticationToken) authentication;
        //String username = token.getName();
        String firstname = token.getName();
        String password = (String) token.getCredentials();



        Optional<UserEntity> user = userRepository.findUserByEmail(firstname);

        if (!user.isPresent()) {
            throw new BadCredentialsException("There is not account with given credentials");
        }
//        if (!passwordEncoder.matches(passwordEncoder.encode(password),passwordEncoder.encode( user.get().getPassword()))) {
//            throw new BadCredentialsException("Invalid username or password");
//        }

        UserEntity usersEntity=user.get();
        List<Roles> authorities = Collections.singletonList(usersEntity.getRoles());
        if(usersEntity.getRoles() == null) {
            try {
                throw new AuthorizationException("User has no authority");
            } catch (AuthorizationException e) {
                e.printStackTrace();
            }
        }
        return new UsernamePasswordAuthenticationToken(firstname, password, authorities.stream().map(authority
                -> new SimpleGrantedAuthority(authority.name())).collect(Collectors.toList()));
    }
}
