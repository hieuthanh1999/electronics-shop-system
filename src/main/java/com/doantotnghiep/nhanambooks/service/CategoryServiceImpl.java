package com.doantotnghiep.nhanambooks.service;

import com.doantotnghiep.nhanambooks.model.Category;
import com.doantotnghiep.nhanambooks.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class CategoryServiceImpl implements CategoryService{

    private final CategoryRepository categoryRepository;
    @Override
    public List<Category> getCategories() {
        return categoryRepository.findAll();
    }

    @Override
    public Optional<Category> getCategory(Long id) {
        Optional<Category> optionalCategory = categoryRepository.findById(id);

        return optionalCategory;
    }

    @Override
    public void saveCategory(Category category) {
        categoryRepository.save(category);
    }

    @Override
    public boolean deleteCategory(Long id) {
        try {
            categoryRepository.deleteById(id);
        } catch(Exception e) {
            return false;
        }
        return true;
    }
}
