package koredebank.example.bank.repository;

import koredebank.example.bank.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.Optional;

@Transactional
@Repository
public interface UserRepository extends JpaRepository<User,String> {

    Optional<User> findUserByEmail(String email);
    Optional<User> findUserEntitiesByEmail(String email);
    Optional<User> findById(int userId);
   // Optional<User> findUserByUsername(String username);

}
