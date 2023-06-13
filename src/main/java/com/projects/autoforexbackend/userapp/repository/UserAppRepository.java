package com.projects.autoforexbackend.userapp.repository;

import com.projects.autoforexbackend.userapp.model.UserApp;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
@Transactional(readOnly = true)
public interface UserAppRepository extends JpaRepository<UserApp, Long> {

    Optional<UserApp> findByEmail(String email);
    UserApp findByUsernameAndPassword(String email, String password);
}
