package koredebank.example.bank.security.securityServices;



import com.fasterxml.jackson.annotation.JsonIgnore;
import koredebank.example.bank.model.UserEntity;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Slf4j
@Data
@NoArgsConstructor
public class ApplicationUser implements UserDetails {
    private  String id;

    private  String name;

    @JsonIgnore
    private  String email;

    //email == username
//    @JsonIgnore
//    private  String username;

    @JsonIgnore
    private  String password;

    private Collection<? extends GrantedAuthority> authorities;



    public static ApplicationUser create(UserEntity userEntity) {
        //user entity has no role. if you need a role, add field role

        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(userEntity.getRoles().toString())); //user entity does not have role. create one
        return new ApplicationUser(
               String.valueOf( userEntity.getId()),
                userEntity.getFirstname() + " " + userEntity.getLastname(),
                userEntity.getEmail(),
                userEntity.getPassword(),
                authorities
        );
    }

    public ApplicationUser(String id, String name, String email, String password, Collection<? extends GrantedAuthority> authorities) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
        this.authorities = authorities;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
