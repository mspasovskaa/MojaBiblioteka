package com.finki.library.service;

import com.finki.library.model.Borrow;
import com.finki.library.model.Copy;
import com.finki.library.model.User;
import java.util.List;

public interface BorrowService {
    Borrow findById(Long id);
    Borrow deleteByBook(Long id);
    Borrow create(User client, Copy copy);
    Borrow delete(Long id);
    List<Borrow> listAll();
    List<Borrow> findAllByClient(Long clientId);
}
