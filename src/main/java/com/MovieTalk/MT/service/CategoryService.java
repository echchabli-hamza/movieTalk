package com.MovieTalk.MT.service;

import com.MovieTalk.MT.entity.Category;
import java.util.List;

public interface CategoryService {
    Category add(Category category);
    Category update(Long id, Category category);
    void delete(Long id);
    List<Category> listAll();
    Category listOne(Long id);
}
