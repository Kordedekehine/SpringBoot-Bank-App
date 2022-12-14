package koredebank.example.bank.dto;

import javax.validation.constraints.*;

public class UserDepositsFundsResponseDto {

    @NotBlank(message = "Target account no is mandatory")
    private String targetAccountNo;

    private String nameOfDepositor;

    // Prevent fraudulent transfers attempting to abuse currency conversion errors
    @Positive(message = "Transfer amount must be positive")
    private double amount;

    public String getNameOfDepositor() {
        return nameOfDepositor;
    }

    public void setNameOfDepositor(String nameOfDepositor) {
        this.nameOfDepositor = nameOfDepositor;
    }

    public String getTargetAccountNo() {
        return targetAccountNo;
    }

    public void setTargetAccountNo(String targetAccountNo) {
        this.targetAccountNo = targetAccountNo;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }
}
