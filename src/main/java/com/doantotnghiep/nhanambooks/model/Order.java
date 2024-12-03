package com.doantotnghiep.nhanambooks.model;

import com.doantotnghiep.nhanambooks.enums.OrderStatus;
import com.doantotnghiep.nhanambooks.enums.PaymentMethod;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Data
@Entity
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    private String fullname;
    private Long userid;
    private String address;
    private String phone;
    private LocalDate oncreate;
    private String email;
    private Integer total;
    @OneToMany(mappedBy = "order")
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private List<OrderDetail> orderDetails;
    @ManyToOne
    @JoinColumn(name = "discountid")
    private Discount discount;
    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private OrderStatus orderStatus;
    @Enumerated(EnumType.STRING)
    @Column(name = "paymentmethod")
    private PaymentMethod paymentMethod;
}
