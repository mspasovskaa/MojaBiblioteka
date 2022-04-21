package com.finki.library.model;


import javax.persistence.*;


@Entity
public class Reservation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @OneToOne
    private Copy copy;
    @ManyToOne
    private User client;

    public Reservation() {
    }

    public Reservation(Copy copy, User client) {
        this.copy = copy;
        this.client = client;

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Copy getCopy() {
        return copy;
    }

    public void setCopy(Copy copy) {
        this.copy = copy;
    }

    public User getClient() {
        return client;
    }

    public void setClient(User client) {
        this.client = client;
    }
}
