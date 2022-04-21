package com.finki.library.web;

import com.finki.library.model.User;
import com.finki.library.model.enums.Role;
import com.finki.library.service.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;

@Controller

public class HomeController {
    private final BookService bookService;
    private final BorrowService borrowService;
    private final CategoryService categoryService;
    private final UserService userService;
    private final CopyService copyService;
    private final ReservationService reservationService;
    private final EmailSenderService emailSenderService;

    public HomeController(BookService bookService, BorrowService borrowService, CategoryService categoryService, UserService userService, CopyService copyService, ReservationService reservationService, EmailSenderService emailSenderService) {
        this.bookService = bookService;
        this.borrowService = borrowService;
        this.categoryService = categoryService;
        this.userService = userService;
        this.copyService = copyService;
        this.reservationService = reservationService;
        this.emailSenderService = emailSenderService;
    }

    @GetMapping({"/library", "/"})
    public String getHomePage(Model model, HttpServletRequest request) {
    //    this.userService.create("ana", "ana.anevska", "aa", "ana@yahoo.com", Role.ROLE_ADMIN);
        String username = request.getRemoteUser();
        if (username != null) {
            User user = this.userService.findByUsername(username);
            model.addAttribute("user", user);
        }
        return "home";
    }

    @GetMapping("/library/accountInfo")
    public String getAccountInfo(Model model, HttpServletRequest request) {
        String username = request.getRemoteUser();
        User user = this.userService.findByUsername(username);
        model.addAttribute("user", user);
        return "accountInfo";
    }

    @GetMapping("/library/map")
    public String getMap() {
        return "map";
    }
}
