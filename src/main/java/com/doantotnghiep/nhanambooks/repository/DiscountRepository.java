package com.doantotnghiep.nhanambooks.repository;

import com.doantotnghiep.nhanambooks.model.Discount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DiscountRepository extends JpaRepository<Discount, Long> {
    @Query("select case when count(d) > 0 then true else false end from Discount d where d.amount > 0 " +
            "and d.expired >= now() and d.isActive = true and d.name = :name")
    boolean exitsDiscountCanUse(@Param("name")String name);

    Discount findByName(String name);

    @Query("select d from Discount d where d.isActive = true and d.amount > 0 and d.expired >= now()")
    List<Discount> findCanUse();
}
