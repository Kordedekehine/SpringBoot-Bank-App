package koredebank.example.bank.repository;

import koredebank.example.bank.model.CustomerCompliantForm;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerCompliantFormRepository extends JpaRepository<CustomerCompliantForm, Long> {

}
