package com.finki.library.service;

import com.finki.library.model.Book;
import com.finki.library.model.Category;
import java.util.List;

public interface CategoryService {

    List<Category> listAll();
    Category findByName(String name);
    Category create(String name, String description);
    Category update(String name,Book book);
    Category delete(Long id);
    Category findById(Long id);
    Category deleteBooks(Long id,Long bookId);
    List<Category> findAllByBookIsFalse(Book book);

}
