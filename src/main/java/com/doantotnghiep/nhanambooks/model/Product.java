package com.doantotnghiep.nhanambooks.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;

@Data
@Entity
@Table(name = "product")
public class Product {

    @Id
    @GeneratedValue
    private Long id;

    private String name;
    private Integer quantity;
    @ManyToOne
    @JoinColumn(name="categoryid")
    private Category category;

    private String description;
    private String author;
    private String image;
    int price;
    int discount;
    LocalDate oncreate;
}
