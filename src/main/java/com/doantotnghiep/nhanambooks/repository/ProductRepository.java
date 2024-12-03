package com.doantotnghiep.nhanambooks.repository;

import com.doantotnghiep.nhanambooks.dto.CountProductByCategory;
import com.doantotnghiep.nhanambooks.model.Product;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.beans.Transient;
import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    Page<Product> findByNameContainingIgnoreCase(String keyword, Pageable pageable);

    Optional<Product> findByName(String name);
    @Query("select new com.doantotnghiep.nhanambooks.dto.CountProductByCategory(count(p),p.category.name ) from Product p group by p.category")
    List<CountProductByCategory> countProductByCategory();

    @Query("select p from Product p where p.quantity > 0 order by p.id desc ")
    List<Product> findJustArrivedProduct(Pageable pageable);

    @Query("select case when count(p) > 0 then true else false end from Product p where p.quantity > 0 and p.id = :id and p.quantity > :quantity")
    Boolean existsToSaveOrder(@Param("id") Long id,@Param("quantity") int quantity);

    @Modifying
    @Query("update Product p set p.quantity = p.quantity - :quantity where p.id = :id")
    @Transactional
    void updateQuantity(@Param("quantity") int quantity,@Param("id") Long id);

    @Query("select p from Product p where p.category.id = :id and p.quantity > 0 order by p.id desc limit 5")
    List<Product> findRelativeProduct(Long id);

    @Query("select sum(p.quantity) from Product p where p.quantity > 0")
    int findProductInStock();

    List<Product> findByCategoryId(Long id);

    @Query("select p from (select od.product as p,sum(od.quantity) as num from OrderDetail od group by od.product order by num limit 8)")
    List<Product> findByMostSale();
}
