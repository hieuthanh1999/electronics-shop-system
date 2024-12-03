package com.doantotnghiep.nhanambooks.dto;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
public class CountProductByCategory {
    private long count;
    private String categoryName;

    public CountProductByCategory(long count, String categoryName) {
        this.count = count;
        this.categoryName = categoryName;
    }
}
