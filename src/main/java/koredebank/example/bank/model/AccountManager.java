package koredebank.example.bank.model;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
public class AccountManager implements Serializable {
    @Id
    private String id;
    @Column
    private String email;
    @Enumerated
    private Roles roles;
    @Column
    private String password;

    @OneToMany(cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    @JoinColumn
    private List<Transaction> transactionList=new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    @JoinColumn
    private List<UserAccount> userAccountsList=new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    @JoinColumn
    private List<CustomerCompliantForm> customerCompliantFormList = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "users_id")
    private UserEntity userEntity;

    @ManyToOne
    @JoinColumn(name = "user_account_id")
    private UserAccount userAccount;

    public UserAccount getUserAccount() {
        return userAccount;
    }

    public void setUserAccount(UserAccount userAccount) {
        this.userAccount = userAccount;
    }

    public UserEntity getUser() {
        return userEntity;
    }

    public void setUser(UserEntity userEntity) {
        this.userEntity = userEntity;
    }

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

    public Roles getRoles() {
        return roles;
    }

    public void setRoles(Roles roles) {
        this.roles = Roles.ACCOUNT_MANAGER;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }


    public List<Transaction> getTransactionList() {
        return transactionList;
    }

    public void setTransactionList(List<Transaction> transactionList) {
        this.transactionList = transactionList;
    }

    public List<UserAccount> getUserAccountsList() {
        return userAccountsList;
    }

    public void setUserAccountsList(List<UserAccount> userAccountsList) {
        this.userAccountsList = userAccountsList;
    }


    public List<CustomerCompliantForm> getCustomerCompliantFormList() {
        return customerCompliantFormList;
    }

    public void setCustomerCompliantFormList(List<CustomerCompliantForm> customerCompliantFormList) {
        this.customerCompliantFormList = customerCompliantFormList;
    }
}
