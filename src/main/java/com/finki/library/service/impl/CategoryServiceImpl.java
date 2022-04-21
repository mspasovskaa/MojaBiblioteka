package com.finki.library.service.impl;

import com.finki.library.model.Book;
import com.finki.library.model.Category;
import com.finki.library.model.exceptions.BookNotFoundException;
import com.finki.library.model.exceptions.CategoryNotFoundException;
import com.finki.library.repository.BookRepository;
import com.finki.library.repository.CategoryRepository;
import com.finki.library.service.CategoryService;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;
    private final BookRepository bookRepository;

    public CategoryServiceImpl(CategoryRepository categoryRepository, BookRepository bookRepository) {
        this.categoryRepository = categoryRepository;
        this.bookRepository = bookRepository;
    }

    @Override
    public List<Category> listAll() {
        return this.categoryRepository.findAll();
    }

    @Override
    public Category findByName(String name) {
        return this.categoryRepository.findByName(name);
    }


    @Override
    public Category create(String name, String description) {
        List<Book> books=new ArrayList<>();
        Category category=new Category(name,description,books);
        return this.categoryRepository.save(category);
    }

    @Override
    public Category update(String name,Book book) {

            this.categoryRepository.findByName(name).getBooks().add(book);

        return this.categoryRepository.findByName(name);
    }

    @Override
    public Category delete(Long id) {
        Category category=this.findById(id);
        this.categoryRepository.delete(category);
        return category;
    }

    @Override
    public Category findById(Long id) {
        return this.categoryRepository.findById(id).orElseThrow(CategoryNotFoundException::new);
    }

    @Override
    public Category deleteBooks(Long id,Long bookid) {
        Category category=this.findById(id);
        Book book=this.bookRepository.findById(bookid).orElseThrow(()->new BookNotFoundException());
        category.getBooks().remove(book);
        return this.categoryRepository.save(category);
    }

    @Override
    public List<Category> findAllByBookIsFalse(Book book) {
        return this.categoryRepository.findAllByBooksNotContaining(book);
    }

}
