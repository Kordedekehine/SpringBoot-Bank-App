package koredebank.example.bank.dto;

import java.util.ArrayList;
import java.util.List;

public class UserCompliantListResponseDto {

    private int sizeOfList;

    private List<UserCompliantFormResponseDto> userCompliantFormResponseDtoList= new ArrayList<>();


    public int getSizeOfList() {
        return sizeOfList;
    }

    public void setSizeOfList(int sizeOfList) {
        this.sizeOfList = sizeOfList;
    }

    public List<UserCompliantFormResponseDto> getUserCompliantFormResponseDtoList() {
        return userCompliantFormResponseDtoList;
    }

    public void setUserCompliantFormResponseDtoList(List<UserCompliantFormResponseDto> userCompliantFormResponseDtoList) {
        this.userCompliantFormResponseDtoList = userCompliantFormResponseDtoList;
    }
}
