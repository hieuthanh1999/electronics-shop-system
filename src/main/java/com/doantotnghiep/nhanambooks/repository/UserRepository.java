package com.doantotnghiep.nhanambooks.repository;

import com.doantotnghiep.nhanambooks.model.Product;
import com.doantotnghiep.nhanambooks.role.Role;
import com.doantotnghiep.nhanambooks.user.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.Set;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);

    Optional<User> findByToken(String token);

    Page<User> findByNameContainingIgnoreCase(String keyword, Pageable pageable);
}
