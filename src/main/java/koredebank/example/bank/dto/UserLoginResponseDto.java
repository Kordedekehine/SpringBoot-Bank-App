package koredebank.example.bank.dto;


import koredebank.example.bank.model.Roles;
import koredebank.example.bank.security.securityUtils.JWTToken;

public class UserLoginResponseDto {

    private String id;
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private String email;
    private Roles roles;
    private JWTToken token;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Roles getRoles() {
        return roles;
    }

    public void setRoles(Roles roles) {
        this.roles = roles;
    }

    public JWTToken getToken() {
        return token;
    }

    public void setToken(JWTToken token) {
        this.token = token;
    }
}
