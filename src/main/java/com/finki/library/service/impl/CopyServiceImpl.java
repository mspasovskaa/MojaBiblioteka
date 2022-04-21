package com.finki.library.service.impl;

import com.finki.library.model.Book;
import com.finki.library.model.Copy;
import com.finki.library.model.exceptions.BookNotFoundException;
import com.finki.library.model.exceptions.CopyNotFoundException;
import com.finki.library.repository.BookRepository;
import com.finki.library.repository.CopyRepository;
import com.finki.library.service.CopyService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CopyServiceImpl implements CopyService {
    private final CopyRepository copyRepository;
    private final BookRepository bookRepository;

    public CopyServiceImpl(CopyRepository copyRepository, BookRepository bookRepository) {
        this.copyRepository = copyRepository;
        this.bookRepository = bookRepository;
    }

    @Override
    public Copy findById(Long id) {
        return this.copyRepository.findById(id).orElseThrow(()->new CopyNotFoundException());
    }

    @Override
    public Copy create(Book book) {
        Copy copy=new Copy(book);
        return this.copyRepository.save(copy);
    }

    @Override
    public Copy delete(Long id) {
        Copy copy=this.copyRepository.findById(id).orElseThrow(CopyNotFoundException::new);
        this.copyRepository.delete(copy);
        return copy;
    }

    @Override
    public void deleteAllByBook(Long bookId) {
        Book book=this.bookRepository.findById(bookId).orElseThrow(()->new BookNotFoundException());
        List<Copy> copyList=this.copyRepository.findAll();
        for (Copy c: copyList) {
            if(c.getBook().equals(book))
            {
                this.copyRepository.delete(c);
            }
        }
    }

    @Override
    public List<Copy> findAllByBook(Long bookId) {
        List<Copy> copyList=this.copyRepository.findAllByBook_Id(bookId);
        return copyList;
    }
}
