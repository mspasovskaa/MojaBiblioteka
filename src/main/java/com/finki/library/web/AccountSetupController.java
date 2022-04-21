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

@Controller
public class AccountSetupController {

    private final BookService bookService;
    private final BorrowService borrowService;
    private final CategoryService categoryService;
    private final UserService userService;
    private final CopyService copyService;
    private final ReservationService reservationService;
    private final EmailSenderService emailSenderService;
    public AccountSetupController(BookService bookService, BorrowService borrowService, CategoryService categoryService, UserService userService, CopyService copyService, ReservationService reservationService, EmailSenderService emailSenderService) {
        this.bookService = bookService;
        this.borrowService = borrowService;
        this.categoryService = categoryService;
        this.userService = userService;
        this.copyService = copyService;
        this.reservationService = reservationService;
        this.emailSenderService = emailSenderService;
    }
    @PostMapping("/library/add-client")
    public String addClient(@RequestParam String name,
                            @RequestParam String username, @RequestParam String mail)
    {
        String body="Вашето корисничко име е: "+username+".\n";
        body+="Можете да продолжите со поставување на вашата лозинка на следниот линк: \n";
        body+="http://localhost:8888/library/start-setup/?username="+username;

        this.userService.create(name, username, " ", mail, Role.ROLE_USER);


        String email=this.emailSenderService.sendMail(mail,"Поставување лозинка",body);
        return "redirect:/library/clients";

    }
    @PostMapping("/library/changePassword/{id}")
    public String changePassword(@PathVariable(required = false) Long id)
    {
        User user=this.userService.findById(id);
        String username=user.getUsername();
        String mail=user.getEmail();
        String body="Можете да продолжите со поставување на вашата лозинка на следниот линк: \n";
        body+="http://localhost:8888/library/start-setup/?username="+username;

        String email=this.emailSenderService.sendMail(mail,"Поставување лозинка",body);

        return "redirect:/library/accountInfo";

    }
    @GetMapping("/library/start-setup")
    public String startSetup(@RequestParam(required=false) String error, @RequestParam String username, Model model)
    {
        model.addAttribute("username",username);
        if(error!=null)
        {
            model.addAttribute("error","Лозинките не се совпаѓаат!");
        }
        return "account-setup";
    }
    @PostMapping("/library/setup-account")
    public String setupAccount(@RequestParam String username,@RequestParam String password,@RequestParam String repeatpassword)
    {
        if(password.equals(repeatpassword)) {
            User user = this.userService.findByUsername(username);
            this.userService.updatePassword(user.getName(), user.getUsername(), password, user.getEmail(), user.getRole());
            return "redirect:/login";
        }
        return "redirect:/library/start-setup/?error=NoMatch&username="+username;
    }
}
