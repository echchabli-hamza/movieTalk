package com.MovieTalk.MT.service;

import com.MovieTalk.MT.entity.Category;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;

public interface CategoryService {
    Category add(Category category);

    Category add(Category category, MultipartFile image);

    Category update(Long id, Category category);

    Category update(Long id, Category category, MultipartFile image);

    void delete(Long id);

    List<Category> listAll();

    Category listOne(Long id);
}
