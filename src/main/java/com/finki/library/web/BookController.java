package com.finki.library.web;

import com.finki.library.model.*;
import com.finki.library.model.enums.BookStatus;
import com.finki.library.service.*;
import com.lowagie.text.DocumentException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@Controller
public class BookController
{
    private final BookService bookService;
    private final BorrowService borrowService;
    private final CategoryService categoryService;
    private final UserService userService;
    private final CopyService copyService;
    private final ReservationService reservationService;
    private final EmailSenderService emailSenderService;
    private final PDFExporterService pdfExporterService;
    public BookController(BookService bookService, BorrowService borrowService, CategoryService categoryService, UserService userService, CopyService copyService, ReservationService reservationService, EmailSenderService emailSenderService, PDFExporterService pdfExporterService) {
        this.bookService = bookService;
        this.borrowService = borrowService;
        this.categoryService = categoryService;
        this.userService = userService;
        this.copyService = copyService;
        this.reservationService = reservationService;
        this.emailSenderService = emailSenderService;
        this.pdfExporterService = pdfExporterService;
    }
    @GetMapping("/library/listbooks")
    public String getListBooks(@RequestParam(required = false) Long categoryId, @RequestParam(required = false) String error, Model model, HttpServletRequest request) {
        if(error!=null) {
            if (error.equals("NoReservationsAllowed")) {
                model.addAttribute("error", "Немате право за повеќе резервации!");
            } else if (error.equals("NoBorrowsAllowed")) {
                model.addAttribute("error", "Корисникот нема право за повеќе позајмици!");

            }
        }
        List<Book> books;
        if (categoryId == null) {
            books = this.bookService.listAll();
            Collections.sort(books, new TitleComparator());
        } else {
            books = this.bookService.listBooksByCategory(categoryId);
        }
        List<Category> categories = this.categoryService.listAll();
        model.addAttribute("books", books);
        model.addAttribute("categories", categories);
        model.addAttribute("reservations", reservationService.listAll());
        return "listbooks";
    }
    @GetMapping("/library/myBooks")
    public String getMyBooks(Model model, HttpServletRequest request) {
        String username = request.getRemoteUser();
        User user = this.userService.findByUsername(username);
        List<Reservation> reservations = this.reservationService.findAllByClient(user.getId());
        List<Book> bookList = new ArrayList<>();
        for (Reservation r : reservations) {
            bookList.add(r.getCopy().getBook());
        }
        model.addAttribute("books", bookList);
        model.addAttribute("clientname", user.getName());

        List<Borrow> borrowList = this.borrowService.findAllByClient(user.getId());
        List<Book> bookList2 = new ArrayList<>();
        for (Borrow b : borrowList) {
            bookList2.add(b.getCopy().getBook());
        }
        model.addAttribute("borrows", bookList2);

        return "myBooks";
    }

    @PostMapping("/library/export/pdf/{id}")
    public void exportToPDF(@PathVariable Long id, HttpServletResponse response, HttpServletRequest request) throws DocumentException, IOException {
        response.setContentType("application/pdf");
        DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
        String currentDateTime = dateFormatter.format(new Date());

        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename=receipt_" + currentDateTime + ".pdf";
        response.setHeader(headerKey, headerValue);


        String username = request.getRemoteUser();
        User user = this.userService.findByUsername(username);
        this.pdfExporterService.export(response,user,id);



    }

    @GetMapping("/library/add-book")
    public String showAdd(Model model) {
        List<Category> categories = this.categoryService.listAll();
        model.addAttribute("types", BookStatus.values());
        model.addAttribute("categories", categories);
        return "edit";
    }
    @GetMapping("/library/edit/{id}")
    public String showEdit(@PathVariable Long id, Model model) {
        Book book = this.bookService.findById(id);
        List<Category> categories = this.categoryService.findAllByBookIsFalse(book);
        List<Category> categoryList=book.getCategories();
        List<Category> categories1=new ArrayList<>();
        for (Category c:categoryList
        ) {
            if(!categories1.contains(c))
            {
                categories1.add(c);
            }
        }

        model.addAttribute("types", BookStatus.values());
        model.addAttribute("categories1",categories1);
        model.addAttribute("categories", categories);
        model.addAttribute("book", book);
        return "edit";
    }
    @PostMapping("/library/delete/{id}")
    public String delete(@PathVariable Long id) {
        Book book = this.bookService.findById(id);
        List<Category> categories = book.getCategories().stream().toList();
        for (Category cat : categories) {
            this.categoryService.deleteBooks(cat.getId(), book.getId());
        }
        this.bookService.deleteCategories(id);
        this.reservationService.deleteByBook(id);
        this.borrowService.deleteByBook(id);
        this.copyService.deleteAllByBook(id);
        return "redirect:/library/listbooks";
    }
    @PostMapping("/library/books/{id}")
    public String update(@PathVariable Long id,
                         @RequestParam String title,
                         @RequestParam String author,
                         @RequestParam int year,
                         @RequestParam Integer numCopies,
                         @RequestParam BookStatus type,
                         @RequestParam List<Long> category,
                         @RequestParam String image,
                         HttpServletRequest request) {
        String[] categories = request.getParameterValues("category");
        List<Long> cat = new ArrayList<>();
        for (int i = 0; i < categories.length; i++) {
            cat.add(Long.parseLong(categories[i]));
        }
        this.bookService.update(id, title, author, year, cat, numCopies, type,image);
        return "redirect:/library/listbooks";
    }


    @PostMapping("/library/books")
    public String create(@RequestParam String title,
                         @RequestParam String author,
                         @RequestParam Integer year,
                         @RequestParam Integer numCopies,
                         @RequestParam BookStatus type,
                         @RequestParam List<Long> category,
                         @RequestParam String image,
                         HttpServletRequest request) {
        String[] categories = request.getParameterValues("category");
        List<Long> cat = new ArrayList<>();
        for (int i = 0; i < categories.length; i++) {
            cat.add(Long.parseLong(categories[i]));
        }
        this.bookService.add(title, author, year, cat, numCopies, type,image);
        return "redirect:/library/listbooks";
    }

}

class TitleComparator implements Comparator<Book> {
    @Override
    public int compare(Book o1, Book o2) {
        return o1.getTitle().compareTo(o2.getTitle());
    }
}