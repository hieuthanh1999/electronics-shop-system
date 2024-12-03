package com.doantotnghiep.nhanambooks.repository;

import com.doantotnghiep.nhanambooks.model.ReceiveEmail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface ReceiveRepository extends JpaRepository<ReceiveEmail, Long> {

    Optional<ReceiveEmail> findByEmail(String email);

    @Query("select u.email as Email from User u union select re.email as Email from ReceiveEmail re")
    Set<String> findAllEmail();
}
