package koredebank.example.bank.dto;

import javax.validation.constraints.Positive;

public class UserWithdrawFundsResponseDto {

    private String accountNumber;

    private String sortCode;
    @Positive(message = "Transfer amount must be positive")
    private double amount;

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getSortCode() {
        return sortCode;
    }

    public void setSortCode(String sortCode) {
        this.sortCode = sortCode;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }
}
