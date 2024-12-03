package com.doantotnghiep.nhanambooks.dto;

import com.doantotnghiep.nhanambooks.enums.PaymentMethod;
import lombok.Data;

@Data
public class OrderRequest {
    private String fullname;
    private String phone;
    private String email;
    private String address;
    private String discountName;
    private PaymentMethod paymentMethod;
}
