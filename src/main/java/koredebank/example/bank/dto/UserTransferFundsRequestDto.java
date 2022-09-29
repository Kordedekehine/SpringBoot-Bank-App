package koredebank.example.bank.dto;

import koredebank.example.bank.payload.UserAccountDto;

import javax.validation.constraints.Min;
import javax.validation.constraints.Positive;

public class UserTransferFundsRequestDto {

    private UserAccountDto sourceAccount;

    private UserAccountDto targetAccount;

    @Positive(message = "Transfer amount must be positive")
    @Min(value = 1, message = "Amount must be larger than 1")
    private double amount;

    private String reference;

    private String targetEmail;


    public String getTargetEmail() {
        return targetEmail;
    }

    public void setTargetEmail(String targetEmail) {
        this.targetEmail = targetEmail;
    }

    public UserAccountDto getSourceAccount() {
        return sourceAccount;
    }

    public void setSourceAccount(UserAccountDto sourceAccount) {
        this.sourceAccount = sourceAccount;
    }

    public UserAccountDto getTargetAccount() {
        return targetAccount;
    }

    public void setTargetAccount(UserAccountDto targetAccount) {
        this.targetAccount = targetAccount;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }
}
