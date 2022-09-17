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

    @NotNull
    private Instant expirationDate;

    @Column
    private String modeOfMeeting;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(columnDefinition = "user_compliant_id")
    private User user;

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

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
