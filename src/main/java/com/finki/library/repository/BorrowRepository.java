package com.finki.library.repository;

import com.finki.library.model.Book;
import com.finki.library.model.Borrow;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BorrowRepository extends JpaRepository<Borrow, Long> {
    Borrow deleteByCopy_Book(Book book);
    List<Borrow> findAllByCopyBook(Book book);
    List<Borrow> findAllByClient_Id(Long clientId);
}
