package koredebank.example.bank.dto;

import java.util.ArrayList;
import java.util.List;

public class AccountListResponseDto {

    private int sizeOfList;
    private List<AccountResponseDto> accountResponseDtoList= new ArrayList<>();

    public int getSizeOfList() {
        return sizeOfList;
    }

    public void setSizeOfList(int sizeOfList) {
        this.sizeOfList = sizeOfList;
    }

    public List<AccountResponseDto> getAccountResponseDtoList() {
        return accountResponseDtoList;
    }

    public void setAccountResponseDtoList(List<AccountResponseDto> accountResponseDtoList) {
        this.accountResponseDtoList = accountResponseDtoList;
    }
}
