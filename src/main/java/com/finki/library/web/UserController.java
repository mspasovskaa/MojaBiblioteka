package com.finki.library.web;

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
import java.util.List;

@Controller
public class UserController {
    private final BookService bookService;
    private final BorrowService borrowService;
    private final CategoryService categoryService;
    private final UserService userService;
    private final CopyService copyService;
    private final ReservationService reservationService;
    private final EmailSenderService emailSenderService;
    public UserController(BookService bookService, BorrowService borrowService, CategoryService categoryService, UserService userService, CopyService copyService, ReservationService reservationService, EmailSenderService emailSenderService) {
        this.bookService = bookService;
        this.borrowService = borrowService;
        this.categoryService = categoryService;
        this.userService = userService;
        this.copyService = copyService;
        this.reservationService = reservationService;
        this.emailSenderService = emailSenderService;
    }
    @GetMapping("/library/clients")
    public String getClients(Model model)
    {
        List<User> clients=this.userService.findAllByRole(Role.ROLE_USER);
        model.addAttribute("clients",clients);
        return "listclients";
    }
    @GetMapping("/library/add-client")
    public String showAddClient(Model model)
    {
        return "add-client";
    }

    @PostMapping("/library/delete-client/{id}")
    public String deleteClient(@PathVariable Long id)
    {
        this.userService.delete(id);
        return "redirect:/library/clients";
    }
    @PostMapping("/library/edit-acc/{id}")
    public String showEditUser(@PathVariable Long id, Model model) {
        User user = this.userService.findById(id);
        model.addAttribute("user", user);
        return "edit-user";
    }

    @PostMapping("/library/user/{id}")
    public String updateUser(@PathVariable Long id, @RequestParam String name,@RequestParam String username, @RequestParam String mail, Model model,HttpServletRequest request) {
        User user = this.userService.findById(id);
        model.addAttribute("user", user);
        this.userService.update(id, name, username,mail);
        return "redirect:/login";
    }
}
