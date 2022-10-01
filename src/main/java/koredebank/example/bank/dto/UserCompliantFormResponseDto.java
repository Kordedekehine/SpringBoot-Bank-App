package koredebank.example.bank.dto;

import org.springframework.web.multipart.MultipartFile;

public class UserCompliantFormResponseDto {

    private String title;

    private String description;

    private String modeOfMeeting;


    private String image1;


    private String image2;


    private String image3;
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


    public String getImage1() {
        return image1;
    }

    public void setImage1(String image1) {
        this.image1 = image1;
    }

    public String getImage2() {
        return image2;
    }

    public void setImage2(String image2) {
        this.image2 = image2;
    }

    public String getImage3() {
        return image3;
    }

    public void setImage3(String image3) {
        this.image3 = image3;
    }
}
