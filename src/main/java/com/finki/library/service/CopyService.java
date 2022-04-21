package com.finki.library.service;

import com.finki.library.model.Book;
import com.finki.library.model.Copy;
import java.util.List;

public interface CopyService {

    Copy findById(Long id);
    Copy create(Book book);
    Copy delete(Long id);
    void deleteAllByBook(Long bookId);
    List<Copy> findAllByBook(Long bookId);
}
