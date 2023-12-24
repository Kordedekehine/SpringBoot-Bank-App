package koredebank.example.bank.repository;

import koredebank.example.bank.model.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity,String> {

    Optional<UserEntity> findUserByEmail(String email);


    Optional<UserEntity> findUserEntitiesByEmail(String email);

   // Optional<User> findUserByUsername(String username);

}
