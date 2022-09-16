package koredebank.example.bank.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import java.time.LocalDateTime;


@Entity
@Table(name = "transaction", schema = "people_bank")
@Data
@NoArgsConstructor
@ToString
@SequenceGenerator(name = "transaction_seq", sequenceName = "transaction_sequence", schema = "people_bank", initialValue = 5)
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "transaction_seq")
    private long id;

    private long sourceAccountId;

    private long targetAccountId;

    private String targetOwnerName;

    private String targetEmail;

    private double amount;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private User user;

    private LocalDateTime initiationDate;

    private LocalDateTime completionDate;

    private String reference;



    // Add support for Bank charges, currency conversion, setup repeat payment/ standing order
}
