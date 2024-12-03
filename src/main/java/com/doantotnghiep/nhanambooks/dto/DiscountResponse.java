package com.doantotnghiep.nhanambooks.dto;

import com.doantotnghiep.nhanambooks.model.Discount;
import lombok.Data;

@Data
public class DiscountResponse {
    private Discount discount;
    private boolean isValid;
}
