package koredebank.example.bank.repository;

import koredebank.example.bank.model.AccountManager;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AccountManagerRepository extends JpaRepository<AccountManager,String> {

    Optional<AccountManager> findAdminByEmail(String email);
}
