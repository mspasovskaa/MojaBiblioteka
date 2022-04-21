package com.finki.library.service.impl;

import com.finki.library.model.Book;
import com.finki.library.model.Copy;
import com.finki.library.model.Reservation;
import com.finki.library.model.User;
import com.finki.library.model.enums.BookStatus;
import com.finki.library.model.exceptions.*;
import com.finki.library.repository.BookRepository;
import com.finki.library.repository.CopyRepository;
import com.finki.library.repository.ReservationRepository;
import com.finki.library.repository.UserRepository;
import com.finki.library.service.ReservationService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

@Service

public class ReservationServiceImpl implements ReservationService {
    private final ReservationRepository reservationRepository;
    private final CopyRepository copyRepository;
    private final UserRepository clientRepository;
    private final BookRepository bookRepository;

    public ReservationServiceImpl(ReservationRepository reservationRepository, CopyRepository copyRepository, UserRepository clientRepository, BookRepository bookRepository) {
        this.reservationRepository = reservationRepository;
        this.copyRepository = copyRepository;
        this.clientRepository = clientRepository;
        this.bookRepository = bookRepository;
    }

    @Override
    public List<Reservation> listAll() {
        return reservationRepository.findAll();
    }

    @Override
    public Reservation findById(Long id) {
        return this.reservationRepository.findById(id).orElseThrow(() -> new ReservationNotFoundException());
    }


    @Override
    public Reservation delete(String username, Long id) {
        Book book = this.bookRepository.findById(id).orElseThrow(() -> new BookNotFoundException());
        int numCopies = book.getNumCopies();
        User user = this.clientRepository.findByUsername(username).orElseThrow(() -> new UserNotFoundException());
        List<Copy> copies = this.copyRepository.findAllByBook_Id(id);
        List<Reservation> reservations = this.reservationRepository.findAllByClient_Id(user.getId());

        for (Copy c : copies) {
            for (Reservation r : reservations) {

                if (c.getId().equals(r.getCopy().getId())) {
                    this.reservationRepository.delete(r);
                    book.setNumCopies(numCopies + 1);
                    user.setAvailableReservations(user.getAvailableReservations() + 1);
                    this.bookRepository.save(book);
                    return r;
                }
            }
        }
        return (Reservation) null;
    }

    @Override
    public Reservation create(String username, Long id) {
        Book book = this.bookRepository.findById(id).orElseThrow(() -> new BookNotFoundException());
        User user = this.clientRepository.findByUsername(username).orElseThrow(() -> new UserNotFoundException());
        List<Copy> copies = this.copyRepository.findAllByBook_Id(id);
        if (user.getAvailableReservations() < 1)
            return (Reservation) null;
        else {
            if (book.getNumCopies() != 0) {
                Reservation reservation = new Reservation(copies.get(copies.size() - 1), user);
                this.reservationRepository.save(reservation);
                book.setNumCopies(book.getNumCopies() - 1);
                if (book.getNumCopies() == 0) {
                    book.setStatus(BookStatus.UNAVAILABLE);
                }
                this.bookRepository.save(book);
                user.setAvailableReservations(user.getAvailableReservations() - 1);
                clientRepository.save(user);
                //timer implementation
                Timer timer = new Timer();
                TimerTask timerTask = new TimerTask() {
                    @Override
                    public void run() {
                        reservationRepository.delete(reservation);
                        user.setAvailableReservations(user.getAvailableReservations() + 1);
                        clientRepository.save(user);
                        book.setNumCopies(book.getNumCopies() + 1);
                        book.setStatus(BookStatus.AVAILABLE);
                        bookRepository.save(book);
                    }
                };
                timer.schedule(timerTask, 30000);

                return reservation;
            }
        }
        return (Reservation) null;

    }

    @Override
    public List<Reservation> findAllByClient(Long clientId) {
        List<Reservation> reservations = this.reservationRepository.findAllByClient_Id(clientId);
        return reservations;
    }

    @Override
    public Reservation deleteByBook(Long id) {
        Book book = this.bookRepository.findById(id).orElseThrow(() -> new BookNotFoundException());
        List<Reservation> reservations = this.reservationRepository.findAllByCopyBook(book);
        for (Reservation r : reservations
        ) {
            if (r.getCopy().getBook().equals(book)) {
                this.reservationRepository.delete(r);
            }
        }

        return (Reservation) null;
    }
}
