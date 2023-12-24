package koredebank.example.bank.security.securityServices;


import io.jsonwebtoken.*;
import io.jsonwebtoken.SignatureException;
import koredebank.example.bank.model.UserEntity;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Base64;
import java.util.Collection;
import java.util.Date;
import java.util.function.Function;
import java.util.stream.Collectors;

import static koredebank.example.bank.security.securityUtils.SecurityConstants.*;


@Data
@Service
@Configuration
public class TokenProviderServiceImpl implements Serializable, TokenProviderService {
    @Override
    public String generateLoginToken(Authentication authentication, UserEntity userEntity) {

        final String authorities = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));


        String jwts = Jwts.builder()
                .setSubject(authentication.getName())
                .claim(AUTHORITIES_KEY, authorities)
                //this error is being thrown because "where is AUTHORITIES_KEY" coming from?? it should be a variable that holds something.
                //create a security constants class and declare it there then import the class here
//                .claim("id", user.getId())
                .setIssuer("AUTOX")
                .signWith(SignatureAlgorithm.HS512, getEncryptedSigningKey())
                //this should go away once above is solved
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis()
                        + EXPIRATION_DATE)) //same issue with AUTHORITIES_KEY above
                .compact();
        return jwts;

    }
    private String getEncryptedSigningKey(){

        String encryptedSigningKey =  Base64.getEncoder().encodeToString(SIGNING_KEY_STRING.getBytes());
        //same issue with above

        return encryptedSigningKey;
    }
    Logger logger = LoggerFactory.getLogger(TokenProviderServiceImpl.class);

    @Override
    public String getEmailFromToken(String token) {
        return getClaimFromToken(token, Claims::getSubject);
    }

    @Override
    public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = getAllClaimsFromToken(token);
        return claimsResolver.apply(claims);
    }

    private Claims getAllClaimsFromToken(String token) {

        var split = token.split(" ");
        String t = split[split.length-1];


        Claims claims;
        try{
            claims = Jwts.parser()
                    .setSigningKey(getEncryptedSigningKey())
                    .parseClaimsJws(t)
                    .getBody();
            return claims;
        }
        catch (SignatureException ex){
            logger.error("untrusted token detected and invalidated");
            throw new SecurityException("token untrusted");

        }

    }

    @Override
    public boolean validateToken(String token, UserDetails userDetails){
        final String email = getEmailFromToken(token);
        boolean tokenStatus = email.equals(userDetails.getUsername()) && (!isTokenExpired(token));
        return tokenStatus;

    }

    private boolean isTokenExpired(String token) {
        final Date expiration = getExpirationDateFromToken(token);
        return expiration.before(new Date());
    }

    private Date getExpirationDateFromToken(String token) {
        return getClaimFromToken(token, Claims::getExpiration);
    }


    @Override
    public UsernamePasswordAuthenticationToken getAuthentication(final String authenticationToken, final Authentication authentication, final UserDetails userDetails) {
        final JwtParser jwtParser = Jwts.parser().setSigningKey(getEncryptedSigningKey());

        final Jws<Claims> claimsJws = jwtParser.parseClaimsJws(authenticationToken);

        final Claims claims = claimsJws.getBody();

        final Collection<? extends GrantedAuthority> authorities =
                Arrays.stream(claims.get(AUTHORITIES_KEY).toString().split(","))
                        .map(SimpleGrantedAuthority::new)
                        .collect(Collectors.toList());

        return new UsernamePasswordAuthenticationToken(userDetails, "", authorities);
    }
}
