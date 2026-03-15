package com.MovieTalk.MT.service.impl;

import com.MovieTalk.MT.entity.Category;
import com.MovieTalk.MT.repository.CategoryRepository;
import com.MovieTalk.MT.service.CategoryService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final FileStorageService fileStorageService;

    public CategoryServiceImpl(CategoryRepository categoryRepository,
            FileStorageService fileStorageService) {
        this.categoryRepository = categoryRepository;
        this.fileStorageService = fileStorageService;
    }

    @Override
    public Category add(Category category) {
        if (categoryRepository.existsByName(category.getName())) {
            throw new RuntimeException("Category with this name already exists");
        }
        return categoryRepository.save(category);
    }

    @Override
    public Category add(Category category, MultipartFile image) {
        if (categoryRepository.existsByName(category.getName())) {
            throw new RuntimeException("Category with this name already exists");
        }
        if (image != null && !image.isEmpty()) {
            String fileName = fileStorageService.save(image);
            category.setImagePath("/uploads/" + fileName);
        }
        return categoryRepository.save(category);
    }

    @Override
    public Category update(Long id, Category category) {
        Category existing = categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Category not found"));

        if (category.getName() != null) {
            if (!category.getName().equals(existing.getName()) &&
                    categoryRepository.existsByName(category.getName())) {
                throw new RuntimeException("Category with this name already exists");
            }
            existing.setName(category.getName());
        }
        if (category.getImagePath() != null) {
            existing.setImagePath(category.getImagePath());
        }

        return categoryRepository.save(existing);
    }

    @Override
    public Category update(Long id, Category category, MultipartFile image) {
        Category existing = categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Category not found"));

        if (category.getName() != null) {
            if (!category.getName().equals(existing.getName()) &&
                    categoryRepository.existsByName(category.getName())) {
                throw new RuntimeException("Category with this name already exists");
            }
            existing.setName(category.getName());
        }

        if (image != null && !image.isEmpty()) {
            // Delete old image if it exists
            if (existing.getImagePath() != null) {
                String oldFileName = existing.getImagePath().replace("/uploads/", "");
                fileStorageService.delete(oldFileName);
            }
            String fileName = fileStorageService.save(image);
            existing.setImagePath("/uploads/" + fileName);
        }

        return categoryRepository.save(existing);
    }

    @Override
    public void delete(Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Category not found"));
        // Delete the image file if it exists
        if (category.getImagePath() != null) {
            String fileName = category.getImagePath().replace("/uploads/", "");
            fileStorageService.delete(fileName);
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
