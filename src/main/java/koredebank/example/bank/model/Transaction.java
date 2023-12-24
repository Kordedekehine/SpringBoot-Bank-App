package koredebank.example.bank.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import java.time.LocalDateTime;


@Entity
@Table(name = "transaction")
@Data
@NoArgsConstructor
@ToString
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private long id;

    private long sourceAccountId;

    private long targetAccountId;

    private String targetOwnerName;

    private String targetEmail;

    private double amount;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "users_id")
    private UserEntity userEntity;

    private LocalDateTime initiationDate;

    private LocalDateTime completionDate;

    private String reference;

    // Add support for Bank charges, currency conversion, setup repeat payment/ standing order
}
