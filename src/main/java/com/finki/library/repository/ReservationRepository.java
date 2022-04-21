package com.finki.library.repository;

import com.finki.library.model.Book;
import com.finki.library.model.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    List<Reservation> findAllByClient_Id(Long clientId);
    Reservation findByClient_IdAndCopy_Id(Long clientId,Long copyId);
    Reservation findByClient_Id(Long clientId);
    Reservation deleteByCopy_Book(Book book);
    List<Reservation> findAllByCopyBook(Book book);
}
