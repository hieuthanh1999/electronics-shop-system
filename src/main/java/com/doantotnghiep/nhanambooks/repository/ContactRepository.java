package com.doantotnghiep.nhanambooks.repository;

import com.doantotnghiep.nhanambooks.model.Contact;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ContactRepository extends JpaRepository<Contact, Long> {
    Page<Contact> findByNameContainingIgnoreCase(String keyword, Pageable pageable);
}
