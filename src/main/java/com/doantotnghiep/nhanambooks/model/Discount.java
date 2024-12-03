package com.doantotnghiep.nhanambooks.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;

@Data
@Entity
@Table(name = "discount")
public class Discount {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    private String name;
    private int amount;
    private int discount;
    private LocalDate oncreate;
    private LocalDate expired;
    private Boolean isActive;
}
