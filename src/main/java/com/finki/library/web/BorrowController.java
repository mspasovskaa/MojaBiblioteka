package com.finki.library.web;

import com.finki.library.model.Book;
import com.finki.library.model.Borrow;
import com.finki.library.model.Copy;
import com.finki.library.model.User;
import com.finki.library.model.enums.Role;
import com.finki.library.service.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.List;

@Controller
public class BorrowController {

    private final BookService bookService;
    private final BorrowService borrowService;
    private final CategoryService categoryService;
    private final UserService userService;
    private final CopyService copyService;
    private final ReservationService reservationService;
    private final EmailSenderService emailSenderService;
    public BorrowController(BookService bookService, BorrowService borrowService, CategoryService categoryService, UserService userService, CopyService copyService, ReservationService reservationService, EmailSenderService emailSenderService) {
        this.bookService = bookService;
        this.borrowService = borrowService;
        this.categoryService = categoryService;
        this.userService = userService;
        this.copyService = copyService;
        this.reservationService = reservationService;
        this.emailSenderService = emailSenderService;
    }

    @PostMapping("/library/borrow/{bookid}")
    public String getBorrowPage(@PathVariable Long bookid, Model model) {
        Book book = this.bookService.findById(bookid);
        model.addAttribute("book", book);
        return "createborrow";
    }


    @GetMapping("/library/borrowed")
    public String showBorrowed(Model model) {
        List<Borrow> borrowList = this.borrowService.listAll();
        model.addAttribute("borrows", borrowList);
        model.addAttribute("today", LocalDateTime.now());

        return "borrowlist";
    }
    @PostMapping("/library/deleteborrow/{id}")
    public String deleteBorrow(@PathVariable Long id) {

        Book book=this.borrowService.findById(id).getCopy().getBook();
        bookService.PlusNumOfCopies(book.getId());
        this.borrowService.delete(id);

        return "redirect:/library/borrowed";
    }

    @PostMapping("/library/createborrow")
    public String createBorrow(@RequestParam String username, @RequestParam Long bookid, HttpServletRequest request) {

        User user = this.userService.findByUsername(username);
        if(user.getAvailableBorrows()>=1) {
            List<Copy> copyList = this.copyService.findAllByBook(bookid);
            this.borrowService.create(user, copyList.get(copyList.size() - 1));
            bookService.MinusNumOfCopies(bookid);
            String remoteusername=request.getRemoteUser();
            User remoteuser=this.userService.findByUsername(remoteusername);
            if(remoteuser.getRole().equals(Role.ROLE_USER)) {
                return "redirect:/library/listbooks";
            }
            else if(remoteuser.getRole().equals(Role.ROLE_ADMIN)){
                return "redirect:/library/borrowed";
            }
        }
        return "redirect:/library/listbooks/?error=NoBorrowsAllowed";

    }

    @PostMapping("/library/createborrowfromreservation")
    public String createBorrowFromRes(@RequestParam String username, @RequestParam Long bookid, HttpServletRequest request) {

        User user = this.userService.findByUsername(username);
        if(user.getAvailableBorrows()>=1) {
            List<Copy> copyList = this.copyService.findAllByBook(bookid);
            this.borrowService.create(user, copyList.get(copyList.size() - 1));
            return "redirect:/library/borrowed";

        }
        return "redirect:/library/listbooks/?error=NoBorrowsAllowed";

    }
}
