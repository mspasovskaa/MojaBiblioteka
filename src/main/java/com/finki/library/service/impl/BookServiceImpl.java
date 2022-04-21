package com.finki.library.service.impl;

import com.finki.library.model.Book;
import com.finki.library.model.Category;
import com.finki.library.model.Copy;
import com.finki.library.model.enums.BookStatus;
import com.finki.library.model.exceptions.BookNotFoundException;
import com.finki.library.model.exceptions.CategoryNotFoundException;
import com.finki.library.repository.BookRepository;
import com.finki.library.repository.CategoryRepository;
import com.finki.library.repository.CopyRepository;
import com.finki.library.service.BookService;
import com.finki.library.service.CategoryService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class BookServiceImpl implements BookService {
    private final BookRepository bookRepository;
    private final CategoryRepository categoryRepository;
    private final CategoryService categoryService;
    private final CopyRepository copyRepository;

    public BookServiceImpl(BookRepository bookRepository, CategoryRepository categoryRepository, CategoryService categoryService, CopyRepository copyRepository) {
        this.bookRepository = bookRepository;
        this.categoryRepository = categoryRepository;
        this.categoryService = categoryService;
        this.copyRepository = copyRepository;
    }

    @Override
    public List<Book> listAll() {
        return this.bookRepository.findAll();
    }

    @Override
    public Book findById(Long id) {
        return this.bookRepository.findById(id).orElseThrow(() -> new BookNotFoundException());
    }


    @Override
    public Book delete(Long id) {
        Book book = this.bookRepository.findById(id).orElseThrow(BookNotFoundException::new);
        this.bookRepository.delete(book);
        return book;
    }

    @Override
    public Book update(Long id, String title, String author, int year, List<Long> categoriesIds, int numCopies, BookStatus status,String imageURL) {
        List<Category> categories1 = this.categoryRepository.findAllById(categoriesIds);
        List<Category> allcategories=this.categoryRepository.findAll();
        Book book = this.findById(id);
        book.setTitle(title);
        book.setAuthor(author);
        book.setYear(year);
        book.setCategories(categories1);
        book.setNumCopies(numCopies);
        book.setStatus(status);
        book.setImageURL(imageURL);
        for (Category c:allcategories
             ) {
            if(!categories1.contains(c))
            {
                this.categoryService.deleteBooks(c.getId(),book.getId());
            }
        }
        for (Category cat : categories1) {
            this.categoryService.update(cat.getName(), book);
        }

        return this.bookRepository.save(book);
    }

    @Override
    public Book deleteCategories(Long id) {
        Book book = this.findById(id);
        book.getCategories().clear();
        return book;
    }

    @Override
    public Book add(String title, String author, int year, List<Long> categoryId, int numCopies, BookStatus status,String imageURL) {

        List<Category> category = this.categoryRepository.findAllById(categoryId);
        Book book = new Book(title, author, year, category, numCopies, status,imageURL);

        for (Category cat : category) {
            this.categoryService.update(cat.getName(), book);
        }
        ;
        Integer numCopies1 = book.getNumCopies();
        for (int i = 0; i < numCopies1; i++) {
            Copy copy = new Copy(book);
            this.copyRepository.save(copy);
        }

        return this.bookRepository.save(book);

    }

    @Override
    public List<Book> listBooksByCategory(Long categoryId) {
        Category category;
        if (categoryId != null) {
            category = this.categoryRepository.findById(categoryId).orElseThrow(CategoryNotFoundException::new);
        } else {
            category = (Category) null;
        }
        if (category != null) {
            return this.bookRepository.findAllByCategoriesContaining(category);
        } else {
            return this.bookRepository.findAll();
        }
    }

    @Override
    public Book PlusNumOfCopies(Long id) {
        Book book = this.bookRepository.findById(id).orElseThrow(() -> new BookNotFoundException());
        book.setNumCopies(book.getNumCopies() + 1);
        book.setStatus(BookStatus.AVAILABLE);
        return bookRepository.save(book);
    }

    @Override
    public Book MinusNumOfCopies(Long id) {
        Book book = this.bookRepository.findById(id).orElseThrow(() -> new BookNotFoundException());
        book.setNumCopies(book.getNumCopies() - 1);
        if (book.getNumCopies() == 0) {
            book.setStatus(BookStatus.UNAVAILABLE);
        }
        return bookRepository.save(book);
    }


}
