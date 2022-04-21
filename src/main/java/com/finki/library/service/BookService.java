package com.finki.library.service;

import com.finki.library.model.Book;
import com.finki.library.model.Category;
import com.finki.library.model.enums.BookStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface BookService {

    List<Book> listAll();
    Book findById(Long id);
    Book delete(Long id);
    Book update(Long id, String title, String author, int year, List<Long> categoriesIds, int numCopies, BookStatus status,String imageURL);
    Book deleteCategories(Long id);
    Book add(String title, String author, int year, List<Long> categoryId, int numCopies, BookStatus status,String imageURL);
    List<Book> listBooksByCategory(Long categoryId);
    Book PlusNumOfCopies(Long id);
    Book MinusNumOfCopies(Long id);

}
