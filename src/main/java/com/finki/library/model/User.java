package com.finki.library.model;

import com.finki.library.model.enums.Role;
import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Entity
@Data
@Table(name = "library_users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String username;
    private String password;
    private String email;
    @Enumerated(EnumType.STRING)
    private Role role;
    private int availableReservations;
    private int availableBorrows;
    @OneToMany(mappedBy = "client",cascade = CascadeType.ALL)
    private List<Reservation> reservationList;
    @OneToMany(mappedBy = "client",cascade = CascadeType.ALL)
    private List<Borrow> borrowList;




    public User() {
    }

    public User(String name, String username, String password, String email, Role role) {
        this.name = name;
        this.username = username;
        this.password = password;
        this.email = email;
        this.role = role;
        this.availableBorrows=3;
        this.availableReservations=3;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public int getAvailableReservations() {
        return availableReservations;
    }

    public void setAvailableReservations(int availableReservations) {
        this.availableReservations = availableReservations;
    }

    public int getAvailableBorrows() {
        return availableBorrows;
    }

    public void setAvailableBorrows(int availableBorrows) {
        this.availableBorrows = availableBorrows;
    }
}
