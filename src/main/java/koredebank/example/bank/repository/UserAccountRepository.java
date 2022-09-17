package koredebank.example.bank.repository;

import koredebank.example.bank.model.UserAccount;
import org.springframework.data.jpa.repository.JpaRepository;


import java.util.Optional;

public interface UserAccountRepository extends JpaRepository<UserAccount, Long> {

    Optional<UserAccount> findBySortCodeAndAccountNumber(String sortCode, String accountNumber);

    Optional<UserAccount> findByAccountNumber(String accountNumber);


}
