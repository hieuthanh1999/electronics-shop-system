package com.doantotnghiep.nhanambooks.service;

import com.doantotnghiep.nhanambooks.model.Category;

import java.util.List;
import java.util.Optional;

public interface CategoryService {
    List<Category>  getCategories();

    Optional<Category> getCategory(Long id);

    void saveCategory(Category category);

    boolean deleteCategory(Long id);
}
