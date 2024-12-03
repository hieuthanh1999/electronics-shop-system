package com.doantotnghiep.nhanambooks.repository;

import com.doantotnghiep.nhanambooks.model.OrderDetail;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderDetailRepository extends JpaRepository<OrderDetail, Long> {
}
