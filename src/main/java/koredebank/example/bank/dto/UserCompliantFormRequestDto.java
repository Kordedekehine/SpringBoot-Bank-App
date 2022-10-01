package koredebank.example.bank.dto;

import com.sun.istack.NotNull;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.Column;

public class UserCompliantFormRequestDto {

    private String title;

    private String description;

    private String modeOfMeeting;

    private MultipartFile image1;


    private MultipartFile image2;


    private MultipartFile image3;


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


    public MultipartFile getImage1() {
        return image1;
    }

    public void setImage1(MultipartFile image1) {
        this.image1 = image1;
    }

    public MultipartFile getImage2() {
        return image2;
    }

    public void setImage2(MultipartFile image2) {
        this.image2 = image2;
    }

    public MultipartFile getImage3() {
        return image3;
    }

    public void setImage3(MultipartFile image3) {
        this.image3 = image3;
    }
}
