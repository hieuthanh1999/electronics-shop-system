package com.doantotnghiep.nhanambooks.repository;

import com.doantotnghiep.nhanambooks.model.Order;
import com.doantotnghiep.nhanambooks.model.OrderDetail;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

public interface OrderRepository extends JpaRepository<Order, UUID> {

    Page<Order> findByFullnameContainingIgnoreCase(String keyword, Pageable pageable);

    @Query("select sum(od.quantity * (od.product.price - od.product.discount)) from Order o join OrderDetail od where o.oncreate = now() and od.order = o")
    Integer countOrderToDay();

    @Query("select od from OrderDetail od join Order o where od.order = o")
    List<OrderDetail> findDetailById(UUID id);

    @Query("select sum(od.quantity * (od.product.price - od.product.discount)) as total, od.product.category.name from OrderDetail od " +
            "where year(od.order.oncreate) = year(now()) and month (od.order.oncreate) = month(now()) group by od.product.category order by total limit 5")
    List<Object[]> findDataForPieChart();

    @Query("select sum(od.quantity * (od.product.price - od.product.discount)) as total, month(od.order.oncreate) from OrderDetail od " +
            "where year(od.order.oncreate) = year(now()) group by month(od.order.oncreate) order by month(od.order.oncreate) limit 12")
    List<Object[]> findDataForLineChart();
}
