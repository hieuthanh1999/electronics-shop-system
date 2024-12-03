package com.doantotnghiep.nhanambooks.repository;

import com.doantotnghiep.nhanambooks.role.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface RoleRepository extends JpaRepository<Role, Integer> {
    public Set<Role> findByName(String name);
}
