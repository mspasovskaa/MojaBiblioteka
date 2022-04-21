package com.finki.library.model;

import lombok.Data;
import com.finki.library.model.enums.BookStatus;

import javax.persistence.*;
import java.util.List;

@Entity
@Data
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private String author;
    private Integer year;
    @ManyToMany(mappedBy = "books",cascade = CascadeType.ALL)
    private List<Category> categories;
    private int numCopies;
    @Enumerated(EnumType.STRING)
    private BookStatus status;
    private String imageURL;

    public Book() {
    }

    public Book(String title, String author, Integer year, List<Category> categories, int numCopies, BookStatus status,String imageURL) {
        this.title = title;
        this.author = author;
        this.year = year;
        this.categories = categories;
        this.numCopies = numCopies;
        this.status = status;
        this.imageURL=imageURL;
    }
}
