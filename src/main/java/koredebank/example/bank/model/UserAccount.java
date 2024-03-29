package koredebank.example.bank.model;

import lombok.Data;
import lombok.ToString;

import javax.persistence.*;
import javax.validation.constraints.Email;
import java.util.ArrayList;
import java.util.List;

@Data
@ToString
@Entity
@Table(name = "account")
public class UserAccount {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    private String sortCode;

    private String accountNumber;

    private double currentBalance;

    @Email
    private String accountEmail;

    private String bankName;


    private String ownerName;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "users_id")
    private UserEntity userEntity;

    @OneToMany(cascade = CascadeType.ALL,fetch = FetchType.EAGER)
    @JoinColumn
    private List<Transaction>transactionList=new ArrayList<>(); //paging or the normal conventional way

    public UserAccount() {

    }

    public UserAccount(String bankName, String ownerName, String generateSortCode, String generateAccountNumber, double currentBalance) {
        this.sortCode = generateSortCode;
        this.accountNumber = generateAccountNumber;
        this.currentBalance = currentBalance;
        this.bankName = bankName;
        this.ownerName = ownerName;
    }
//    public UserAccount(long id, String sortCode, String accountNumber, double currentBalance, String bankName, String ownerName,int age) {
//        this.id = id;
//        this.sortCode = sortCode;
//        this.accountNumber = accountNumber;
//        this.currentBalance = currentBalance;
//        this.bankName = bankName;
//        this.ownerName = ownerName;
//    }
//
//    public UserAccount(long id, String sortCode, String accountNumber, double currentBalance, String bankName, String ownerName,int age) {
//        this.id = id;
//        this.sortCode = sortCode;
//        this.accountNumber = accountNumber;
//        this.currentBalance = currentBalance;
//        this.bankName = bankName;
//        this.ownerName = ownerName;
//    }



    // later Add support for multiple account types (business, savings, etc.)
// later Add support for foreign currency accounts
}
