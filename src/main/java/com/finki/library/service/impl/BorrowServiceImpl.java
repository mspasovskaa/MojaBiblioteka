package com.finki.library.service.impl;

import com.finki.library.model.*;
import com.finki.library.model.exceptions.BookNotFoundException;
import com.finki.library.model.exceptions.BorrowNotFoundException;
import com.finki.library.repository.BookRepository;
import com.finki.library.repository.BorrowRepository;
import com.finki.library.repository.UserRepository;
import com.finki.library.service.BorrowService;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class BorrowServiceImpl implements BorrowService {
    private final BorrowRepository borrowRepository;
    private final UserRepository userRepository;
    private final BookRepository bookRepository;

    public BorrowServiceImpl(BorrowRepository borrowRepository, UserRepository userRepository, BookRepository bookRepository) {
        this.borrowRepository = borrowRepository;
        this.userRepository = userRepository;
        this.bookRepository = bookRepository;
    }

    @Override
    public Borrow findById(Long id) {
        return this.borrowRepository.findById(id).orElseThrow(()->new BorrowNotFoundException());
    }

    @Override
    public Borrow deleteByBook(Long id) {
        Book book=this.bookRepository.findById(id).orElseThrow(()->new BookNotFoundException());
        List<Borrow> borrowList=this.borrowRepository.findAll();
        for (Borrow b:borrowList
             ) {
            if(b.getCopy().getBook().equals(book))
            {
                this.borrowRepository.delete(b);
            }
        }
        return (Borrow) null;
    }

    @Override
    public Borrow create(User client, Copy copy) {
        LocalDateTime today=LocalDateTime.now();

        LocalDateTime dueDate = today.plusMinutes(3);
        if(client.getAvailableBorrows()<1)
            return (Borrow) null;
        else {
            Borrow borrow = new Borrow(client, copy, dueDate);
            client.setAvailableBorrows(client.getAvailableBorrows() - 1);
            return this.borrowRepository.save(borrow);
        }
    }

    @Override
    public Borrow delete(Long id) {
        Borrow borrow=this.findById(id);
        User user=borrow.getClient();
        user.setAvailableBorrows(user.getAvailableBorrows()+1);
        this.userRepository.save(user);
        this.borrowRepository.delete(borrow);
        return borrow;
    }

    @Override
    public List<Borrow> listAll() {
        return this.borrowRepository.findAll();
    }

    @Override
    public List<Borrow> findAllByClient(Long clientId) {
        List<Borrow> borrowList = this.borrowRepository.findAllByClient_Id(clientId);
        return borrowList;
    }
}
