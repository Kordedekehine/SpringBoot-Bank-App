package koredebank.example.bank.payload;

import javax.validation.constraints.NotBlank;
import java.util.Objects;

public class CreateAccountDto {

    @NotBlank(message = "Bank name is mandatory")
    private String bankName;

    @NotBlank(message = "Owner name is mandatory")
    private String ownerName;


    public CreateAccountDto() {}

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    @Override
    public String toString() {
        return "CreateAccountInput{" +
                "bankName='" + bankName + '\'' +
                ", ownerName='" + ownerName + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CreateAccountDto that = (CreateAccountDto) o;
        return Objects.equals(bankName, that.bankName) &&
                Objects.equals(ownerName, that.ownerName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(bankName, ownerName);
    }
}
