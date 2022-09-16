package koredebank.example.bank.payload;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.Positive;

public class TransferDto {

    private UserAccountDto sourceAccount;

    private UserAccountDto targetAccount;

    @Positive(message = "Transfer amount must be positive")
    // Prevent fraudulent transfers attempting to abuse currency conversion errors
    @Min(value = 1, message = "Amount must be larger than 1")
    private double amount;

    private String reference;

    public TransferDto() {}

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

    @Override
    public String toString() {
        return "TransactionInput{" +
                "sourceAccount=" + sourceAccount +
                ", targetAccount=" + targetAccount +
                ", amount=" + amount +
                ", reference='" + reference + '\'' +
                '}';
    }
}
