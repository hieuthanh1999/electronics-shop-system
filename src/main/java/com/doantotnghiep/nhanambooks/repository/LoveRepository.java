package com.doantotnghiep.nhanambooks.repository;

import com.doantotnghiep.nhanambooks.model.Love;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface LoveRepository extends JpaRepository<Love,Long> {
    @Query("select l from Love l where l.user.id = :id ")
    List<Love> findAllByUserId(@Param("id") Long id);

    @Query("select l from Love l where l.user.email = :email ")
    List<Love> findByUserEmail(@Param("email") String email);
}
