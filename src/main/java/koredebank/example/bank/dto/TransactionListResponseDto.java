package koredebank.example.bank.dto;

import java.util.ArrayList;
import java.util.List;

public class TransactionListResponseDto {

    private int sizeOfList;
    private List<TransactionResponseDto> transactionResponseDtoList= new ArrayList<>();


    public int getSizeOfList() {
        return sizeOfList;
    }

    public void setSizeOfList(int sizeOfList) {
        this.sizeOfList = sizeOfList;
    }

    public List<TransactionResponseDto> getTransactionResponseDtoList() {
        return transactionResponseDtoList;
    }

    public void setTransactionResponseDtoList(List<TransactionResponseDto> transactionResponseDtoList) {
        this.transactionResponseDtoList = transactionResponseDtoList;
    }
}
