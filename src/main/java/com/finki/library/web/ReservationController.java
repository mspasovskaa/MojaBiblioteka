package com.finki.library.web;

import com.finki.library.model.Book;
import com.finki.library.model.Reservation;
import com.finki.library.model.User;
import com.finki.library.service.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
public class ReservationController {
    private final BookService bookService;
    private final BorrowService borrowService;
    private final CategoryService categoryService;
    private final UserService userService;
    private final CopyService copyService;
    private final ReservationService reservationService;
    private final EmailSenderService emailSenderService;

    public ReservationController(BookService bookService, BorrowService borrowService, CategoryService categoryService, UserService userService, CopyService copyService, ReservationService reservationService, EmailSenderService emailSenderService) {
        this.bookService = bookService;
        this.borrowService = borrowService;
        this.categoryService = categoryService;
        this.userService = userService;
        this.copyService = copyService;
        this.reservationService = reservationService;
        this.emailSenderService = emailSenderService;
    }

    @GetMapping("/library/reservationlist")
    public String getHomePage(Model model) {

        List<Reservation> reservations=this.reservationService.listAll();
        model.addAttribute("reservations",reservations);
        return "reservationlist";
    }
    @PostMapping("/library/reserve/{bookid}")
    public String createReservation(@PathVariable Long bookid, HttpServletRequest request) {

        String username = request.getRemoteUser();
        User user=this.userService.findByUsername(username);
        if(user.getAvailableReservations()>=1) {
            List<Reservation> reservations=this.reservationService.findAllByClient(user.getId());
            Book book=this.bookService.findById(bookid);
            for (Reservation r: reservations) {
                if(r.getCopy().getBook().equals(book))
                {
                    return "redirect:/library/listbooks/?error=NoReservationsAllowed";
                }

            }
            this.reservationService.create(username, bookid);
            return "redirect:/library/listbooks";
        }
        return "redirect:/library/listbooks/?error=NoReservationsAllowed";

    }

    @PostMapping("/library/reserve/delete/{id}")
    public String deleteReservation(@PathVariable Long id, HttpServletRequest request) {
        Book book = this.bookService.findById(id);
        String username = request.getRemoteUser();
        this.reservationService.delete(username, id);
        bookService.PlusNumOfCopies(id);
        return "redirect:/library/myBooks";

    }
}
