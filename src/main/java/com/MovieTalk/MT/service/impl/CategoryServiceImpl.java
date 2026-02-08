package com.MovieTalk.MT.service.impl;

import com.MovieTalk.MT.entity.Category;
import com.MovieTalk.MT.repository.CategoryRepository;
import com.MovieTalk.MT.service.CategoryService;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;

    public CategoryServiceImpl(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Override
    public Category add(Category category) {
        if (categoryRepository.existsByName(category.getName())) {
            throw new RuntimeException("Category with this name already exists");
        }
        return categoryRepository.save(category);
    }

    @Override
    public Category update(Long id, Category category) {
        Category existingCategory = categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Category not found"));

        if (category.getName() != null) {
            if (!category.getName().equals(existingCategory.getName()) && 
                categoryRepository.existsByName(category.getName())) {
                throw new RuntimeException("Category with this name already exists");
            }
            existingCategory.setName(category.getName());
        }
        if (category.getImageUrl() != null) {
            existingCategory.setImageUrl(category.getImageUrl());
        }

        return categoryRepository.save(existingCategory);
    }

    @Override
    public void delete(Long id) {
        if (!categoryRepository.existsById(id)) {
            throw new RuntimeException("Category not found");
        }
        categoryRepository.deleteById(id);
    }

    @Override
    public List<Category> listAll() {
        return categoryRepository.findAll();
    }

    @Override
    public Category listOne(Long id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Category not found"));
    }
}
