package com.doantotnghiep.nhanambooks.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "receive_email")
public class ReceiveEmail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String email;
    private String name;

    public ReceiveEmail(String name, String email) {
        this.name = name;
        this.email = email;
    }

    public ReceiveEmail() {

    }
}
