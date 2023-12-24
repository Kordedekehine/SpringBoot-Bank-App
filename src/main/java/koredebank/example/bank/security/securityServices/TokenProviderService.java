package koredebank.example.bank.security.securityServices;



import io.jsonwebtoken.Claims;
import koredebank.example.bank.model.UserEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.function.Function;


public interface TokenProviderService {
    String generateLoginToken(Authentication authentication, UserEntity userEntity);

    String getEmailFromToken(String token);

    <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver);

    public UsernamePasswordAuthenticationToken getAuthentication(final String authenticationToken, final Authentication authentication, final UserDetails userDetails);

    boolean validateToken(String token, UserDetails userDetails);
}
