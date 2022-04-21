package com.finki.library.model;

import lombok.Data;
import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Entity
public class Borrow {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    private User client;
    @OneToOne
    private Copy copy;
    private LocalDateTime dueDate;

    public Borrow() {
    }

    public Borrow(User client, Copy copy, LocalDateTime dueDate) {
        this.client = client;
        this.copy = copy;
        this.dueDate = dueDate;
    }
}
