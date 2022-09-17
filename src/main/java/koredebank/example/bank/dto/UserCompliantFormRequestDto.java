package koredebank.example.bank.dto;

import com.sun.istack.NotNull;

import javax.persistence.Column;

public class UserCompliantFormRequestDto {

    private String title;

    private String description;

    private String modeOfMeeting;

    private CustomerCompliantDaysLength customerCompliantDaysLength;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getModeOfMeeting() {
        return modeOfMeeting;
    }

    public void setModeOfMeeting(String modeOfMeeting) {
        this.modeOfMeeting = modeOfMeeting;
    }

    public CustomerCompliantDaysLength getCustomerCompliantDaysLength() {
        return customerCompliantDaysLength;
    }

    public void setCustomerCompliantDaysLength(CustomerCompliantDaysLength customerCompliantDaysLength) {
        this.customerCompliantDaysLength = customerCompliantDaysLength;
    }
}
