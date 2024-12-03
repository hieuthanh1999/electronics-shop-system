package com.doantotnghiep.nhanambooks.model;

import com.doantotnghiep.nhanambooks.user.User;
import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "love")
public class Love {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "userid")
    private User user;

    @ManyToOne
    @JoinColumn(name = "productid")
    private Product product;

}
