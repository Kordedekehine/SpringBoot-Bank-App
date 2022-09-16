package koredebank.example.bank.security;

import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
@AllArgsConstructor
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http
                .csrf().disable()
                .authorizeRequests()
                .antMatchers("/api/v1/bank","/api/v1/bank/***","/api/v1/bank/***","/api/v1/bank/user/***","/api/v1/bank/user/***/***")
                .permitAll()
                .antMatchers("/swagger-ui/","/swagger-ui","/api/**", "/swagger-ui.html", "/swagger-ui/**","/webjars/**", "/v2/**", "/swagger-resources/**").permitAll()
                .anyRequest().permitAll();
    }
}
