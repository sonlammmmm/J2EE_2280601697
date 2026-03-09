package com.example.LAB05.repository;

import com.example.LAB05.model.Account;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AccountRepository extends JpaRepository<Account, Integer> {
    Optional<Account> findByLoginName(String loginName);
}
