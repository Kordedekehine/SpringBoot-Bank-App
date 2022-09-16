package koredebank.example.bank.dto;

public class UserForgotPasswordRequestDto {

    private String email;

    public void setEmail(String email){
        this.email = email;
    }

    public String getEmail(){
        return email;
    }
}
