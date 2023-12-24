package koredebank.example.bank.model;

import com.sun.istack.NotNull;

import javax.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "compliant_form")
public class CustomerCompliantForm {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private long id;

    @Column
    private String title;

    @Column
    private String description;

    @Column
    private String image1;

    @Column
    private String image2;

    @Column
    private String image3;

    @NotNull
    private Instant expirationDate;

    @Column
    private String modeOfMeeting;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(columnDefinition = "users_compliant_id")
    private UserEntity userEntity;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

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

    public Instant getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(Instant expirationDate) {
        this.expirationDate = expirationDate;
    }

    public String getModeOfMeeting() {
        return modeOfMeeting;
    }

    public void setModeOfMeeting(String modeOfMeeting) {
        this.modeOfMeeting = modeOfMeeting;
    }

    public UserEntity getUser() {
        return userEntity;
    }

    public void setUser(UserEntity userEntity) {
        this.userEntity = userEntity;
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
