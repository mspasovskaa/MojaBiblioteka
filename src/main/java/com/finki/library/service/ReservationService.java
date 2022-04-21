package com.finki.library.service;

import com.finki.library.model.Reservation;
import java.util.List;

public interface ReservationService {

    List<Reservation> listAll();
    Reservation findById(Long id);
    //Reservation create(Long copyId, Long clientId);
    Reservation delete(String username,Long id);
    Reservation create(String username,Long id);
    List<Reservation> findAllByClient(Long clientId);
    Reservation deleteByBook(Long id);

}
