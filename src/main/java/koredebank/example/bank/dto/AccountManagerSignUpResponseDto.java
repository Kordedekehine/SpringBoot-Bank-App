package koredebank.example.bank.dto;

import koredebank.example.bank.model.Roles;

import javax.validation.constraints.Email;

public class AccountManagerSignUpResponseDto {

    private String id;

    private String email;


    private Roles role;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Roles getRole() {
        return role;
    }

    public void setRole(Roles role) {
        this.role = role;
    }
}
